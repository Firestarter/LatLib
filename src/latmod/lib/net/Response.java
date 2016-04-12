package latmod.lib.net;

import com.google.gson.JsonElement;
import latmod.lib.*;

import java.io.*;
import java.util.List;

public final class Response
{
	public final RequestMethod method;
	public final long millis;
	public final int code;
	public final InputStream stream;
	
	public Response(RequestMethod m, long ms, int c, InputStream is)
	{
		method = m;
		millis = ms;
		code = c;
		stream = is;
	}
	
	public Response(InputStream is)
	{ this(RequestMethod.SIMPLE_GET, 0L, 200, is); }
	
	public String toString()
	{ return Integer.toString(code); }
	
	public String asString() throws Exception
	{ return LMStringUtils.readString(stream); }
	
	public List<String> asStringList() throws Exception
	{ return LMStringUtils.readStringList(stream); }
	
	public JsonElement asJson() throws Exception
	{ return LMJsonUtils.fromJson(new BufferedReader(new InputStreamReader(stream))); }
}