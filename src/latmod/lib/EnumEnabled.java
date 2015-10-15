package latmod.lib;

public enum EnumEnabled
{
	ENABLED,
	DISABLED;
	
	public boolean isEnabled()
	{ return this == ENABLED; }
}