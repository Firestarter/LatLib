package latmod.lib;

import java.io.IOException;
import java.util.*;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.*;

public final class Time implements Comparable<Time>
{
	public final long millis;
	public final byte seconds;
	public final byte minutes;
	public final byte hours;
	public final byte day;
	public final byte month;
	public final int year;
	
	@SuppressWarnings("deprecation")
	private Time(Date date)
	{
		millis = date.getTime();
		seconds = (byte)date.getSeconds();
		minutes = (byte)date.getMinutes();
		hours = (byte)date.getHours();
		day = (byte)date.getDate();
		month = (byte)(date.getMonth() + 1);
		year = date.getYear() + 1900;
		date = null;
	}
	
	public boolean equalsTime(long t)
	{ return millis == t; }
	
	public int hashCode()
	{ return Long.hashCode(millis); }
	
	public boolean equals(Object o)
	{ return o != null && (o == this || (o instanceof Time && equalsTime(((Time)o).millis)) || (o instanceof Number && equalsTime(((Number)o).longValue()))); }
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(year);
		sb.append(',');
		append00(sb, month);
		sb.append(',');
		append00(sb, day);
		sb.append(',');
		append00(sb, hours);
		sb.append(',');
		append00(sb, minutes);
		sb.append(',');
		append00(sb, seconds);
		sb.append(',');
		append000(sb, (int)(millis % 1000L));
		return sb.toString();
	}
	
	public int compareTo(Time o)
	{ return Long.compare(millis, o.millis); }
	
	private static void append00(StringBuilder sb, int i)
	{ if(i < 10) sb.append('0'); sb.append(i); }
	
	private static void append000(StringBuilder sb, int i)
	{
		if(i < 100) sb.append('0');
		if(i < 10) sb.append('0');
		sb.append(i);
	}
	
	public String getTime()
	{
		StringBuilder sb = new StringBuilder();
		append00(sb, hours);
		sb.append(':');
		append00(sb, minutes);
		sb.append(':');
		append00(sb, seconds);
		return sb.toString();
	}
	
	public String getTimeHMS()
	{
		StringBuilder sb = new StringBuilder();
		
		if(hours > 0)
		{
			append00(sb, hours);
			sb.append('h');
		}
		
		if(hours > 0 || minutes > 0)
		{
			append00(sb, minutes);
			sb.append('m');
		}
		
		append00(sb, seconds);
		sb.append('s');
		return sb.toString();
	}
	
	public String getDate()
	{
		StringBuilder sb = new StringBuilder();
		append00(sb, day);
		sb.append('.');
		append00(sb, month);
		sb.append('.');
		sb.append(year);
		return sb.toString();
	}
	
	public String getDateAndTime()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getDate());
		sb.append(' ');
		sb.append(getTime());
		return sb.toString();
	}
	
	public long getDelta()
	{ return Math.abs(now().millis - millis); }
	
	// Static //
	
	@SuppressWarnings("deprecation")
	public static Time parseTime(String s)
	{
		if(s == null || s.length() < 20) return null;
		String[] s1 = s.split(",");
		if(s1 == null || s1.length != 7) return null;
		
		int year = Integer.parseInt(s1[0]);
		int month = Integer.parseInt(s1[1]);
		int day = Integer.parseInt(s1[2]);
		int hours = Integer.parseInt(s1[3]);
		int minutes = Integer.parseInt(s1[4]);
		int seconds = Integer.parseInt(s1[5]);
		return get(new Date(year - 1900, month - 1, day, hours, minutes, seconds).getTime() + Integer.parseInt(s1[6]));
		//public Date(int year, int month, int date, int hrs, int min)
	}
	
	public static Time get(Date date)
	{ return new Time(date); }
	
	public static Time get(long millis)
	{ return get(new Date(millis)); }
	
	public static Time now()
	{ return get(new Date()); }
	
	public static class Serializer extends TypeAdapter<Time>	
	{
		public void write(JsonWriter out, Time value) throws IOException
		{
			if(value == null) out.nullValue();
			else out.value(value.toString());
		}
		
		public Time read(JsonReader in) throws IOException
		{
			if(in.peek() == JsonToken.NULL) { in.nextNull(); return null; }
			return parseTime(in.nextString());
		}
	}
}