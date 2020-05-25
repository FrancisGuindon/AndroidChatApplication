package com.pam.travail5;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.pam.travail5.model.MessageSession;

public class IncomingMessageHolder extends MessageHolder {

    private final TextView user;

    public IncomingMessageHolder(@NonNull View itemView) {
        super(itemView);
        user = itemView.findViewById(R.id.user);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MessageSession message) {
        super.update(message);
        user.setText(message.getDetails().getSession().getUsername());
    }
}
