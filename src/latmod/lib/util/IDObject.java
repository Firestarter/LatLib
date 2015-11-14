package latmod.lib.util;

public class IDObject implements Comparable<IDObject>
{
	public String ID;
	
	public IDObject(String id)
	{ setID(id); }
	
	public void setID(String id)
	{ ID = id; }
	
	public final String toString()
	{ return ID; }
	
	public final int hashCode()
	{ return ID.hashCode(); }
	
	public final boolean equals(Object o)
	{ return o != null && (o == this || ID == o || ID.equals(o.toString())); }
	
	public int compareTo(IDObject o)
	{ return ID.compareToIgnoreCase(o.ID); }
}