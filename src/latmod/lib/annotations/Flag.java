package latmod.lib.annotations;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public enum Flag implements IFlag
{
	/**
	 * Will be synced with client
	 */
	SYNC(0),
	
	/**
	 * Will be hidden from config gui
	 */
	HIDDEN(1),
	
	/**
	 * Will be visible in config gui, but uneditable
	 */
	CANT_EDIT(2),
	
	/**
	 * Can add new config entries
	 */
	CAN_ADD(3),
	
	/**
	 * Will be excluded from writing / reading from files
	 */
	EXCLUDED(4),
	
	/**
	 * Used when info is present
	 */
	HAS_INFO(5);
	
	public final byte ID;
	public final String name;
	
	Flag(int b)
	{
		ID = (byte) b;
		name = name().toLowerCase();
	}
	
	public byte getFlagID()
	{ return ID; }
}
