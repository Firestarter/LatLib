package latmod.lib.util;

import latmod.lib.IIDObject;
import latmod.lib.LMUtils;

public class FinalIDObject implements IIDObject
{
	private final String ID;
	
	public FinalIDObject(String id)
	{
		if(id == null || id.isEmpty()) { throw new NullPointerException("ID can't be null!"); }
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
}