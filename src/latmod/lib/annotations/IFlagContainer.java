package latmod.lib.annotations;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public interface IFlagContainer extends IAnnotationContainer
{
	void setFlag(IFlag flag, boolean b);
	boolean getFlag(IFlag flag);
}
