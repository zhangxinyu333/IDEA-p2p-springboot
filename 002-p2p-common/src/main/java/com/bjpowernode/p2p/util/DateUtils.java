package com.bjpowernode.p2p.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 张新宇
 * 2020/8/28
 */
public class DateUtils {
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
}
