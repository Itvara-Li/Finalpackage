package com.coffee.mysql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** SQL 列值转换器
 * 将String类型转成 Java基础类型
 * 
 */
public class SqlValueParser
{
	// 时间转换
	private DateFormat sdf_dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat sdf_t = new SimpleDateFormat("HH:mm:ss");
	
	public Integer asInt(String str, Integer defValue)
	{
		if(str == null) return defValue;
		try {
			return Integer.valueOf( str );
		}catch(Exception e)
		{
			return defValue;
		}
	}
	
	public Long asLong(String str, Long defValue)
	{
		if(str == null) return defValue;
		try {
			return Long.valueOf( str );
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	
	public Short asShort(String str, Short defValue)
	{
		if(str == null) return defValue;
		try {
			return Short.valueOf( str );
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	
	public Byte asByte(String str, Byte defValue)
	{
		if(str == null) return defValue;
		try {
			return Byte.valueOf( str );
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	
	public Double asDouble(String str, Double defValue)
	{
		if(str == null) return defValue;
		try {
			return Double.valueOf( str );
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	
	public Float asFloat(String str, Float defValue)
	{
		if(str == null) return defValue;
		try {
			return Float.valueOf( str );
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	
	public Boolean asBoolean(String str, Boolean defValue)
	{
		if(str == null) return defValue;
		try {
			return ! str.equals("0");
		}catch(Exception e)
		{
			return defValue;
		}
	}
	
	public String asString(String str, String defValue)
	{
		if(str == null) return defValue;
		return str;
	}
	
	public Date asDateTime(String str, Date defValue)
	{
		if(str == null) return defValue;
		try {
			return sdf_dt.parse(str);
		}catch(Exception e)
		{
			return defValue;
		}	
	}
	
	public Date asDate(String str, Date defValue)
	{
		if(str == null) return defValue;
		try {
			return sdf_d.parse(str);
		}catch(Exception e)
		{
			return defValue;
		}	
	}
}
