package com.glim.common.utils;

public abstract class CountUtil {

    public static String getCountString(Integer count) {

        String countString;
        if (count < 10000) {
            countString = String.valueOf(count);
        }else if (count < 100000000) {
            countString = String.format("%.2f만",  count / 10000.0);
        }else {
            countString = String.format("%.2f억", count / 100000000.0);
        }

        return countString;
    }
}
