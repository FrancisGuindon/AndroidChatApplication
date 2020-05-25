package com.pam.travail5;

import android.icu.text.DateFormat;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.travail5.model.MessageSession;

public class MessageHolder extends RecyclerView.ViewHolder {

    private TextView message;
    private final TextView time;
    private final ImageView avatar;


    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.contenu);
        time = itemView.findViewById(R.id.time);
        avatar = itemView.findViewById(R.id.avatar);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(MessageSession message) {
        this.message.setText(message.getMessage().getMessage());
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        this.time.setText(formatter.format(message.getMessage().getTime()));
        if (message.getDetails().getAvatar() != null && message.getDetails().getAvatar().getImage() != null) {
            avatar.setVisibility(View.VISIBLE);
            avatar.setImageBitmap(message.getDetails().getAvatar().getImage());
        } else {
            avatar.setVisibility(View.GONE);
        }
    }
}
