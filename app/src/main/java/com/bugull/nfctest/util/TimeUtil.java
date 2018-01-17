package com.bugull.nfctest.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author kk on 2018/1/16  13:31.
 *         Email:763744747@qq.com
 */

public class TimeUtil {
    public static String curTime(long time) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA).format(new Date(time));
    }
}
