package com.pam.travail5.manager;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.pam.travail5.ChatActivity;
import com.pam.travail5.ChatApplication;
import com.pam.travail5.model.Message;
import com.pam.travail5.model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_DISCONNECT;

public class ConnectionManager {

    public static final String SELF_LOGGED_IN = "login";
    public static final String ADD_USER = "add user";
    public static final String NEW_MESSAGE = "new message";
    public static final String MESSAGE_RECEIVED = "new message";

    // json
    public static final String USERNAME = "username";
    public static final String AVATAR = "avatar";
    public static final String USERID = "userid";
    public static final String USERS = "users";
    public static final String LOGTIME = "logtime";
    public static final String MESSAGE = "message";
    public static final String USER_JOINED = "user joined";

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        void onLoggedIn(Session session);

        void onDisconnected();

        void onConnected(String url);

        void onUserJoined(Session session);

        void onMessage(Message message);

    }

    private Socket socket;
    private final Handler runOnUiThread;
    private Listener listener;
    private final UserManager userManager;


    public ConnectionManager(Socket socket, UserManager userManager) {
        this.socket = socket;

        runOnUiThread = new Handler(Looper.getMainLooper());
        this.userManager = userManager;
    }

    protected void onMessage(Message message) {
        System.out.println("message received");
    }

    public Socket getSocket() {
        return socket;
    }

    public void pause() {
        getSocket().off();
    }


    protected void on(String event, Emitter.Listener listener) {
        getSocket().on(event, args -> runOnUiThread(() -> listener.call(args)));
    }

    private void runOnUiThread(Runnable runnable) {
        runOnUiThread.post(runnable);
    }

    protected void onConnected(String url) {
        System.out.println("connected");
        listener.onConnected(url);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onLoggedIn(JSONObject data) {
        System.out.println("logged in");

        Session session = getUserManager().getLocalUser().newSession();
        listener.onLoggedIn(session);

        try {
            JSONArray users = data.getJSONArray(USERS);
            for (int i = 0; i < users.length(); i++) {
                onUserJoined(users.getJSONObject(i));
            }
        } catch (JSONException e) {
            // Mauvais format de message en provenance du serveur, ignorer
        }
    }

    public Message sendMessage(String message) {
        getSocket().emit(NEW_MESSAGE, message);
        return getUserManager().getLocalUser().newMessage(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onUserJoined(JSONObject data) {
        try {
            String userId = data.getString(USERID);
            String username = data.getString(USERNAME);
            String avatarBase64 = null;
            Date loginTime = null;
            if (data.has(AVATAR)) {
                avatarBase64 = data.getString(AVATAR);
                if (avatarBase64.length() >= 200e3) {
                    //refuser les images trop grandes
                    avatarBase64 = null;
                }
            }
            if (data.has(LOGTIME)) {
                loginTime = new Date(data.getLong(LOGTIME));
            }
            Session session = getUserManager().getRemoteUser(userId).setAvatar(avatarBase64).setUsername(username).setLogintime(loginTime).newSession();
            listener.onUserJoined(session);
        } catch (JSONException e) {
            // Mauvais format de message en provenance du serveur, ignorer
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login() {
        JSONObject json = new JSONObject();
        String username = getUserManager().getLocalUser().getUsername();
        String avatar = getUserManager().getLocalUser().getAvatarBase64();
        String uuid = getUserManager().getLocalUser().getUuid();

        try {
            json.put(USERNAME, username);
            json.put(AVATAR, avatar);
            json.put(USERID, uuid);
            getSocket().emit(ADD_USER, json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void onMessageReceived(JSONObject data) {
        try {
            String userid = data.getString(USERID);
            String content = data.getString(MESSAGE);
            Message message = getUserManager().getRemoteUser(userid).newMessage(content);
            listener.onMessage(message);

        } catch (JSONException e) {
            // Mauvais format, ignorer
        }
    }

    private UserManager getUserManager() {
        return userManager;
    }

    protected void onDisconnected() {
        System.out.println("disconnected");
        listener.onDisconnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void resume() {

        on(EVENT_CONNECT, args -> this.onConnected(ChatApplication.URL));

        on(EVENT_DISCONNECT, args -> this.onDisconnected());

        on(SELF_LOGGED_IN, args -> this.onLoggedIn((JSONObject) args[0]));

        on(NEW_MESSAGE, args -> this.onMessageReceived((JSONObject) args[0]));

        on(USER_JOINED, args->this.onUserJoined((JSONObject)args[0]));
    }
}
