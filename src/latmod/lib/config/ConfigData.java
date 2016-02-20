package latmod.lib.config;

import latmod.lib.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public final class ConfigData
{
	private Boolean isHidden;
	private Boolean isExcluded;
	private Boolean sync;
	private Boolean canEdit;
	private Boolean canAdd;
	public String[] info;
	private Double min;
	private Double max;
	
	public String toString()
	{
		Map<String, Object> data = new HashMap<>();
		if(isHidden != null) data.put("hidden", isHidden);
		if(isExcluded != null) data.put("excluded", isExcluded);
		if(sync != null) data.put("sync", sync);
		if(canEdit != null) data.put("can_edit", canEdit);
		if(canAdd != null) data.put("can_add", canAdd);
		if(info != null && info.length > 0) data.put("info", LMStringUtils.strip(info));
		if(min != null) data.put("min", min);
		if(max != null) data.put("max", max);
		return LMMapUtils.toString(data);
	}
	
	public boolean isHidden()
	{ return isHidden == null ? Hidden.DEF_VALUE : isHidden.booleanValue(); }
	
	public boolean isExcluded()
	{ return isExcluded == null ? Excluded.DEF_VALUE : isExcluded.booleanValue(); }
	
	public boolean sync()
	{ return sync == null ? Sync.DEF_VALUE : sync.booleanValue(); }
	
	public boolean canEdit()
	{ return canEdit == null ? CanEdit.DEF_VALUE : canEdit.booleanValue(); }
	
	public boolean canAdd()
	{ return canAdd == null ? CanAdd.DEF_VALUE : canAdd.booleanValue(); }
	
	public double min()
	{ return min == null ? Double.NEGATIVE_INFINITY : min; }
	
	public double max()
	{ return max == null ? Double.POSITIVE_INFINITY : max; }
	
	public double getDouble(double v)
	{ return MathHelperLM.clamp(v, min(), max()); }
	
	public void setDefaults()
	{
		isHidden = null;
		isExcluded = null;
		sync = null;
		canEdit = null;
		canAdd = null;
		info = null;
		min = null;
		max = null;
	}
	
	public byte getFlags()
	{
		byte flags = 0;
		flags = Bits.setBit(flags, (byte) 0, isHidden());
		flags = Bits.setBit(flags, (byte) 1, isExcluded());
		flags = Bits.setBit(flags, (byte) 2, sync());
		flags = Bits.setBit(flags, (byte) 3, canEdit());
		flags = Bits.setBit(flags, (byte) 4, canAdd());
		return flags;
	}
	
	public void setFlags(byte flags)
	{
		isHidden = Bits.getBit(flags, (byte) 0);
		isExcluded = Bits.getBit(flags, (byte) 1);
		sync = Bits.getBit(flags, (byte) 2);
		canEdit = Bits.getBit(flags, (byte) 3);
		canAdd = Bits.getBit(flags, (byte) 4);
	}
	
	public void setFrom(ConfigData from)
	{
		if(from == null) return;
		isHidden = from.isHidden;
		isExcluded = from.isExcluded;
		sync = from.sync;
		canEdit = from.canEdit;
		canAdd = from.canAdd;
		info = from.info;
		min = from.min;
		max = from.max;
	}
	
	public void write(ByteIOStream io)
	{
		io.writeByte(getFlags());
		
		byte flags2 = 0;
		flags2 = Bits.setBit(flags2, (byte) 0, info != null && info.length > 0);
		flags2 = Bits.setBit(flags2, (byte) 1, min != null);
		flags2 = Bits.setBit(flags2, (byte) 2, max != null);
		io.writeByte(flags2);
		
		if(info != null && info.length > 0)
		{
			io.writeByte(info.length);
			for(int i = 0; i < info.length; i++)
				io.writeUTF(info[i]);
		}
		
		if(min != null) io.writeDouble(min.doubleValue());
		if(max != null) io.writeDouble(max.doubleValue());
	}
	
	public void read(ByteIOStream io)
	{
		setDefaults();
		setFlags(io.readByte());
		byte flags2 = io.readByte();
		
		if(Bits.getBit(flags2, (byte) 0))
		{
			info = new String[io.readUnsignedByte()];
			for(int i = 0; i < info.length; i++)
				info[i] = io.readUTF();
		}
		
		if(Bits.getBit(flags2, (byte) 1)) min = io.readDouble();
		if(Bits.getBit(flags2, (byte) 2)) max = io.readDouble();
	}
	
	public interface Container
	{
		void setConfigData(ConfigData d);
	}
	
	public static void injectAll(Class<?> c)
	{
		try
		{
			for(Field f : c.getDeclaredFields())
			{
				inject(f, null, null);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static Map.Entry<Object, ConfigData> inject(Field field, Object parent, Container container)
	{
		if(field == null) return null;
		
		try
		{
			field.setAccessible(true);
			Object obj = field.get(parent);
			
			if(obj != null)
			{
				if(container == null && obj instanceof Container) container = (Container) obj;
				if(container == null) return null;
				
				Annotation[] annotations = field.getDeclaredAnnotations();
				if(annotations.length == 0) return null;
				
				ConfigData data = new ConfigData();
				
				for(Annotation a : annotations)
				{
					if(a instanceof Info)
					{
						data.info = ((Info) a).value();
					}
					else if(a instanceof Hidden)
					{
						data.isHidden = ((Hidden) a).value();
					}
					else if(a instanceof Excluded)
					{
						data.isExcluded = ((Excluded) a).value();
					}
					else if(a instanceof Sync)
					{
						data.sync = ((Sync) a).value();
					}
					else if(a instanceof CanEdit)
					{
						data.canEdit = ((CanEdit) a).value();
					}
					else if(a instanceof CanAdd)
					{
						data.canAdd = ((CanAdd) a).value();
					}
					else if(a instanceof MinValue)
					{
						data.min = ((MinValue) a).value();
					}
					else if(a instanceof MaxValue)
					{
						data.max = ((MaxValue) a).value();
					}
				}
				
				container.setConfigData(data);
				System.out.println("Injected ConfigData into " + container.getClass().getName() + ", " + obj + ": " + data);
				return new MapEntry<>(obj, data);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
}
