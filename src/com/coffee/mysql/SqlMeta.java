package com.coffee.mysql;

import java.sql.ResultSetMetaData;
import java.util.List;

public class SqlMeta
{
	public int index;
	public String label;
	public int type; // java.sql.Types
	public String typeName;
	
	// 读取每一列的 MetaData
	public static SqlMeta[] read(ResultSetMetaData rsmd) throws Exception
	{
		int numColumn = rsmd.getColumnCount();
		SqlMeta[] result = new SqlMeta[numColumn];		
		for(int i=0; i<numColumn ; i++)
		{
			SqlMeta m = result[i] = new SqlMeta();
			
			int column = i + 1;
			m.index = column;
			m.label = rsmd.getColumnLabel( column);
			m.type = rsmd.getColumnType( column );
			m.typeName = rsmd.getColumnName(column);
		}
		
		return result;		
	}
}
