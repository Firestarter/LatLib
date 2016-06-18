package com.latmod.lib;

public enum OS
{
    WINDOWS,
    LINUX,
    OSX,
    OTHER;

    public static final OS current = get();
    public static final String ARCH = System.getProperty("sun.arch.data.model");
    public static final boolean IS_64_ARCH = ARCH.equals("64");

    private static OS get()
    {
        String s = System.getProperty("os.name");
        if(s == null || s.isEmpty())
        {
            return OTHER;
        }
        s = s.toLowerCase();
        if(s.contains("win"))
        {
            return WINDOWS;
        }
        else if(s.contains("mac"))
        {
            return OSX;
        }
        else if(s.contains("linux") || s.contains("unix"))
        {
            return LINUX;
        }
        return OTHER;
    }
}