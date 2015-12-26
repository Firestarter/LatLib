package latmod.lib.net;

import latmod.lib.FastMap;

import java.util.Map;

public class LinkBuilder
{
	private final StringBuilder base;
	private final FastMap<String, Object> args;

	public LinkBuilder(String init)
	{
		base = new StringBuilder(init);
		args = new FastMap<>();
	}

	public LinkBuilder append(String s)
	{ base.append(s); return this; }

	public LinkBuilder put(String s, Object o)
	{ args.put(s, o); return this; }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder(base);

		if(!args.isEmpty())
		{
			boolean first = true;

			for(Map.Entry<String, Object> e : args.entrySet())
			{
				sb.append(first ? '?' : '&');
				sb.append(e.getKey());
				sb.append('=');
				sb.append(e.getValue());
				first = false;
			}
		}

		return sb.toString();
	}
}