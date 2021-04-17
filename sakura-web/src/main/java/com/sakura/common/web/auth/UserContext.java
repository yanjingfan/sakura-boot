package com.sakura.common.web.auth;

public class UserContext {
    private static ThreadLocal<LocalUser> userThread = new ThreadLocal<>();

    public static void set(LocalUser user) {
        userThread.set(user);
    }

    public static LocalUser get() {
        return userThread.get();
    }

    public static void remove() {
        userThread.remove();
    }

}
