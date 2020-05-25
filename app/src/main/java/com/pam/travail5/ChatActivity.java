package com.pam.travail5;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pam.travail5.manager.ConnectionManager;
import com.pam.travail5.manager.DatabaseManager;
import com.pam.travail5.manager.UserManager;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.Session;
import com.pam.travail5.persistence.ChatDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.*;

public class ChatActivity extends AppCompatActivity implements ConnectionManager.Listener {

    protected UserManager userManager;

    protected ConnectionManager connectionManager;

    protected DatabaseManager databaseManager;

    protected ChatApplication getChatApplication() {
        return (ChatApplication) getApplication();
    }

    protected Socket getSocket() {
        return getChatApplication().getSocket();
    }

    protected ChatDatabase getDatabase() {
        return getChatApplication().getDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        getConnectionManager().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        getConnectionManager().pause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseManager = new DatabaseManager() {
            @Override
            public ChatDatabase getDatabase() {
                return ChatActivity.this.getDatabase();
            }
        };

        userManager = new UserManager(databaseManager, getChatApplication().getUuid());

        connectionManager = new ConnectionManager(getSocket(), userManager);

        connectionManager.setListener(this);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void onLoggedIn(Session session) {
        System.out.println("user joined" + session.getUsername());
    }

    @Override
    public void onDisconnected() {
        System.out.println("disconnected");
    }

    @Override
    public void onConnected(String url) {
        System.out.println("connected to " + url);
    }

    @Override
    public void onUserJoined(Session session) {
        System.out.println("user joined " + session.getUsername());
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("message received " + message.getMessage());
    }
}
