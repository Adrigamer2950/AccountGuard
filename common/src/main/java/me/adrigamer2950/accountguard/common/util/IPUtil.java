package me.adrigamer2950.accountguard.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {

    public static final String IPV4_REGEX = "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";
    public static final String IPV6_REGEX = "\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z";

    public static boolean checkIP(String ip) {
        Pattern pV4 = Pattern.compile(IPV4_REGEX);
        Pattern pV6 = Pattern.compile(IPV6_REGEX);

        if (ip == null)
            return false;

        Matcher m = pV4.matcher(ip);
        Matcher m2 = pV6.matcher(ip);

        return m.matches() || m2.matches();
    }
}