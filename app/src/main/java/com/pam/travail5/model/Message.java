package com.pam.travail5.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pam.travail5.persistence.Converter;

import java.util.Date;

@Entity(tableName = "MESSAGES")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String message;
    @TypeConverters({Converter.class})
    private Date time;
    private int type;

    private long sessionId;

    public static final int TYPE_INCOMING = 0;
    public static final int TYPE_OUTGOING = 1;


    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public long getSessionId() {
        return sessionId;
    }

    public Message(long id, String message, Date time, int type, long sessionId) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.type = type;
        this.sessionId = sessionId;
    }

    @Ignore
    public Message(String message, int type, long sessionId) {
        this.message = message;
        this.type = type;
        this.sessionId = sessionId;
        this.time = new Date();
    }
}
