package latmod.lib.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class AnnotationHelper
{
	public static void inject(Field field, Object parent, Object obj) throws Exception
	{
		if(field == null || !(obj instanceof IAnnotationContainer)) return;
		
		for(Annotation a : field.getDeclaredAnnotations())
		{
			Class<?> c = a.annotationType();
			
			if(c == Info.class)
			{
				if(obj instanceof IInfoContainer)
				{
					String[] info = ((Info) a).value();
					if(info != null && info.length == 0) info = null;
					((IInfoContainer) obj).setInfo(info);
					
					if(obj instanceof IFlagContainer)
					{
						((IFlagContainer) obj).setFlag(Flag.HAS_INFO, info != null);
					}
				}
			}
			else if(c == NumberBounds.class)
			{
				if(obj instanceof INumberBoundsContainer)
				{
					NumberBounds b = (NumberBounds) a;
					((INumberBoundsContainer) obj).setBounds(b.min(), b.max());
				}
			}
			else if(c == Flags.class)
			{
				if(obj instanceof IFlagContainer)
				{
					IFlagContainer fc = (IFlagContainer) obj;
					
					for(Flag f : ((Flags) a).value())
					{
						fc.setFlag(f, true);
					}
				}
			}
		}
	}
}