package com.amit.apptest.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SessionManager {
    private static final ConcurrentHashMap<String, AtomicBoolean> loginMap = new ConcurrentHashMap<>();

    public static boolean isLoggedIn(String deviceKey) {
        AtomicBoolean flag = loginMap.get(deviceKey);
        return flag != null && flag.get();
    }

    public static void markLoggedIn(String deviceKey) {
        loginMap.putIfAbsent(deviceKey, new AtomicBoolean(true));
        loginMap.get(deviceKey).set(true);
    }

    public static void markLoggedOut(String deviceKey) {
        loginMap.put(deviceKey, new AtomicBoolean(false));
    }
}