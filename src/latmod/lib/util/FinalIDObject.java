package latmod.lib.util;

import latmod.lib.*;

public class FinalIDObject implements IIDObject, Comparable<Object> // IDObject
{
	private final String ID;
	
	public FinalIDObject(String id)
	{
		if(id == null || id.isEmpty()) throw new NullPointerException("ID can't be null!");
		ID = id;
	}
	
	@Override
	public final String getID()
	{ return ID; }
	
	@Override
	public String toString()
	{ return ID; }
	
	@Override
	public final int hashCode()
	{ return ID.hashCode(); }
	
	@Override
	public final boolean equals(Object o)
	{ return o != null && (o == this || o == ID || ID.equals(LMUtils.getID(o))); }
	
	@Override
	public int compareTo(Object o)
	{ return ID.compareToIgnoreCase(LMUtils.getID(o)); }
}