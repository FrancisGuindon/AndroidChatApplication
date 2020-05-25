package com.pam.travail5.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MessageSession {

    @Embedded
    private Message message;

    @Relation(
            entity = Session.class,
            parentColumn = "sessionId",
            entityColumn = "id"
    )
    private SessionAvatar session;

    public MessageSession(Message message, SessionAvatar session) {
        this.message = message;
        this.session = session;
    }

    public Message getMessage() {
        return message;
    }

    public SessionAvatar getDetails() {
        return session;
    }
}
