package com.pam.travail5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.pam.travail5.model.Avatar;
import com.pam.travail5.model.Session;

import java.io.File;
import java.io.IOException;

import io.socket.client.Socket;

public class LoginActivity extends ChatActivity {

    public static final String USERNAME = "username";
    public static final int REQUEST_CODE = 1;
    public static final String AVATAR_BITMAP = "avatarBitmap";
    public static final String AVATAR_AUTH = "avatarAuth";
    public static final String AVATAR = "avatar";
    private ProgressBar progressBar;
    private EditText login;
    private TextView loginName;
    ImageView avatar;
    private File avatarFile;
    Bitmap avatarImage;


    protected ChatApplication getChatApplication() {
        //if (getChatApplication() != null)
        return (ChatApplication) getApplication();

    }

    protected Socket getSocket() {
        if (getChatApplication() != null) {
            return getChatApplication().getSocket();
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // IDs
        login = findViewById(R.id.loginName);
        progressBar = findViewById(R.id.loginPgr);
        avatar = findViewById(R.id.avatar);

        if (savedInstanceState != null) {
            Bitmap avatar = savedInstanceState.getParcelable(AVATAR_BITMAP);
            this.avatar.setImageBitmap(avatar);
            getUserManager().getLocalUser().setAvatar(avatar);
        } else {
            // Re-set old avatar
            Avatar oldAvatar = getUserManager().getLocalUser().getAvatar();
            if (oldAvatar != null) {
                Bitmap avatarBitmap = getUserManager().getLocalUser().getAvatar().getImage();
                avatar.setImageBitmap(avatarBitmap);
            }

            // Re-set old name
            String oldName = getUserManager().getLocalUser().getUsername();
            if (oldName != null) {
                login.setText(oldName);
            }
        }

        login.setOnEditorActionListener((v, actionId, event) -> {
            if (!hasUsername()) return false;

            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                login.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                getUserManager().getLocalUser().setUsername(getUsername());
                getSocket().connect();
                return true;
            }
            return false;
        });

        getChatApplication().getUuid();
    }

    private String getUsername() {
        return login.getText().toString();
    }

    private boolean hasUsername() {
        return !"".equals(getUsername().trim());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        progressBar.setVisibility(View.INVISIBLE);
        login.setEnabled(true);
        //login.setText(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onConnected(String url) {
        super.onConnected("connected to " + url);

        getConnectionManager().login();
    }


    @Override
    public void onLoggedIn(Session session) {
        super.onLoggedIn(session);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File outputDir = getCacheDir();
            avatarFile = new File(outputDir, AVATAR);
            Uri avatarUri = FileProvider.getUriForFile(this, AVATAR_AUTH, avatarFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, avatarUri);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (avatar.getDrawable() != null) {
            BitmapDrawable avatarDrawable = (BitmapDrawable) avatar.getDrawable();
            Bitmap avatarBitmap = avatarDrawable.getBitmap();

            outState.putParcelable(AVATAR_BITMAP, avatarBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                ExifInterface exif = new ExifInterface(avatarFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                float rotate = 0.f;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

                avatarImage = BitmapFactory.decodeFile(avatarFile.getAbsolutePath());

                int w = avatarImage.getWidth();
                int h = avatarImage.getHeight();

                // la taille maximale en largeur et en hauteur
                float max = 256;
                float ratio = max / Math.max(w, h);

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                matrix.postScale(ratio, ratio);

                avatarImage = Bitmap.createBitmap(avatarImage, 0, 0, w, h, matrix, true);

                avatar.setImageBitmap(avatarImage);
                getUserManager().getLocalUser().setAvatar(avatarImage);

                avatarFile.delete();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}