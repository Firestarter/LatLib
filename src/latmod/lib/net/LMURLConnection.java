package latmod.lib.net;

import latmod.lib.*;

import java.io.OutputStream;
import java.net.*;

public class LMURLConnection
{
	public final RequestMethod type;
	public final String url;
	public final ByteIOStream data;
	
	public LMURLConnection(RequestMethod t, String s)
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