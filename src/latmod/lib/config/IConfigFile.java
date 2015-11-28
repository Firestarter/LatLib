package latmod.lib.config;

public interface IConfigFile
{
	public ConfigList getList();
	public void load();
	public void save();
}