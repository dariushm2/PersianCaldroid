package com.dariushm2.PersianCaldroid;

public class Constants {

    public static final String DEFAULT_APP_LANGUAGE = "fa";


    public static final String LIGHT_THEME = "LightTheme";
    public static final String DARK_THEME = "DarkTheme";


    public static final int MONTHS_LIMIT = 5000; // this should be an even number
    public static final String OFFSET_ARGUMENT = "OFFSET_ARGUMENT";
    public static final String BROADCAST_INTENT_TO_MONTH_FRAGMENT = "BROADCAST_INTENT_TO_MONTH_FRAGMENT";
    public static final String BROADCAST_INTENT_TO_MONTH_DIALOG = "BROADCAST_INTENT_TO_MONTH_DIALOG";
    public static final String BROADCAST_FIELD_TO_MONTH_FRAGMENT = "BROADCAST_FIELD_TO_MONTH_FRAGMENT";
    public static final String BROADCAST_FIELD_TO_MONTH_DIALOG = "BROADCAST_FIELD_TO_MONTH_DIALOG";
    public static final String BROADCAST_FIELD_SELECT_DAY = "BROADCAST_FIELD_SELECT_DAY";

    public static final int BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY = Integer.MAX_VALUE;



    public static final char PERSIAN_COMMA = '،';
    public static final char RLM = '\u200F';

    public static final String[] FIRST_CHAR_OF_DAYS_OF_WEEK_NAME = {"ش", "ی", "د", "س",
            "چ", "پ", "ج"};
    public static final char[] ARABIC_DIGITS = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    public static final char[] PERSIAN_DIGITS = {'۰', '۱', '۲', '۳', '۴', '۵', '۶',
            '۷', '۸', '۹'};


}
