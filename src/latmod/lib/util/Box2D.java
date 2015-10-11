package latmod.lib.util;

public class Box2D
{
	public double posX, posY, width, height;
	
	public Box2D() { }
	
	public Box2D(double x, double y, double w, double h)
	{
		posX = x;
		posY = y;
		width = w;
		height = h;
	}
	
	public void setPos(double x, double y)
	{ posX = x; posY = y; }
	
	public boolean isAt(double x, double y)
	{ return x >= posX && y >= posY && x <= posX + width && y <= posY + height; }
	
	public boolean isColliding(Box2D b)
	{
		if(b.posX + b.width < posX || b.posX > posX + width) return false;
		if(b.posY + b.height < posY || b.posY > posY + height) return false;
		return true;
	}
}