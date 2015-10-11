package latmod.lib;

public class LMColorUtils
{
	public static final int[] chatFormattingColors = new int[16];
	private static final float[] staticHSB = new float[3];
	
	public static final int BLACK = 0xFF000000;
	public static final int WHITE = 0xFFFFFFFF;
	public static final int LIGHT_GRAY = 0xFFAAAAAA;
	public static final int DARK_GRAY = 0xFF333333;
	public static final int WIDGETS = 0xFF000000;
	
	static
	{
		for(int i = 0; i < 16; i++)
        {
            int j = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + j;
            int g = (i >> 1 & 1) * 170 + j;
            int b = (i >> 0 & 1) * 170 + j;
            if(i == 6) r += 85;
            chatFormattingColors[i] = getRGBA(r, g, b, 255);
        }
	}
	
	public static int getRGBA(int r, int g, int b, int a)
	{ return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | ((b & 255) << 0); }
	
	public static int getRed(int c)
	{ return (c >> 16) & 255; }
	
	public static int getGreen(int c)
	{ return (c >> 8) & 255; }
	
	public static int getBlue(int c)
	{ return (c >> 0) & 255; }
	
	public static int getAlpha(int c)
	{ return (c >> 24) & 255; }
	
	public static String getHex(int c)
	{ return '#' + Integer.toHexString(getRGBA(c, 255)).substring(2).toUpperCase(); }
	
	public static int getRGBA(int c, int a)
	{ return getRGBA(getRed(c), getGreen(c), getBlue(c), a); }
	
	public static int getHSB(float h, float s, float b)
	{ return java.awt.Color.HSBtoRGB(h, s, b); }
	
	public static void setHSB(int r, int g, int b)
	{ java.awt.Color.RGBtoHSB(r, g, b, staticHSB); }
	
	public static void setHSB(int c)
	{ setHSB(getRed(c), getGreen(c), getBlue(c)); }
	
	public static float getHSBHue()
	{ return staticHSB[0]; }
	
	public static float getHSBSaturation()
	{ return staticHSB[1]; }
	
	public static float getHSBBrightness()
	{ return staticHSB[2]; }
	
	public static int addBrightness(int c, int b)
	{
		int red = MathHelperLM.clampInt(getRed(c) + b, 0, 255);
		int green = MathHelperLM.clampInt(getGreen(c) + b, 0, 255);
		int blue = MathHelperLM.clampInt(getBlue(c) + b, 0, 255);
		return getRGBA(red, green, blue, getAlpha(c));
	}
	
	public static void addHue(int pixels[], float f)
	{
		if(pixels == null || pixels.length == 0) return;
		for(int i = 0; i < pixels.length; i++)
		{
			setHSB(pixels[i]);
			pixels[i] = getRGBA(getHSB(staticHSB[0] + f, staticHSB[1], staticHSB[2]), getAlpha(pixels[i]));
		}
	}
	
	public static int lerp(int col1, int col2, double m, int alpha)
	{
		m = MathHelperLM.clamp(m, 0F, 1F);
		int r = MathHelperLM.lerpInt(getRed(col1), getRed(col2), m);
		int g = MathHelperLM.lerpInt(getGreen(col1), getGreen(col2), m);
		int b = MathHelperLM.lerpInt(getBlue(col1), getBlue(col2), m);
		return getRGBA(r, g, b, alpha);
	}
	
	public static int lerp(int col1, int col2, double m)
	{ return lerp(col1, col2, m, getAlpha(col1)); }
}