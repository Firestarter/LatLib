package latmod.lib.util;

import latmod.lib.MathHelperLM;

public final class DoubleBounds
{
	public final double defValue, minValue, maxValue;
	
	public DoubleBounds(double def, double min, double max)
	{
		defValue = MathHelperLM.clamp(def, min, max);
		minValue = min;
		maxValue = max;
	}
	
	public DoubleBounds(double def)
	{ this(def, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); }
	
	public double getVal(double v)
	{ return MathHelperLM.clamp(v, minValue, maxValue); }
}