package com.pam.travail5.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;

public class Converter {

    @TypeConverter
    public static Date convert(Long value)
    {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long convert(Date date)
    {
        return date == null ? null : date.getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String convert(Bitmap bitmap)
    {
        if (bitmap == null) return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static Bitmap convert(String base64)
    {
        if (base64 == null) return null;
        byte [] decodedBytes = Base64.getDecoder().decode(base64);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
