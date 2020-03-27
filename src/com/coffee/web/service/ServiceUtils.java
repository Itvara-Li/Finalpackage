package com.coffee.web.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;

public class ServiceUtils
{
	/////////////////// 工具方法 //////////////////
	// 从 Stream 中读取数据直到读完
	public static String readAsText(InputStream inStream, String charset, int maxSize) throws IOException
	{
		ByteArrayOutputStream cache = new ByteArrayOutputStream(1024 * 16);
		byte[] data = new byte[1024];

		int numOfWait = 0;
		while (true)
		{
			int n = inStream.read(data); // n: 实际读取的字节数
			if (n < 0)
				break; // 连接已经断开
			if (n == 0)
			{
				if (numOfWait++ >= 3)
					break; // 此种情况不得连续3次
				try
				{
					Thread.sleep(5);
				} catch (Exception e)
				{
				}
				continue;// 数据未完 //
			}
			numOfWait = 0;

			// 缓存起来
			cache.write(data, 0, n);
			if (cache.size() > maxSize) // 上限, 最多读取512K
				break;
		}

		return cache.toString(charset);
	}

	
}
