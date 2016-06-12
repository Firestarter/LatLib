package latmod.lib;

/**
 * Created by LatvianModder on 30.05.2016.
 */
public class ObjectCallback
{
    public interface Handler
    {
        void onCallback(ObjectCallback c);
    }

    public final Object ID;
    public final boolean set;
    public final boolean close;
    public final Object object;

    public ObjectCallback(Object id, boolean s, boolean c, Object o)
    {
        ID = id;
        set = s;
        close = c;
        object = o;
    }
}
