package com.pam.travail5.manager;

import com.pam.travail5.model.Avatar;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.MessageSession;
import com.pam.travail5.model.Session;
import com.pam.travail5.model.User;
import com.pam.travail5.persistence.ChatDatabase;

import java.util.Date;
import java.util.List;

public abstract class DatabaseManager {

    public Avatar getAvatar(String uuid) {
        return getDatabase().getAvatarDao().queryForLast(uuid);
    }

    public Session getSession(String uuid) {
        return getDatabase().getSessionDao().queryForLast(uuid);
    }

    public List<MessageSession> getAllMessages() {
        return getDatabase().getMessageDao().queryForAll();
    }

    public MessageSession getMessageDetails(long msgId) {
        return getDatabase().getMessageDao().queryByIdWithSession(msgId);
    }

    public Message newMessage(String uuid, String content, int type) {
        Session session = getSession(uuid);
        Message message = getDatabase().getMessageDao().insert(session, type, content);
        return message;
    }

    public Session newSession(String uuid, String username,
                              Avatar avatar, Date loginTime) {

        User user = getDatabase().getUserDao().queryOrCreate(uuid);
        if (avatar != null) {
            avatar = getDatabase().getAvatarDao().queryOrCreate(avatar);
        }
        if (loginTime == null) {
            loginTime = new Date();
        }

        long id = getDatabase().getSessionDao().insert(new Session(user, avatar, username, loginTime));
        return getDatabase().getSessionDao().queryForId(id);
    }

    public abstract ChatDatabase getDatabase();
}
