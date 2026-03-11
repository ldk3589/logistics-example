package com.dk.logistics.common.utils;


import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            return index > 0 ? ip.substring(0, index).trim() : ip.trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        return ip == null ? "unknown" : ip;
    }

    private static boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }
}