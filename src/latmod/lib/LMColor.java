package latmod.lib;

/**
 * Created by LatvianModder on 08.01.2016.
 */
public final class LMColor implements Cloneable
{
	private int color;
	private int red, green, blue;
	private float hue, saturation, brightness;
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
		hue = col.hue;
		saturation = col.saturation;
		brightness = col.brightness;
		string = col.string;
	}

	public void setRGB(int col)
	{
		color = 0xFF000000 | col;
		red = LMColorUtils.getRed(color);
		green = LMColorUtils.getGreen(color);
		blue = LMColorUtils.getBlue(color);
		LMColorUtils.setHSB(color);
		hue = LMColorUtils.getHSBHue();
		saturation = LMColorUtils.getHSBSaturation();
		brightness = LMColorUtils.getHSBBrightness();
		string = LMColorUtils.getHex(color);
	}

	public void setRGB(int r, int g, int b)
	{ setRGB(LMColorUtils.getRGBA(r, g, b, 255)); }

	public void setHSB(float h, float s, float b)
	{
		hue = MathHelperLM.clampFloat(h, 0F, 1F);
		saturation = MathHelperLM.clampFloat(s, 0F, 1F);
		brightness = MathHelperLM.clampFloat(b, 0F, 1F);
		color = 0xFF000000 | LMColorUtils.getHSB(hue, saturation, brightness);
		red = LMColorUtils.getRed(color);
		green = LMColorUtils.getGreen(color);
		blue = LMColorUtils.getBlue(color);
		string = LMColorUtils.getHex(color);
	}

	public int color() { return color; }

	public int red() { return red; }

	public int green() { return green; }

	public int blue() { return blue; }

	public float hue() { return hue; }

	public float saturation() { return saturation; }

	public float brightness() { return brightness; }

	public String toString() { return string; }

	public boolean equals(Object o)
	{ return ((LMColor) o).color == color; }

	public LMColor clone()
	{
		LMColor col = new LMColor();
		col.set(this);
		return col;
	}
}