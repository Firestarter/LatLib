package latmod.lib.config;

public interface IConfigFile
{
	ConfigGroup getGroup();
	void load();
	void save();
}