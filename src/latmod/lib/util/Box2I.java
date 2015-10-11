package latmod.lib.util;

public class Box2I
{
	public int posX, posY, width, height;
	
	public Box2I() { }
	
	public Box2I(int x, int y, int w, int h)
	{
		posX = x;
		posY = y;
		width = w;
		height = h;
	}
	
	public void setPos(int x, int y)
	{ posX = x; posY = y; }
	
	public boolean isAt(int x, int y)
	{ return x >= posX && y >= posY && x <= posX + width && y <= posY + height; }
	
	public boolean isColliding(Box2I b)
	{
		if(b.posX + b.width < posX || b.posX > posX + width) return false;
		if(b.posY + b.height < posY || b.posY > posY + height) return false;
		return true;
	}
}