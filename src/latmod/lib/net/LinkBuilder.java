package latmod.lib.net;

public class LinkBuilder
{
	private final StringBuilder string;
	private boolean first = true;
	
	public LinkBuilder(String init)
	{
		string = new StringBuilder();
		if(init != null && !init.isEmpty())
		{
			string.append(init);
			string.append('?');
		}
	}
	
	public LinkBuilder append(String key, Object val)
	{
		if(first) first = false;
		else string.append('&');
		string.append(key);
		string.append('=');
		string.append(val);
		return this;
	}
	
	public String toString()
	{ return string.toString(); }
}