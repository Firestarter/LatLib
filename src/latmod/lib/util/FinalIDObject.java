package latmod.lib.util;

import latmod.lib.LMUtils;

public class FinalIDObject implements Comparable<Object> // IDObject
{
	public final String ID;
	
	public FinalIDObject(String id)
	{ ID = id; }
	
	public String toString()
	{ return ID; }
	
	public final int hashCode()
	{ return ID.hashCode(); }
	
	public final boolean equals(Object o)
	{ return o != null && (o == this || o == ID || ID.equals(LMUtils.getID(o))); }
	
	public int compareTo(Object o)
	{ return ID.compareToIgnoreCase(LMUtils.getID(o)); }
}