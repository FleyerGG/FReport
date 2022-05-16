package ru.fleyer.freports.spigot.utils;

public class TimeUtils {
    public static String getTime(long sec) {
        if (sec < 1L) {
            return "1 \u0441\u0435\u043a\u0443\u043d\u0434\u0430";
        }
        long m = sec / 60L;
        sec %= 60L;
        long h = m / 60L;
        m %= 60L;
        long d = h / 24L;
        h %= 24L;
        long y = d / 365L;
        d %= 365L;
        String time = "";
        if (y > 0L) {
            time = time + y + " " + TimeUtils.formatTime(y, "\u0433\u043e\u0434", "\u043b\u0435\u0442", "\u043b\u0435\u0442");
            if (d > 0L || h > 0L || m > 0L || sec > 0L) {
                time = time + " ";
            }
        }
        if (d > 0L) {
            time = time + d + " " + TimeUtils.formatTime(d, "\u0434\u0435\u043d\u044c", "\u0434\u043d\u044f", "\u0434\u043d\u0435\u0439");
            if (h > 0L || m > 0L || sec > 0L) {
                time = time + " ";
            }
        }
        if (h > 0L) {
            time = time + h + " " + TimeUtils.formatTime(h, "\u0447\u0430\u0441", "\u0447\u0430\u0441\u0430", "\u0447\u0430\u0441\u043e\u0432");
            if (m > 0L || sec > 0L) {
                time = time + " ";
            }
        }
        if (m > 0L) {
            time = time + m + " " + TimeUtils.formatTime(m, "\u043c\u0438\u043d\u0443\u0442\u0430", "\u043c\u0438\u043d\u0443\u0442\u044b", "\u043c\u0438\u043d\u0443\u0442");
            if (sec > 0L) {
                time = time + " ";
            }
        }
        if (sec > 0L) {
            time = time + sec + " " + TimeUtils.formatTime(sec, "\u0441\u0435\u043a\u0443\u043d\u0434\u0430", "\u0441\u0435\u043a\u0443\u043d\u0434\u044b", "\u0441\u0435\u043a\u0443\u043d\u0434");
        }
        return time;
    }

    public static long longTime(String in) {
        int time;
        String name = in.replaceAll("\\d", "");
        try {
            time = Integer.parseInt(in.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            return 0L;
        }
        if (name.equalsIgnoreCase("years") || name.equalsIgnoreCase("year") || name.equalsIgnoreCase("y")) {
            return time * 24 * 60 * 60 * 365;
        }
        if (name.equalsIgnoreCase("days") || name.equalsIgnoreCase("day") || name.equalsIgnoreCase("d")) {
            return time * 24 * 60 * 60;
        }
        if (name.equalsIgnoreCase("hours") || name.equalsIgnoreCase("hour") || name.equalsIgnoreCase("h")) {
            return time * 60 * 60;
        }
        if (name.equalsIgnoreCase("minutes") || name.equalsIgnoreCase("minute") || name.equalsIgnoreCase("min") || name.equalsIgnoreCase("m")) {
            return time * 60;
        }
        if (name.equalsIgnoreCase("seconds") || name.equalsIgnoreCase("second") || name.equalsIgnoreCase("sec") || name.equalsIgnoreCase("s")) {
            return time;
        }
        return 0L;
    }

    private static String formatTime(long num, String single, String lessfive, String others) {
        if (num % 100L > 10L && num % 100L < 15L) {
            return others;
        }
        switch ((int) (num % 10L)) {
            case 1: {
                return single;
            }
            case 2:
            case 3:
            case 4: {
                return lessfive;
            }
        }
        return others;
    }
}

