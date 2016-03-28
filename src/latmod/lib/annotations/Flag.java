package latmod.lib.annotations;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public enum Flag
{
	/**
	 * Will be synced with client
	 */
	SYNC(0),
	
	/**
	 * Will be invisible in config gui
	 */
	INVISIBLE(1),
	
	/**
	 * Will be visible in config gui, but uneditable
	 */
	UNEDITABLE(2);
	
	public final byte ID;
	public final String name;
	
	Flag(int b)
	{
		ID = (byte) b;
		name = name().toLowerCase();
	}
}
