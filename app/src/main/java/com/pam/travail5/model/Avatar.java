package com.pam.travail5.model;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pam.travail5.persistence.Converter;

@Entity(tableName = "avatars", indices = {@Index(value = {"image"}, unique = true)})
public class Avatar {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @TypeConverters(value = Converter.class)
    private Bitmap image;

    public Avatar(long id, Bitmap image)
    {
        this.id = id;
        this.image = image;
    }

    @Ignore
    public Avatar(Bitmap image)
    {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }
}
