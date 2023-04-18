package com.example.epos.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id)
    {
        threadLocal.set(id);
    }
    // thread as the apply field
    public static Long getCurrentId()
    {
        return threadLocal.get();
    }
}
