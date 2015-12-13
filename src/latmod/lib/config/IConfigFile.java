package latmod.lib.config;

public interface IConfigFile
{
	public ConfigGroup getGroup();
	public void load();
	public void save();
}