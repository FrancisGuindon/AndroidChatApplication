package com.pam.travail5.manager;

import com.pam.travail5.model.Message;

public class RemoteUser extends UserHolder {
    public RemoteUser(String uuid, DatabaseManager databaseManager) {
        super(uuid, databaseManager);
    }

    @Override
    public Message newMessage(String content) {
        return getDatabaseManager().newMessage(getUuid(), content, Message.TYPE_INCOMING);
    }
}
