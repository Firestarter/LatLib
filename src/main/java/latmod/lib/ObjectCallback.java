package latmod.lib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 30.05.2016.
 */
public class ObjectCallback
{
    public interface Handler
    {
        void onCallback(@Nonnull ObjectCallback c);
    }

    public final Object ID;
    public final boolean set;
    public final boolean close;
    public final Object object;

    public ObjectCallback(@Nullable Object id, boolean s, boolean c, @Nullable Object o)
    {
        ID = id;
        set = s;
        close = c;
        object = o;
    }
}
