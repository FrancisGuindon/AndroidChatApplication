package com.pam.travail5.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class SessionAvatar {
    @Embedded
    private Session session;

    @Relation(
            parentColumn = "avatarId",
            entityColumn = "id"
    )
    private Avatar avatar;

    public SessionAvatar(Session session, Avatar avatar) {
        this.session = session;
        this.avatar = avatar;
    }

    public Session getSession() {
        return session;
    }

    public Avatar getAvatar() {
        return avatar;
    }
}
