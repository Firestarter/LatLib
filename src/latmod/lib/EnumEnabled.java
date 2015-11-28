package latmod.lib;

public enum EnumEnabled
{
	ENABLED,
	DISABLED;
	
	public static final EnumEnabled[] VALUES = values();
	
	public boolean isEnabled()
	{ return this == ENABLED; }
}