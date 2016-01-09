package latmod.lib.util;

import latmod.lib.MathHelperLM;

public class Pos2D implements Cloneable
{
	public double x, y;
	
	public Pos2D() { }
	
	public Pos2D(double px, double py)
	{ set(px, py); }
	
	public void set(double px, double py)
	{
		x = px;
		y = py;
	}
	
	public Pos2I toPos2I()
	{ return new Pos2I((int) x, (int) y); }
	
	public int hashCode()
	{ return Double.hashCode(x) * 31 + Double.hashCode(y); }
	
	public boolean equalsPos(Pos2D o)
	{ return o.x == x && o.y == y; }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(MathHelperLM.formatDouble(x));
		sb.append(',');
		sb.append(' ');
		sb.append(MathHelperLM.formatDouble(y));
		sb.append(']');
		return sb.toString();
	}
	
	public boolean equals(Object o)
	{ return o != null && (o == this || equalsPos((Pos2D) o)); }

	public Pos2D clone()
	{ return new Pos2D(x, y); }
}