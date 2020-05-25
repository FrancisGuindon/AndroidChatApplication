package com.pam.travail5.manager;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pam.travail5.model.Avatar;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.Session;
import com.pam.travail5.persistence.Converter;

import java.util.Date;

public abstract class UserHolder {

    // Informations provenant de la base de données
    private Session session;
    private Avatar avatar;

    protected String uuid;
    protected DatabaseManager databaseManager;

    // Informations précisées dans les setters
    protected String username;
    protected String avatarBase64;
    protected Bitmap avatarBitmap;
    protected Date logintime;

    public UserHolder(String uuid, DatabaseManager databaseManager) {
        this.uuid = uuid;
        this.databaseManager = databaseManager;
    }


    // region setters
    public UserHolder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserHolder setAvatar(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
        this.avatarBitmap = null;
        this.avatar = null;
        return this;
    }

    public UserHolder setAvatar(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
        this.avatarBase64 = null;
        this.avatar = null;
        return this;
    }

    public UserHolder setLogintime(Date logintime) {
        this.logintime = logintime;
        return this;
    }

    //endregion

    //region getters
    public Session getSession() {
        if (session == null) {
            session = getDatabaseManager().getSession(getUuid());
        }
        return session;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Avatar getAvatar() {
        if (avatarBitmap != null) {
            avatar = new Avatar(avatarBitmap);
        } else if (avatarBase64 != null) {
            avatar = new Avatar(Converter.convert(avatarBase64));
        } else if (avatar == null) {
            avatar = getDatabaseManager().getAvatar(getUuid());
        }
        return avatar;
    }

    public String getUuid() {
        return uuid;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public String getUsername() {
        String username = this.username;
        if (username == null) {
            Session session = getSession();
            if (session != null) {
                username = session.getUsername();
            }
        }
        return username;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getAvatarBase64() {
        String avatarBase64 = this.avatarBase64;
        if (avatarBase64 == null) {
            avatarBase64 = Converter.convert(getAvatarBitmap());
        }
        return avatarBase64;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getAvatarBitmap() {
        Bitmap avatarBitmap = this.avatarBitmap;
        if (avatarBitmap == null) {
            if (avatarBase64 != null) {
                avatarBitmap = Converter.convert(avatarBase64);
            } else {
                Avatar avatar = getAvatar();
            }
            if (avatar != null) {
                avatarBitmap = avatar.getImage();
            }
        }
        return avatarBitmap;
    }

    public Date getLongintime() {
        return logintime;
    }
    //endregion

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Session newSession() {
        session = getDatabaseManager().newSession(
                getUuid(),
                getUsername(),
                getAvatar(),
                getLongintime()
        );
        avatar = null;
        return session;
    }

    public abstract Message newMessage(String content);

}
