package latmod.lib;

import java.io.*;
import java.net.*;

public class LMURLConnection
{
	public static enum Type
	{
		GET,
		POST,
		HEAD,
		OPTIONS,
		PUT,
		DELETE,
		TRACE
	}
	
	public static class Response
	{
		public final long millis;
		public final int code;
		public final InputStream stream;
		
		public Response(long ms, int c, InputStream s)
		{
			millis = ms;
			code = c;
			stream = s;
		}
		
		public String asString() throws Exception
		{ return LMStringUtils.readString(stream); }
	}
	
	public static class LinkBuilder
	{
		private final StringBuilder string;
		private boolean first = true;
		
		public LinkBuilder(String init)
		{
			string = new StringBuilder();
			if(init != null && !init.isEmpty())
			{
				string.append(init);
				string.append('?');
			}
		}
		
		public LinkBuilder append(String key, Object val)
		{
			if(first) first = false;
			else string.append('&');
			string.append(key);
			string.append('=');
			string.append(val);
			return this;
		}
		
		public String toString()
		{ return string.toString(); }
	}
	
	public final Type type;
	public final String url;
	public final ByteIOStream data;
	
	public LMURLConnection(Type t, String s)
	{
		type = t;
		url = s;
		data = new ByteIOStream();
	}
	
	public Response connect() throws Exception
	{
		long startTime = LMUtils.millis();
		
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod(type.name());
		con.setRequestProperty("User-Agent", "HTTP/1.1");
		con.setDoInput(true);
		
		if(data.hasData())
		{
			//System.out.println("Sending '" + con.getRequestMethod() + "' data '" + new String(data.toByteArray()) + "'");
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(data.toByteArray());
			os.flush();
			os.close();
		}
		
		int responseCode = con.getResponseCode();
		return new Response(LMUtils.millis() - startTime, responseCode, con.getInputStream());
	}
}