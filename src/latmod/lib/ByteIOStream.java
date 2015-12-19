package latmod.lib;
import java.io.*;
import java.util.UUID;

/** Made by LatvianModder */
public final class ByteIOStream
{
	private byte[] bytes;
	private int pos;
	
	private byte[] utf_bytes = null;
	private char[] utf_chars = null;
	
	private static void throwUTFException(String s)
	{
		try { throw new UTFDataFormatException(s); }
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public ByteIOStream(int size)
	{ bytes = new byte[size]; }
	
	public ByteIOStream()
	{ this(16); }
	
	public boolean hasData()
	{ return pos > 0; }
	
	public byte[] toByteArray()
	{
		if(pos == bytes.length) return bytes;
		byte[] b = new byte[pos];
		System.arraycopy(bytes, 0, b, 0, pos);
		return b;
	}
	
	public byte[] toCompressedByteArray()
	{
		try { return ByteCompressor.compress(bytes, 0, pos); }
		catch(Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public void expand(int c)
	{
		if(pos + c >= bytes.length)
		{
			byte[] b = new byte[bytes.length + Math.max(c, 16)];
			System.arraycopy(bytes, 0, b, 0, pos);
			bytes = b;
		}
	}
	
	public void flip()
	{ pos = 0; }
	
	public void setData(byte[] b)
	{ bytes = b; flip(); }
	
	public void setCompressedData(byte[] b)
	{
		try { setData(ByteCompressor.decompress(b, 0, b.length)); }
		catch(Exception e) { e.printStackTrace(); setData(null); }
	}
	
	public String toString()
	{ return toString(false); }
	
	public String toString(boolean compressed)
	{
		byte[] b = compressed ? toCompressedByteArray() : toByteArray();
		StringBuilder sb = new StringBuilder();
		sb.append("[ (");
		sb.append(b.length);
		sb.append(") ");
		sb.append(LMStringUtils.stripB(b));
		sb.append(" ]");
		return sb.toString();
	}
	
	public OutputStream createOutputStream()
	{
		OutputStream os = new OutputStream()
		{
			public void write(int b) throws IOException
			{ writeUByte(b); }
			
			public void write(byte b[], int off, int len) throws IOException
			{ writeRawBytes(b, off, len); }
		};
		
		return os;
	}
	
	public InputStream createInputStream()
	{
		InputStream is = new InputStream()
		{
			public int read() throws IOException
			{ return readUByte(); }
			
			public int read(byte b[], int off, int len) throws IOException
			{ return readRawBytes(b, off, len); }
			
			public int available()
			{ return bytes.length - pos; }
		};
		
		return is;
	}
	
	// Read functions //
	
	public byte readByte()
	{
		byte b = bytes[pos];
		pos++;
		return b;
	}
	
	public int readRawBytes(byte[] b, int off, int len)
	{
		if(b == null || len == 0) return 0;
		System.arraycopy(bytes, pos, b, off, len);
		pos += len;
		return len;
	}
	
	public int readUByte()
	{ return readByte() & 0xFF; }
	
	public int readRawBytes(byte[] b)
	{ return readRawBytes(b, 0, b.length); }
	
	public byte[] readByteArray(ByteCount c)
	{
		int s = c.read(this);
		if(s == -1) return null;
		byte[] b = new byte[s];
		readRawBytes(b);
		return b;
	}
	
	public boolean readBoolean()
	{ return readUByte() == 1; }
	
	public String readString()
	{
		int l = readShort();
		
		if(l == -1) return null;
		l = l & 0xFFFF;
		
		if(utf_bytes == null || utf_bytes.length < l)
			utf_bytes = new byte[l * 2];
		
		if(utf_chars == null || utf_chars.length < l)
			utf_chars = new char[l * 2];
		
		int c, c2, c3, c1 = 0, cac = 0;
		
		readRawBytes(utf_bytes, 0, l);
		
		while(c1 < l)
		{
			c = (int) utf_bytes[c1] & 0xFF;
			if(c > 127) break;
			c1++;
			utf_chars[cac++] = (char)c;
		}
		
		while(c1 < l)
		{
			c = (int) utf_bytes[c1] & 0xFF;
			
			switch(c >> 4)
			{
				case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
					c1++;
					utf_chars[cac++]=(char)c;
					break;
				case 12: case 13:
					c1 += 2;
					if (c1 > l)
						throwUTFException("malformed input: partial character at end");
					c2 = (int) utf_bytes[c1 - 1];
					if ((c2 & 0xC0) != 0x80)
						throwUTFException("malformed input around byte " + c1);
					utf_chars[cac++]=(char)(((c & 0x1F) << 6) | (c2 & 0x3F));
					break;
				case 14:
					c1 += 3;
					if (c1 > l)
						throwUTFException("malformed input: partial character at end");
					c2 = (int) utf_bytes[c1 - 2];
					c3 = (int) utf_bytes[c1 - 1];
					if (((c2 & 0xC0) != 0x80) || ((c3 & 0xC0) != 0x80))
						throwUTFException("malformed input around byte " + (c1 - 1));
					utf_chars[cac++] = (char)(((c & 0x0F) << 12) | ((c2 & 0x3F) << 6) | ((c3 & 0x3F) << 0));
					break;
				default:
					throwUTFException("malformed input around byte " + c1);
			}
		}
		
		return new String(utf_chars, 0, cac);
	}
	
	public int readUShort()
	{
		int v = Bits.toUShort(bytes, pos);
		pos += 2;
		return v;
	}
	
	public short readShort()
	{ return (short)readUShort(); }
	
	public int readInt()
	{
		int v = Bits.toInt(bytes, pos);
		pos += 4;
		return v;
	}
	
	public long readLong()
	{
		long v = Bits.toLong(bytes, pos);
		pos += 8;
		return v;
	}
	
	public float readFloat()
	{ return Float.intBitsToFloat(readInt()); }
	
	public double readDouble()
	{ return Double.longBitsToDouble(readLong()); }
	
	public UUID readUUID()
	{
		UUID v = Bits.toUUID(bytes, pos);
		pos += 16;
		return v;
	}
	
	public int[] readIntArray(ByteCount c)
	{
		int len = c.read(this);
		if(len == -1) return null;
		int[] ai = new int[len];
		for(int i = 0; i < len; i++)
			ai[i] = readInt();
		return ai;
	}
	
	// Write functions //
	
	public void writeByte(byte i)
	{
		expand(1);
		bytes[pos] = i;
		pos++;
	}
	
	public void writeRawBytes(byte[] b, int off, int len)
	{
		if(b == null || len == 0) return;
		expand(len);
		System.arraycopy(b, off, bytes, pos, len);
		pos += len;
	}
	
	public void writeUByte(int i)
	{ writeByte((byte)i); }
	
	public void writeRawBytes(byte[] b)
	{ writeRawBytes(b, 0, b.length); }
	
	public void writeByteArray(byte[] b, ByteCount c)
	{
		if(b == null) { c.write(this, -1); return; }
		c.write(this, b.length);
		writeRawBytes(b);
	}
	
	public void writeBoolean(boolean b)
	{ writeUByte(b ? 1 : 0); }
	
	public void writeString(String s)
	{
		if(s == null) { writeShort((short)-1); return; }
		int sl = s.length();
		int l = 0;
		int c, count = 0;
		
		for(int i = 0; i < sl; i++)
		{
			c = s.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) l++;
			else if (c > 0x07FF) l += 3;
			else l += 2;
		}
		
		if(l > 65535) throwUTFException("encoded string too long: " + l + " bytes");
		
		if(utf_bytes == null || (utf_bytes.length < l))
			utf_bytes = new byte[l * 2];
		
		writeUShort(l);
		
		int i = 0;
		for(i = 0; i < sl; i++)
		{
		   c = s.charAt(i);
		   if (!(c >= 0x0001 && c <= 0x007F)) break;
		   utf_bytes[count++] = (byte) c;
		}
		
		for(;i < sl; i++)
		{
			c = s.charAt(i);
			if (c >= 0x0001 && c <= 0x007F)
				utf_bytes[count++] = (byte) c;
			else if(c > 0x07FF)
			{
				utf_bytes[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				utf_bytes[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
				utf_bytes[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
			else
			{
				utf_bytes[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
				utf_bytes[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
		}
		
		writeRawBytes(utf_bytes, 0, l);
	}
	
	public void writeRawString(String s)
	{
		if(s == null || s.isEmpty()) return;
		for(int i = 0; i < s.length(); i++)
			writeByte((byte)s.charAt(i));
	}
	
	public void writeUShort(int s)
	{
		expand(2);
		Bits.fromUShort(bytes, pos, s);
		pos += 2;
	}
	
	public void writeShort(short s)
	{ writeUShort(s & 0xFFFF); }
	
	public void writeInt(int i)
	{
		expand(4);
		Bits.fromInt(bytes, pos, i);
		pos += 4;
	}
	
	public void writeLong(long l)
	{
		expand(8);
		Bits.fromLong(bytes, pos, l);
		pos += 8;
	}
	
	public void writeFloat(float f)
	{ writeInt(Float.floatToIntBits(f)); }
	
	public void writeDouble(double d)
	{ writeLong(Double.doubleToLongBits(d)); }
	
	public void writeUUID(UUID uuid)
	{
		expand(16);
		Bits.fromUUID(bytes, pos, uuid);
		pos += 16;
	}
	
	public void writeIntArray(int[] ai, ByteCount c)
	{
		int asize = (ai == null) ? -1 : ai.length;
		c.write(this, asize);
		for(int i = 0; i < asize; i++)
			writeInt(ai[i]);
	}

	public static int stringLength(String data)
	{
		int len = 2;
		
		if(data != null && !data.isEmpty())
		{
		}
		
		return len;
	}
}