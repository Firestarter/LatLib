package latmod.lib;

import java.util.UUID;

/**
 * Made by LatvianModder
 */
public class Bits
{
	private static final int FF = 0xFF;
	private static final int FFFF = 0xFFFF;
	private static final long FFFFFFFF = 0xFFFFFFFFL;
	
	public static int toInt(boolean[] b)
	{
		int d = 0;
		for(int i = 0; i < b.length; i++)
			d |= (b[i] ? 1 : 0) << i;
		return d;
	}
	
	public static void toBool(boolean[] b, int d)
	{
		for(int j = 0; j < b.length; j++)
			b[j] = ((d >> j) & 1) == 1;
	}
	
	public static boolean getBit(byte bits, byte i)
	{ return ((bits >> i) & 1) == 1; }
	
	public static int toBit(boolean b, byte i)
	{ return (b ? 1 : 0) << i; }
	
	public static byte setBit(byte bits, byte i, boolean v)
	{
		if(v) return (byte) ((bits & 0xFF) | (1 << i));
		else return (byte) ((bits & 0xFF) & (not(1 << i) & 0xFF));
	}
	
	/*
	public static void fromBits(boolean b[], byte bits)
	{ for(int i = 0; i < b.length; i++) b[i] = getBit(bits, i); }
	
	public static byte toBits(boolean[] b)
	{
		byte i = 0;
		for(int j = 0; j < b.length; j++) i |= toBit(b[j], j);
		return i;
	}
	
	public static void not(boolean[] from, boolean[] to)
	{
		for(int i = 0; i < from.length; i++)
			to[i] = !from[i];
	}
	*/
	
	public static int not(int bits)
	{ return 255 - (bits & 0xFF); }
	
	//
	
	//Int
	public static long intsToLong(int a, int b)
	{ return (((long) a) << 32) | (b & FFFFFFFF); }
	
	public static int intFromLongA(long l)
	{ return (int) (l >> 32); }
	
	public static int intFromLongB(long l)
	{ return (int) l; }
	
	//Short
	public static int shortsToInt(int a, int b)
	{ return ((short) a << 16) | ((short) b & FFFF); }
	
	public static short shortFromIntA(int i)
	{ return (short) (i >> 16); }
	
	public static short shortFromIntB(int i)
	{ return (short) (i & FFFF); }
	
	//Byte
	public static short bytesToShort(int a, int b)
	{ return (short) (((a & FF) << 8) | (b & FF)); }
	
	public static byte byteFromShortA(short s)
	{ return (byte) ((s >> 8) & FF); }
	
	public static byte byteFromShortB(short s)
	{ return (byte) (s & FF); }
	
	// - //
	
	public static int toUShort(byte[] b, int off)
	{
		int ch1 = b[off] & FF;
		int ch2 = b[off + 1] & FF;
		return (ch1 << 8) + (ch2);
	}
	
	public static int toInt(byte[] b, int off)
	{
		int ch1 = b[off] & FF;
		int ch2 = b[off + 1] & FF;
		int ch3 = b[off + 2] & FF;
		int ch4 = b[off + 3] & FF;
		return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4);
	}
	
	public static long toLong(byte[] b, int off)
	{
		return (((long) b[off] << 56) + ((long) (b[off + 1] & FF) << 48) + ((long) (b[off + 2] & FF) << 40) + ((long) (b[off + 3] & FF) << 32) + ((long) (b[off + 4] & FF) << 24) + ((b[off + 5] & FF) << 16) + ((b[off + 6] & FF) << 8) + ((b[off + 7] & FF)));
	}
	
	public static UUID toUUID(byte[] b, int off)
	{
		long msb = toLong(b, off);
		long lsb = toLong(b, off + 8);
		return new UUID(msb, lsb);
	}
	
	// - //
	
	public static void fromUShort(byte[] b, int off, int v)
	{
		b[off] = (byte) (v >>> 8);
		b[off + 1] = (byte) (v);
	}
	
	public static void fromInt(byte[] b, int off, int v)
	{
		b[off] = (byte) (v >>> 24);
		b[off + 1] = (byte) (v >>> 16);
		b[off + 2] = (byte) (v >>> 8);
		b[off + 3] = (byte) (v);
	}
	
	public static void fromLong(byte[] b, int off, long v)
	{
		b[off] = (byte) (v >>> 56);
		b[off + 1] = (byte) (v >>> 48);
		b[off + 2] = (byte) (v >>> 40);
		b[off + 3] = (byte) (v >>> 32);
		b[off + 4] = (byte) (v >>> 24);
		b[off + 5] = (byte) (v >>> 16);
		b[off + 6] = (byte) (v >>> 8);
		b[off + 7] = (byte) (v);
	}
	
	public static void fromUUID(byte[] b, int off, UUID uuid)
	{
		fromLong(b, off, uuid.getMostSignificantBits());
		fromLong(b, off + 8, uuid.getLeastSignificantBits());
	}
}