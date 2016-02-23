package latmod.lib;

/**
 * Created by LatvianModder on 08.01.2016.
 */
public final class LMColor
{
	private int color;
	private int red, green, blue;
	private final float[] hsb = new float[3];
	private String string;
	
	public LMColor() { }
	
	public LMColor(int col)
	{ setRGB(col); }
	
	public void set(LMColor col)
	{
		color = col.color;
		red = col.red;
		green = col.green;
		blue = col.blue;
		hsb[0] = col.hsb[0];
		hsb[1] = col.hsb[1];
		hsb[2] = col.hsb[2];
		string = col.string;
	}
	
	public void setRGB(int col)
	{
		color = 0xFF000000 | col;
		red = LMColorUtils.getRed(color);
		green = LMColorUtils.getGreen(color);
		blue = LMColorUtils.getBlue(color);
		java.awt.Color.RGBtoHSB(red, green, blue, hsb);
		string = LMColorUtils.getHex(color);
	}
	
	public void setRGB(int r, int g, int b)
	{ setRGB(LMColorUtils.getRGBA(r, g, b, 255)); }
	
	public void setHSB(float h, float s, float b)
	{
		hsb[0] = h % 1F;
		if(hsb[0] < 0F) hsb[0] = 1F - hsb[0];
		hsb[1] = MathHelperLM.clampFloat(s, 0F, 1F);
		hsb[2] = MathHelperLM.clampFloat(b, 0F, 1F);
		color = 0xFF000000 | java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		red = LMColorUtils.getRed(color);
		green = LMColorUtils.getGreen(color);
		blue = LMColorUtils.getBlue(color);
		string = LMColorUtils.getHex(color);
	}
	
	public void addHue(float hue)
	{ setHSB(hsb[0] + hue, hsb[1], hsb[2]); }
	
	public int color() { return color; }
	
	public int red() { return red; }
	
	public int green() { return green; }
	
	public int blue() { return blue; }
	
	public float hue() { return hsb[0]; }
	
	public float saturation() { return hsb[1]; }
	
	public float brightness() { return hsb[2]; }
	
	public String toString() { return string; }
	
	public boolean equals(Object o)
	{ return ((LMColor) o).color == color; }
	
	public LMColor copy()
	{
		LMColor col = new LMColor();
		col.set(this);
		return col;
	}
}