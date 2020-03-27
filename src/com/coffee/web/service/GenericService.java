package com.coffee.web.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GenericService extends HttpServlet
{
	protected boolean enableErrorLog = false; // 是否打印异常输出
	protected int MAX_REQUEST_SIZE = 1024 * 512; // 允许上传的JSON最大长度
	
	protected HashMap<String, ConfigItem> configs = new HashMap<String, ConfigItem>();
	
	@Override
	public void init() throws ServletException
	{
		// 从xml配置文件中读取配置
		try
		{
			loadConfig();
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new Error("LW-service.xml 格式不正确! 启动终止启动!");
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException
	{
		// 无论是 GET/POST, 均统一处理
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{		
		// 处理请求数据	
		try{
			handleRequest(request, response);
		}		
		catch(Exception e)
		{
			if(enableErrorLog) e.printStackTrace();
			response.sendError(500, e.getMessage());
			return;
		}
	}
	
	private void handleRequest(HttpServletRequest request, 
			HttpServletResponse response) throws Exception
	{
		// 从URL中解析API的名字
		// servletPath: "/.../hello.do"
		String servletPath = request.getServletPath();
		int p1 = servletPath.lastIndexOf('/');
		int p2 = servletPath.lastIndexOf('.');
		String apiName = servletPath.substring(p1 + 1, p2);
		
		// 查找相关的配置
		ConfigItem cfg = configs.get(apiName);
		if(cfg == null)
			throw new Exception("服务" + apiName + "在LW-service.xml里没有配置!");
		if(cfg.clazz == null)
		{
			try{
				cfg.clazz = Class.forName(cfg.clazzName);
			}catch(Exception e)	{
				throw new Exception("找不到服务" + apiName + "的类" + cfg.clazzName );
			}
		}			
		
		// 创建服务类的对象, 处理该请求
		GenericDo instance = null;
		try{
			instance = (GenericDo) cfg.clazz.newInstance();				
		}catch(InstantiationException e){
			e.printStackTrace();
			throw new Exception(cfg.clazzName + "无法实例化, 请确保构造方法不带参数!");
		}catch(IllegalAccessException e){
			e.printStackTrace();
			throw new Exception(cfg.clazzName + "无法实例化, 请确保构造方法为public!");
		}catch(ClassCastException e){
			e.printStackTrace();
			throw new Exception(cfg.clazzName + "必须是  GenericDo 的子类(或子类的子类)!");
		}catch(Exception e)	{
			e.printStackTrace();
			throw new Exception("在创建 " + cfg.clazzName + "实例的时候出错!请检查构造方法是否有异常!");
		}

		// 读取请求数据 和 URL里的参数
		String strReq = ServiceUtils.readAsText(request.getInputStream(), cfg.charset, MAX_REQUEST_SIZE);

		// 默认不再提取 URL里的参数，如果需要，则由子类调用 FormData.parse(query, "UTF-8") 提取
//		String query = request.getQueryString(); 
//		HashMap<String,String> queryParams = ServiceUtils.parseQuery(query, cfg.charset);
		
		// 读取请求数据, 转成字符串, 转成 JSON
		instance.httpReq = request;
		instance.httpResp = response;
		instance.charset = cfg.charset;
		String strResp = instance.execute(strReq);
					
		// 发送应答给客户端
		response.setCharacterEncoding(cfg.charset);
		response.setContentType("text/plain");
		//response.setHeader("Connection", "close");
		Writer writer = response.getWriter();
		writer.write( strResp );
		writer.close();	
	}
	
	
	/////////////////////////////////////
	// LW-service.xml 中的配置项
	class ConfigItem
	{
		public String name;       // 服务接口名
		public String clazzName;  // 类名
		public Class  clazz;      // 类的实体
		public String charset = "UTF-8";
		
		public ConfigItem(String name, String clazzName)
		{
			this.name = name;
			this.clazzName = clazzName;
		}
	}
	
	// 从 LW-service.xml 中获取配置
	private void loadConfig() throws Exception
	{
		InputStream stream = this.getClass().getResourceAsStream(
				"/LW-service.xml");
		SAXReader reader = new SAXReader();
		Document doc = reader.read(stream);
		stream.close();
		
		Element root = doc.getRootElement();
		List<Element> xServiceList = root.elements("service");
		for (Element e : xServiceList)
		{
			String name = e.attributeValue("name");
			String clazzName = e.attributeValue("class");			
			configs.put(name, new ConfigItem(name, clazzName));
		}
	}
}
