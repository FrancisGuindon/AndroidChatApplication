package com.pam.travail5;

import androidx.room.TypeConverter;

import java.sql.Date;

public class DateConverter {
    @TypeConverter
    public static String fromDate(Date date)
    {
        return date.toString();
    }
}
