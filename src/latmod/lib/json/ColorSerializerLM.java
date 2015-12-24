package latmod.lib.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import latmod.lib.LMColorUtils;

import java.awt.*;
import java.io.IOException;

public class ColorSerializerLM extends TypeAdapter<Color>
{
	public void write(JsonWriter out, Color value) throws IOException
	{
		if(value == null) out.nullValue();
		else out.value('#' + Integer.toHexString(value.getRGB()).toUpperCase());
	}
	
	public Color read(JsonReader in) throws IOException
	{
		if(in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
		int col = Integer.decode(in.nextString());
		return new Color(LMColorUtils.getRed(col), LMColorUtils.getGreen(col), LMColorUtils.getBlue(col), LMColorUtils.getAlpha(col));
	}
}