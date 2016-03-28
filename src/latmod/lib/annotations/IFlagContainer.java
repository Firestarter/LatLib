package latmod.lib.annotations;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public interface IFlagContainer extends IAnnotationContainer
{
	void setFlag(byte id, boolean flag);
	boolean getFlag(byte id);
}
