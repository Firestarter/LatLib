package latmod.lib.util;

public class FinalIDObject extends IDObject
{
	private final String ID;
	
	public FinalIDObject(String id)
	{
		super(id);
		ID = id;
	}
	
	public void setID(String s)
	{  }
	
	public final String toString()
	{ return ID; }
}