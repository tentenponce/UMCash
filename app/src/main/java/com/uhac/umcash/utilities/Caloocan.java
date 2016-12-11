package com.uhac.umcash.utilities;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility methods
 * Created by Exequiel Egbert V. Ponce on 12/10/2016.
 */

public class Caloocan {
    public static final String PESO_SIGN = "\u20B1";
    public static final NumberFormat numberFormat = new DecimalFormat("#,##0.00");
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * Capitalize first letterssss
     * @param str word(s) to capitalize each first letter
     * @return the word(s)
     */
    public static String capitalizeAll(String str) {
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            try {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
                builder.append(cap + " ");
            } catch (StringIndexOutOfBoundsException e) {
                Log.e("androidruntime", "fail capitalize");
            }
        }

        return builder.toString();
    }

    /**
     * Checks the difference between two dates and returns prioritizing
     * year up to below:
     * 2 years ago
     * if it is 0 year, it goes down to months and so on.
     *
     * @param date1 minuend date Today
     * @param date2 subtrahend
     * @return 6 months ago, 2 seconds ago, 1 hour ago.
     */
    public static String dateDifference(String date1, String date2) {
        Date convertedDate1 = Calendar.getInstance().getTime();
        Date convertedDate2 = Calendar.getInstance().getTime();

        try {
            convertedDate1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date1);
            convertedDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date2);
        } catch (ParseException e) {
            Log.e("androidruntime", "Date 1: " + date1);
            Log.e("androidruntime", "Date 2: " + date2);
        }

        String diff = "Now";

        long seconds = (convertedDate1.getTime() - convertedDate2.getTime()) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = months / 12;

        if (years >= 1) {
            diff = withS(years, "Year");
        } else if (months >= 1) {
            diff = withS(months, "Month");
        } else if (days >= 1) {
            diff = withS(days, "Day");
        } else if (hours >= 1) {
            diff = withS(hours, "Hour");
        } else if (minutes >= 1) {
            diff = withS(minutes, "Minute");
        } else if (seconds >= 1) {
            diff = withS(seconds, "Second");
        }

        return diff;
    }

    /**
     * Checks if the number is greater than 1 to add 's'.
     * Eg: year or years
     *
     * @param num  to be check if greater than 1
     * @param date year, month, day, hour, minute or second.
     * @return 2 years ago, 1 month ago etc.
     */
    public static String withS(long num, String date) {
        int intNum = (int) Math.floor(num);
        if (num >= 2) {
            return intNum + " " + date + "s" + " Ago";
        } else {
            return intNum + " " + date + " Ago";
        }
    }
}
