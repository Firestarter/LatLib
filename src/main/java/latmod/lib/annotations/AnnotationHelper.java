package latmod.lib.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class AnnotationHelper
{
    private static final Map<Class<? extends Annotation>, Handler> map = new HashMap<>();

    public interface Handler
    {
        boolean onAnnotationDeclared(Annotation a, IAnnotationContainer container);
    }

    static
    {
        register(Info.class, new Handler()
        {
            @Override
            public boolean onAnnotationDeclared(Annotation a, IAnnotationContainer container)
            {
                if(container instanceof IInfoContainer)
                {
                    String[] info = ((Info) a).value();
                    if(info.length == 0)
                    {
                        info = null;
                    }
                    ((IInfoContainer) container).setInfo(info);
                    return true;
                }

                return false;
            }
        });

        register(NumberBounds.class, new Handler()
        {
            @Override
            public boolean onAnnotationDeclared(Annotation a, IAnnotationContainer container)
            {
                if(container instanceof INumberBoundsContainer)
                {
                    NumberBounds b = (NumberBounds) a;
                    ((INumberBoundsContainer) container).setBounds(b.min(), b.max());
                    return true;
                }

                return false;
            }
        });

        register(Flags.class, new Handler()
        {
            @Override
            public boolean onAnnotationDeclared(Annotation a, IAnnotationContainer container)
            {
                if(container instanceof IFlagContainer)
                {
                    IFlagContainer fc = (IFlagContainer) container;

                    for(byte flag : ((Flags) a).value())
                    {
                        fc.setFlag(flag, true);
                    }

                    return true;
                }

                return false;
            }
        });
    }

    public static void register(Class<? extends Annotation> c, Handler h)
    {
        map.put(c, h);
    }

    public static void inject(Field field, Object parent, Object obj) throws Exception
    {
        if(field == null || !(obj instanceof IAnnotationContainer))
        {
            return;
        }
        IAnnotationContainer container = (IAnnotationContainer) obj;

        for(Annotation a : field.getDeclaredAnnotations())
        {
            Handler h = map.get(a.annotationType());
            if(h != null)
            {
                h.onAnnotationDeclared(a, container);
            }
        }
    }
}