package com.pam.travail5;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.pam.travail5.model.Message;
import com.pam.travail5.model.MessageSession;

import java.util.List;
import java.util.Objects;

public class MainActivity extends ChatActivity {

    private RecyclerView list;
    private EditText text;
    private String username;
    List<MessageSession> messages;
    MessageAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.mylist);
        text = findViewById(R.id.inputText);

        username = getIntent().getStringExtra(LoginActivity.USERNAME);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);

        messages = getDatabaseManager().getAllMessages();
        adapter = new MessageAdapter(messages);
        list.setAdapter(adapter);

        list.post(() -> list.smoothScrollToPosition(adapter.getItemCount() - 1));

        setFullScreen();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setFullScreen() {
        View root = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onStop() {
        super.onStop();

        getSocket().close();
        getDatabase().close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSocket().connect();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onConnected(String url) {
        super.onConnected(url);

        getConnectionManager().login();
    }

    public void send(View view) {
        String message = text.getText().toString().trim();
        if (message.equals("")) return;

        Message msg = getConnectionManager().sendMessage(message);
        add(msg);
        text.clearFocus();
        text.setText(null);

        InputMethodManager inm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void add(Message message)
    {
        messages.add(getDatabaseManager().getMessageDetails(message.getId()));
        adapter.notifyDataSetChanged();
        list.scrollToPosition(messages.size() - 1);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        add(message);
    }
}