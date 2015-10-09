package latmod.core.util;

import java.io.*;
import java.util.zip.*;

public class ByteCompressor
{
	public static byte[] compress(byte[] data) throws Exception
	{  
		Deflater d = new Deflater();
		d.setInput(data);
		ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);   
		d.finish();  
		
		byte[] buffer = new byte[1024];
		while(!d.finished())
		{  
			int count = d.deflate(buffer);
			os.write(buffer, 0, count);
		}
		
		os.close();
		byte[] output = os.toByteArray();
		d.end();
		return output;
	}
	
	public static byte[] decompress(byte[] data) throws Exception
	{
		Inflater i = new Inflater();
		i.setInput(data);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);  
		byte[] buffer = new byte[1024];
		while (!i.finished())
		{
			int count = i.inflate(buffer);
			os.write(buffer, 0, count);
		}
		
		os.close();
		byte[] output = os.toByteArray();
		i.end();
		return output;
	}
}