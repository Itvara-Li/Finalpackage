package com.coffee.web.restful;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.coffee.web.WebException;
import com.coffee.web.service.GenericDo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class RestfulDo extends GenericDo
{
	protected boolean enableErrorLog = true; // 是否打印异常输出
	protected boolean niceJSONFormat = true; // 输出的JSON是否综进 (缩进影响运行效率)
	
	public abstract Object execute(JsonObject jreq) throws Exception;	
	
	@Override
	public String execute(String strReq) throws Exception
	{
		// 处理请求数据
		//JsonObject jresp = new JsonObject();
		Map<String, Object> info = new HashMap <String, Object>();
		try
		{
			// 读取请求数据, 转成字符串, 转成 JSON
			JsonObject jreq = null;
			if (strReq.length() > 0)				
				jreq = new JsonParser().parse(strReq).getAsJsonObject();

			Object data = execute(jreq);
			info.put("error", 0); // 错误码,0表示成功
			info.put("reason", "OK"); // 错误原因描述, 如果没有错误则提示OK
			if (data != null)
				info.put("data", data); //
			
		} catch (WebException e)
		{
			String reason = e.getMessage();
			if(reason == null) reason = e.getClass().getName();
			System.out.println("** " + getClass().getName() + ": " + e.getMessage());
			
			// 出错应答
			info.put("error", e.error); // 错误码,0表示成功
			info.put("reason", reason); // 错误原因描述
			if (enableErrorLog)
				e.printStackTrace();
			
		} catch (Exception e)
		{
			String reason = e.getMessage();
			if(reason == null) reason = e.getClass().getName();
			System.out.println("** " + getClass().getName() + ": " + e.getMessage());

			info.put("error", -1); // 错误码,0表示成功
			info.put("reason", e.getMessage()); // 错误原因描述
			if (enableErrorLog)
				e.printStackTrace();
		}
		// 返回给客户端
		Gson jresp;
		if (niceJSONFormat) {
			jresp= new GsonBuilder()
            .setPrettyPrinting()//格式化json字符串的输出，默认情况下是输出一行，经过这个属性设置后会格式化输出，即有缩进的输出
            .setDateFormat("yyyy-MM-dd HH:mm:ss")//对时间进行格式化
            .create();
		}else {
			jresp=new Gson();
		}
		return jresp.toJson(info);
	}


}
