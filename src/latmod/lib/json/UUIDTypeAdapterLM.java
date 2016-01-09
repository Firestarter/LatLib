package latmod.lib.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;
import latmod.lib.LMStringUtils;

import java.io.IOException;
import java.util.UUID;

public class UUIDTypeAdapterLM extends TypeAdapter<UUID>
{
	public void write(JsonWriter out, UUID value) throws IOException
	{
		if(value == null) out.nullValue();
		else out.value(LMStringUtils.fromUUID(value));
	}
	
	public UUID read(JsonReader in) throws IOException
	{
		if(in.peek() == JsonToken.NULL)
		{
			in.nextNull();
			return null;
		}
		return LMStringUtils.fromString(in.nextString());
	}
}