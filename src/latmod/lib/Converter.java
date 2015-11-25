package latmod.lib;

import java.util.Arrays;

/** Made by LatvianModder */
public class Converter
{
	public static int[] toInts(byte[] b)
	{
		if(b == null) return null;
		int ai[] = new int[b.length];
		for(int i = 0; i < ai.length; i++)
		ai[i] = b[i] & 0xFF; return ai;
	}
	
	public static int[] toInts(short[] b)
	{
		if(b == null) return null;
		int ai[] = new int[b.length];
		for(int i = 0; i < ai.length; i++)
		ai[i] = b[i]; return ai;
	}
	
	public static byte[] toBytes(int[] b)
	{
		if(b == null) return null;
		byte ai[] = new byte[b.length];
		for(int i = 0; i < ai.length; i++)
		ai[i] = (byte)b[i]; return ai;
	}
	
	public static Integer[] fromInts(int[] i)
	{
		if(i == null) return null;
		Integer ai[] = new Integer[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = Integer.valueOf(i[j]); return ai;
	}
	
	public static int[] toInts(Integer[] i)
	{
		if(i == null) return null;
		int ai[] = new int[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = i[j].intValue(); return ai;
	}
	
	public static Float[] fromFloats(float[] i)
	{
		if(i == null) return null;
		Float ai[] = new Float[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = Float.valueOf(i[j]); return ai;
	}
	
	public static float[] toFloats(Float[] i)
	{
		if(i == null) return null;
		float ai[] = new float[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = i[j].floatValue(); return ai;
	}
	
	public static Double[] fromDoubles(double[] i)
	{
		if(i == null) return null;
		Double ai[] = new Double[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = Double.valueOf(i[j]); return ai;
	}
	
	public static double[] toDoubles(Double[] i)
	{
		if(i == null) return null;
		double ai[] = new double[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = i[j].doubleValue(); return ai;
	}
	
	public static Byte[] fromBytes(byte[] i)
	{
		if(i == null) return null;
		Byte ai[] = new Byte[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = Byte.valueOf(i[j]); return ai;
	}
	
	public static byte[] toBytes(Byte[] i)
	{
		if(i == null) return null;
		byte ai[] = new byte[i.length];
		for(int j = 0; j < ai.length; j++)
		ai[j] = i[j].byteValue(); return ai;
	}
	
	public static void toBools(boolean[] bools, IntList idx, boolean isTrue)
	{
		Arrays.fill(bools, !isTrue);
		for(int i = 0; i < idx.size(); i++)
			bools[idx.get(i)] = isTrue;
	}
	
	public static void fromBools(boolean[] bools, IntList il, boolean isTrue)
	{
		il.clear();
		for(int i = 0; i < bools.length; i++)
			if(bools[i] == isTrue) il.add(i);
	}
	
	public static int decodeInt(String s)
	{
		Integer i = decode(s);
		return (i == null) ? 0 : i.intValue();
	}
	
	public static Integer decode(String s)
	{
		try { Integer i = Integer.decode(s); return i; }
		catch(Exception e) { } return null;
	}
	
	public static boolean canParseInt(String s)
	{
		try { Integer.parseInt(s); return true; }
		catch(Exception e) { } return false;
	}
	
	public static boolean canParseDouble(String s)
	{
		try { Double.parseDouble(s); return true; }
		catch(Exception e) { } return false;
	}
	
	public static Integer toInt(String text)
	{
		try { int i = Integer.parseInt(text); return i; }
		catch(Exception e) { } return null;
	}
	
	public static int toInt(String text, int def)
	{
		try { int i = Integer.parseInt(text); return i; }
		catch(Exception e) { } return def;
	}
	
	public static Float toFloat(String text)
	{
		try { float f = Float.parseFloat(text); return f; }
		catch(Exception e) { } return null;
	}
	
	public static float toFloat(String text, float def)
	{
		try { float f = Float.parseFloat(text); return f; }
		catch(Exception e) { } return def;
	}
}