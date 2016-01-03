package latmod.lib.config;

import com.google.gson.*;
import latmod.lib.*;

import java.io.*;

public class ConfigEntryColor extends ConfigEntry
{
	public int defValue;
	private int value;
	private boolean alpha;
	
	public ConfigEntryColor(String id, int def, boolean a)
	{
		super(id, PrimitiveType.INT);
		alpha = a;
		set(def);
		defValue = format(def);
	}
	
	private int format(int col)
	{ return alpha ? col : (col | 0xFF000000); }
	
	public void set(int v)
	{ value = format(v); }
	
	public int get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(format(o.getAsInt())); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(DataOutput io) throws Exception
	{ io.writeInt(get()); }

	public void read(DataInput io) throws Exception
	{ set(format(io.readInt())); }

	public void writeExtended(DataOutput io) throws Exception
	{
		write(io);
		io.writeBoolean(alpha);
		io.writeInt(defValue);
	}

	public void readExtended(DataInput io) throws Exception
	{
		read(io);
		alpha = io.readBoolean();
		defValue = io.readInt();
	}
	
	public String getAsString()
	{ return LMColorUtils.getHex(get()); }
	
	public int getAsInt()
	{ return get(); }

	public String getDefValue()
	{ return LMColorUtils.getHex(defValue); }
}