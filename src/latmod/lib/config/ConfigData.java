package latmod.lib.config;

import latmod.lib.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public class ConfigData
{
	public String[] info;
	public Boolean sync;
	public Double min;
	public Double max;
	
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
					if(a.getClass() == Info.class)
					{
						data.info = ((Info) a).value();
					}
					else if(a.getClass() == Sync.class)
					{
						data.sync = Boolean.TRUE;
					}
					else if(a.getClass() == MinValue.class)
					{
						data.min = ((MinValue) a).value();
					}
					else if(a.getClass() == MaxValue.class)
					{
						data.max = ((MaxValue) a).value();
					}
				}
				
				container.setConfigData(data);
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
