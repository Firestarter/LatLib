package latmod.lib.util;

import latmod.lib.*;

public class Pos2D
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
	
	@Override
	public int hashCode()
	{ return LMUtils.hashCode(x, y); }
	
	public boolean equalsPos(Pos2D o)
	{ return o.x == x && o.y == y; }
	
	@Override
	public String toString()
	{ return "[" + LMStringUtils.formatDouble(x) + ',' + ' ' + LMStringUtils.formatDouble(y) + ']'; }
	
	@Override
	public boolean equals(Object o)
	{ return o != null && (o == this || equalsPos((Pos2D) o)); }
	
	public Pos2D copy()
	{ return new Pos2D(x, y); }
}