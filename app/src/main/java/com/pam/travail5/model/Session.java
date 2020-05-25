package com.pam.travail5.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pam.travail5.persistence.Converter;

import java.util.Date;

@Entity(tableName = "sessions")
@TypeConverters(value = Converter.class)
public class Session {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date login;
    private String username;
    private long userId;
    private long avatarId;

    public Session(long id, Date login, String username, long userId, long avatarId) {
        this.id = id;
        this.login = login;
        this.username = username;
        this.userId = userId;
        this.avatarId = avatarId;
    }

    public Session(User user, Avatar avatar, String username, Date loginTime) {
        this.userId = user.getId();
        if (avatar != null) {
            avatarId = avatar.getId();
        }
        this.username = username;
        this.login = loginTime;
    }

    public long getId() {
        return id;
    }

    public Date getLogin() {
        return login;
    }

    public String getUsername() {
        return username;
    }

    public long getUserId() {
        return userId;
    }

    public long getAvatarId() {
        return avatarId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
