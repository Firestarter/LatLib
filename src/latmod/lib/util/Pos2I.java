package latmod.lib.util;

public class Pos2I implements Cloneable
{
	public int x, y;
	
	public Pos2I() { }
	
	public Pos2I(int px, int py)
	{ set(px, py); }
	
	public void set(int px, int py)
	{ x = px; y = py; }
	
	public Pos2D toPos2D()
	{ return new Pos2D(x, y); }
	
	public int hashCode()
	{ return x * 31 + y; }
	
	public boolean equalsPos(Pos2I o)
	{ return o.x == x && o.y == y; }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(x);
		sb.append(',');
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}
	
	public boolean equals(Object o)
	{ return o != null && (o == this || equalsPos((Pos2I)o)); }

	public Pos2I clone()
	{ return new Pos2I(x, y); }
}