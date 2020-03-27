package com.coffee.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


import com.mchange.v2.c3p0.ComboPooledDataSource;

import com.coffee.mysql.Sql;
import com.coffee.mysql.SqlConnection;

/**
 * SimpleDB工具： 内含一个 C3P0 连接池
 *
 */
public class DB
{
	// 创建连接池 , 全局对象, 从 c3p0-config.xml 里读取默认配置
	public static ComboPooledDataSource pool = new ComboPooledDataSource(); 
	
	/** 一部分参数由 c3p0-config.xml读取, 一部分参数手工配置
	 * 
	 */
	public static void setConfig(String jdbcUrl, String user, String password	) throws Exception
	{
		pool.setDriverClass( "com.mysql.jdbc.Driver" );
		pool.setJdbcUrl( jdbcUrl );
		pool.setUser(user);                                  
		pool.setPassword(password); 
	}
	
	/** 获取连接 , 并包装为 AfSqlConnection
	 * 
	 */
	public static SqlConnection getConnection() throws Exception
	{
		Connection connection = pool.getConnection();
		return new SqlConnection(Sql.MYSQL, connection);
	}
	
	/** 执行 insert update delete 等SQL
	 * 
	 */
	public static int execute(String sql) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.execute(sql);
		}finally{
			conn.close();
		}	
	}
	
	// 执行 SELECT 操作
	public static ResultSet executeQuery(String sql) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.executeQuery(sql);
		}finally{
			conn.close();
		}	
	}
	
	// 封装查询, 返回 List<String[]> 
	public static List<String[]> query(String sql) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.query(sql);
		}finally{
			conn.close();
		}	
	}

	
	/** 查询结果，并返回多行数据，每一行是一个 Map对象
	 * 
	 * @param sql
	 * @param convert 转换参数,未用到,设为0
	 */
	public static List<Map> query(String sql, int convert) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.query(sql, convert);
		}finally{
			conn.close();
		}	
	}

	/** 查询，并自动转换为POJO对象
	 * 
	 * @param sql
	 * @param clazz 要转换成的POJO类
	 */
	public static List query(String sql, Class clazz) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.query(sql, clazz);
		}finally{
			conn.close();
		}			
	}
	
	/** 插入一个POJO对象
	 * 
	 * @param pojo 待插入的POJO对象
	 */
	public static void insert(Object pojo) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			conn.insert(pojo);
		}finally{
			conn.close();
		}	
	}
	
	
	/** 查询获取单行记录
	 * 
	 */
	public static String[] get(String sql) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.get(sql);
		}finally{
			conn.close();
		}	
	}
	
	/** 查询获取单行记录,转类相应的POJO类型
	 * 
	 * @param sql
	 * @param clazz 转成POJO类型
	 */
	public static Object get(String sql, Class clazz) throws Exception
	{
		SqlConnection conn = getConnection();
		try{
			return conn.get(sql, clazz);
		}finally{
			conn.close();
		}			
	}
	
}
