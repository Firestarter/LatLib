package latmod.lib.net;

import com.google.gson.JsonElement;
import latmod.lib.LMJsonUtils;
import latmod.lib.LMStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	
	@Override
	public String toString()
	{ return method + "-" + Integer.toString(code); }
	
	public String asString() throws Exception
	{ return LMStringUtils.readString(stream); }
	
	public List<String> asStringList() throws Exception
	{ return LMStringUtils.readStringList(stream); }
	
	public JsonElement asJson() throws Exception
	{ return LMJsonUtils.fromJson(new BufferedReader(new InputStreamReader(stream))); }
	
	public BufferedImage asImage() throws Exception
	{ return ImageIO.read(stream); }
}