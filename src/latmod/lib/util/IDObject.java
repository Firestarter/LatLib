package latmod.lib.util;

public class IDObject implements Comparable<IDObject>
{
	private String ID;
	
	public IDObject(String id)
	{ setID(id); }
	
	public void setID(String id)
	{ ID = id; }
	
	public String toString()
	{ return ID; }
	
	public final int hashCode()
	{ return toString().hashCode(); }
	
	public final boolean equals(Object o)
	{ return o != null && (o == this || toString() == o || toString() == o.toString() || toString().equals(o.toString())); }
	
	public final int compareTo(IDObject o)
	{ return toString().compareToIgnoreCase(o.toString()); }
}