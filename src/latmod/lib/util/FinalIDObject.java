package latmod.lib.util;

public class FinalIDObject implements Comparable<FinalIDObject>
{
	public final String ID;
	
	public FinalIDObject(String id)
	{ ID = id; }
	
	public final String toString()
	{ return ID; }
	
	public final int hashCode()
	{ return ID.hashCode(); }
	
	public final boolean equals(Object o)
	{ return o != null && (o == this || o == ID || ID.equals(o.toString())); }
	
	public int compareTo(FinalIDObject o)
	{ return ID.compareToIgnoreCase(o.ID); }
}