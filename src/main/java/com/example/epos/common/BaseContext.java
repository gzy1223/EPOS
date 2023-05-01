package com.example.epos.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
    public static int searchSubString(String str, String subStr) {
        if (str == null || subStr == null) {
            return 0;
        }
        int strLen = str.length();
        int subStrLen = subStr.length();
        if (strLen < subStrLen) {
            return 0;
        }
        for (int i = 0; i <= strLen - subStrLen; i++) {
            if (str.substring(i, i + subStrLen).equals(subStr)) {
                return 1;
            }
        }
        return 0;
    }

    public static LocalDateTime Timestamp2LocalDateTime(String orderTimeStamp) {
        long timeStamp = Long.parseLong(orderTimeStamp);
        LocalDateTime localDateTime = Instant.ofEpochMilli(timeStamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String formattedDateTime = localDateTime.format(formatter);
        return localDateTime;
    }
}
