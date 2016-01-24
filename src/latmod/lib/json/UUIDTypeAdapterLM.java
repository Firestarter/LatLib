package latmod.lib.json;

import java.util.UUID;

public class UUIDTypeAdapterLM
{
	public static String getString(UUID id)
	{
		if(id == null) return null;
		long msb = id.getMostSignificantBits();
		long lsb = id.getLeastSignificantBits();
		StringBuilder sb = new StringBuilder(32);
		digitsUUID(sb, msb >> 32, 8);
		digitsUUID(sb, msb >> 16, 4);
		digitsUUID(sb, msb, 4);
		digitsUUID(sb, lsb >> 48, 4);
		digitsUUID(sb, lsb, 12);
		return sb.toString();
	}
	
	private static void digitsUUID(StringBuilder sb, long val, int digits)
	{
		long hi = 1L << (digits * 4);
		String s = Long.toHexString(hi | (val & (hi - 1)));
		sb.append(s, 1, s.length());
	}
	
	public static UUID getUUID(String s)
	{
		try
		{
			if(s.indexOf('-') != -1) return UUID.fromString(s);
			
			int l = s.length();
			StringBuilder sb = new StringBuilder(32);
			for(int i = 0; i < l; i++)
			{
				sb.append(s.charAt(i));
				if(i == 7 || i == 11 || i == 15 || i == 19) sb.append('-');
			}
			
			return UUID.fromString(sb.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}