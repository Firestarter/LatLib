package latmod.lib;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.*;
import java.util.*;

/** Made by LatvianModder */
public class LMUtils
{
	// Class / Object //
	
	private static final Comparator<Package> packageComparator = new Comparator<Package>()
	{
		public int compare(Package o1, Package o2)
		{ return o1.getName().compareTo(o2.getName()); }
	};
	
	@SuppressWarnings("all")
	public static <E> E newObject(Class<?> c, Object... o) throws Exception
	{
		if(c == null) return null;
		
		if(o != null && o.length > 0)
		{
			Class<?>[] params = new Class<?>[o.length];
			for(int i = 0; i < o.length; i++)
			params[i] = o.getClass();
			
			Constructor<?> c1 = c.getConstructor(params);
			return (E) c1.newInstance(o);
		}
		
		return (E) c.newInstance();
	}
	
	public static Package[] getAllPackages()
	{
		Package[] p = Package.getPackages();
		Arrays.sort(p, packageComparator);
		return p;
	}
	
	public static String classpath(Class<?> c)
	{ return (c == null) ? null : c.getName(); }
	
	public static FastList<Class<?>> addSubclasses(Class<?> c, FastList<Class<?>> al, boolean all)
	{
		if(c == null) return null;
		if(al == null) al = new FastList<Class<?>>();
		FastList<Class<?>> al1 = new FastList<Class<?>>();
		al1.addAll(c.getDeclaredClasses());
		if(all && !al1.isEmpty()) for(int i = 0; i < al1.size(); i++)
		al.addAll(addSubclasses(al1.get(i), null, true));
		al.addAll(al1); return al;
	}
	
	public static boolean areObjectsEqual(Object o1, Object o2, boolean allowNulls)
	{
		if(o1 == null && o2 == null) return allowNulls;
		if(o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}
	
	public static int hashCodeOf(Object o)
	{ return o == null ? 0 : o.hashCode(); }
	
	public static int hashCode(Object... o)
	{
		if(o == null || o.length == 0) return 0;
		if(o.length == 1) return hashCodeOf(o[0]);
		int h = 0;
		for(int i = 0; i < o.length; i++)
			h = h * 31 + hashCodeOf(o[i]);
		return h;
	}
	
	public static void throwException(Exception e) throws Exception
	{ if(e != null) throw e; }
	
	// Net //
	
	public static String getHostAddress()
	{
		try { return InetAddress.getLocalHost().getHostAddress(); }
		catch(Exception e) { } return null;
	}
	
	public static String getExternalAddress()
	{
		try { return LMStringUtils.readString(new URL("http://checkip.amazonaws.com").openStream()); }
		catch(Exception e) { } return null;
	}
	
	// Misc //
	
	public static boolean openURI(URI uri) throws Exception
	{
		Class<?> oclass = Class.forName("java.awt.Desktop");
		Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
		oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { uri });
		return true;
	}
	
	public static long millis()
	{ return System.currentTimeMillis(); }
	
	public static void moveBytes(InputStream is, OutputStream os, boolean close) throws Exception
	{
		byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer, 0, buffer.length)) > 0)
			os.write(buffer, 0, len);
		os.flush();
		
		if(close) { is.close(); os.close(); }
	}
	
	@SuppressWarnings("all")
	public static <T> T[] newArray(int length, Class<? extends T> typeClass)
	{ return (T[])new Object[length]; }
	
	@SuppressWarnings("all")
	public static <T> T[] convertArray(Object[] array, Class<? extends T> typeClass)
	{
		if(array == null) return null;
		T[] t = newArray(array.length, typeClass);
		System.arraycopy(array, 0, t, 0, t.length);
		return t;
	}
}