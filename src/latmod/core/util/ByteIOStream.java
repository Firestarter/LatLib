package latmod.core.util;
import java.io.*;
import java.util.UUID;

/** Made by LatvianModder */
public class ByteIOStream
{
	private byte[] bytes;
	private int pos;
	
	private byte[] utf_bytes = null;
	private char[] utf_chars = null;
	
	public ByteIOStream(byte[] b)
	{ bytes = b; }
	
	public ByteIOStream(int size)
	{ this(new byte[size]); }
	
	public ByteIOStream()
	{ this(16); }
	
	public byte[] toByteArray()
	{
		byte[] b = new byte[pos];
		System.arraycopy(bytes, 0, b, 0, pos);
		return b;
	}
	
	public byte[] getRawBytes()
	{ return bytes; }
	
	public int size()
	{ return pos; }
	
	public void expand(int c)
	{
		if(pos + c >= bytes.length)
		{
			byte[] b = new byte[bytes.length + Math.max(c, 16)];
			System.arraycopy(bytes, 0, b, 0, pos);
			bytes = b;
		}
	}
	
	public void flush() throws IOException
	{ }
	
	public void close() throws IOException
	{ bytes = null; }
	
	public boolean isClosed()
	{ return bytes == null; }
	
	public int available() throws Exception
	{ return getRawBytes().length - size(); }
	
	public void setData(byte[] b)
	{
		bytes = b;
		pos = 0;
	}
	
	public String toString()
	{ return "[ " + LMStringUtils.stripB(toByteArray()) + " ]"; }
	
	// Write functions //
	
	public void writeByte(byte i) throws IOException
	{
		expand(1);
		bytes[pos] = i;
		pos++;
	}
	
	public void writeRawBytes(byte[] b, int off, int len) throws Exception
	{
		if(b == null || len == 0) return;
		expand(len);
		System.arraycopy(b, off, bytes, pos, len);
		pos += len;
	}
	
	public void writeUByte(int i) throws IOException
	{ writeByte((byte)i); }
	
	public void writeRawBytes(byte[] b) throws Exception
	{ writeRawBytes(b, 0, b.length); }
	
	public void writeByteArray(byte[] b) throws Exception
	{
		if(b == null) { writeShort((short)-1); return; }
		writeShort((short)b.length);
		writeRawBytes(b);
	}
	
	public void writeBoolean(boolean b) throws Exception
	{ writeUByte(b ? 1 : 0); }
	
	public void writeString(String s) throws Exception
	{
		if(s == null) writeShort((short)-1);
		else if(s.isEmpty()) writeShort((short)0);
		else
		{
			int l = s.length();
			writeShort((short)l);
			for(int i = 0; i < l; i++)
				writeUByte(s.charAt(i));
		}
		writeByteArray(LMStringUtils.toBytes(s));
	}
	
	public void writeUTF(String s) throws Exception
	{
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
		
		if(l > 65535) throw new UTFDataFormatException("encoded string too long: " + l + " bytes");
		
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
	
	public void writeUShort(int s) throws Exception
	{
		expand(2);
		Bits.fromUShort(bytes, pos, s);
		pos += 2;
	}
	
	public void writeShort(short s) throws Exception
	{ writeUShort(s & 0xFFFF); }
	
	public void writeInt(int i) throws Exception
	{
		expand(4);
		Bits.fromInt(bytes, pos, i);
		pos += 4;
	}
	
	public void writeLong(long l) throws Exception
	{
		expand(8);
		Bits.fromLong(bytes, pos, l);
		pos += 8;
	}
	
	public void writeFloat(float f) throws Exception
	{ writeInt(Float.floatToIntBits(f)); }
	
	public void writeDouble(double d) throws Exception
	{ writeLong(Double.doubleToLongBits(d)); }
	
	public void writeUUID(UUID uuid) throws Exception
	{
		expand(16);
		Bits.fromUUID(bytes, pos, uuid);
		pos += 16;
	}
	
	// Read functions //
	
	public byte readByte() throws Exception
	{
		byte b = bytes[pos];
		pos++;
		return b;
	}
	
	public int readRawBytes(byte[] b, int off, int len) throws Exception
	{
		if(b == null || len == 0) return 0;
		System.arraycopy(bytes, pos, b, off, len);
		pos += len;
		return len;
	}
	
	public int readUByte() throws Exception
	{ return readByte() & 0xFF; }
	
	public int readRawBytes(byte[] b) throws Exception
	{ return readRawBytes(b, 0, b.length); }
	
	public byte[] readByteArray() throws Exception
	{
		short s = readShort();
		if(s == -1) return null;
		byte[] b = new byte[s];
		readRawBytes(b);
		return b;
	}
	
	public boolean readBoolean() throws Exception
	{ return readUByte() == 1; }
	
	public String readString() throws Exception
	{ return LMStringUtils.fromBytes(readByteArray()); }
	
	public String readUTF() throws Exception
	{
		int l = readUShort();
		
		if(utf_bytes == null || utf_bytes.length < l)
		{
			utf_bytes = new byte[l * 2];
			utf_chars = new char[l * 2];
		}
		
		int c, c2, c3, c1 = 0, cac = 0;
		
		readRawBytes(utf_bytes, 0, l);
		
		while(c1 < l)
		{
			c = (int) utf_bytes[c1] & 0xff;
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
						throw new UTFDataFormatException("malformed input: partial character at end");
					c2 = (int) utf_bytes[c1 - 1];
					if ((c2 & 0xC0) != 0x80)
						throw new UTFDataFormatException("malformed input around byte " + c1);
					utf_chars[cac++]=(char)(((c & 0x1F) << 6) | (c2 & 0x3F));
					break;
				case 14:
					c1 += 3;
					if (c1 > l)
						throw new UTFDataFormatException("malformed input: partial character at end");
					c2 = (int) utf_bytes[c1 - 2];
					c3 = (int) utf_bytes[c1 - 1];
					if (((c2 & 0xC0) != 0x80) || ((c3 & 0xC0) != 0x80))
						throw new UTFDataFormatException("malformed input around byte " + (c1 - 1));
					utf_chars[cac++] = (char)(((c & 0x0F) << 12) | ((c2 & 0x3F) << 6) | ((c3 & 0x3F) << 0));
					break;
				default:
					throw new UTFDataFormatException("malformed input around byte " + c1);
			}
		}
		
		return new String(utf_chars, 0, cac);
	}
	
	public int readUShort() throws Exception
	{
		int v = Bits.toUShort(bytes, pos);
		pos += 2;
		return v;
	}
	
	public short readShort() throws Exception
	{ return (short)readUShort(); }
	
	public int readInt() throws Exception
	{
		int v = Bits.toInt(bytes, pos);
		pos += 4;
		return v;
	}
	
	public long readLong() throws Exception
	{
		long v = Bits.toLong(bytes, pos);
		pos += 8;
		return v;
	}
	
	public float readFloat() throws Exception
	{ return Float.intBitsToFloat(readInt()); }
	
	public double readDouble() throws Exception
	{ return Double.longBitsToDouble(readLong()); }
	
	public UUID readUUID() throws Exception
	{
		UUID v = Bits.toUUID(bytes, pos);
		pos += 16;
		return v;
	}
}