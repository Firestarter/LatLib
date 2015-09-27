package latmod.core.util;
import java.io.*;

/** Made by LatvianModder <br>
 * DataInputStream + DataOutputStream */
public class DataIOStream extends ByteIOStream
{
	/** Input stream */
	private InputStream input;
	
	/** Output stream */
	private OutputStream output;
	
	/** Instance for reading and writing and with optional compression level */
	public DataIOStream(InputStream is, OutputStream os) throws Exception
	{
		input = (is == null) ? null : new BufferedInputStream(is);
		output = (os == null) ? null : new BufferedOutputStream(os);
	}
	
	/** Flushes output stream */
	public void flush() throws IOException
	{
		if(output != null)
		{
			output.write(getRawBytes(), 0, size());
			output.flush();
		}
	}
	
	/** Stops streams */
	public void close() throws IOException
	{
		super.close();
		if(input != null) input.close();
		if(output != null) output.close();
	}
	
	public boolean isClosed()
	{ return super.isClosed() && input == null && output == null; }
	
	/** @return Bytes available to read */
	public int available() throws Exception
	{ return input.available(); }
	
	public void setData(byte[] b) { }
	
	// Read functions //
	
	public int readUByte() throws Exception
	{ return input.read(); }
	
	public int readRawBytes(byte[] b, int off, int len) throws Exception
	{ return input.read(b, off, len); }
	
	public byte readByte() throws Exception
	{ return (byte)readUByte(); }
}