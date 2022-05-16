package ru.fleyer.freports.spigot.utils;

public class NumberStringUtils {
    private static String format(long num, String single, String lessfive, String others) {
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

    public static String getFormat(long number, String single, String lessfive, String others) {
        return number + " " + NumberStringUtils.format(number, single, lessfive, others);
    }
}

