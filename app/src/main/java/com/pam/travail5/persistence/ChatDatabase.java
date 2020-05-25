package com.pam.travail5.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pam.travail5.model.Avatar;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.Session;
import com.pam.travail5.model.User;

@Database(entities = {Message.class, Avatar.class, Session.class, User.class}, version = 2)
public abstract class ChatDatabase extends RoomDatabase {

    public abstract MessageDao getMessageDao();

    public abstract AvatarDao getAvatarDao();

    public abstract UserDao getUserDao();

    public abstract SessionDao getSessionDao();
}
