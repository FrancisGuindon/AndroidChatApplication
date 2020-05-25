package com.pam.travail5.manager;

public class UserManager {

    private DatabaseManager databaseManager;
    private LocalUser local;

    public UserManager(DatabaseManager databaseManager, String uuid) {
        this.databaseManager = databaseManager;
        this.local = new LocalUser(uuid, databaseManager);
    }

    public UserHolder getLocalUser() {
        return local;
    }

    public UserHolder getRemoteUser(String uuid) {
        return new RemoteUser(uuid, databaseManager);
    }
}
