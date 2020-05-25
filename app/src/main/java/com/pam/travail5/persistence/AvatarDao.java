package com.pam.travail5.persistence;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.pam.travail5.model.Avatar;

@Dao
public abstract class AvatarDao {

    public Avatar queryOrCreate(Avatar avatar) {
        if (avatar.getId() >= 1) {
            return avatar;
        }
        long id = insert(avatar);
        if (id != -1) {
            avatar = queryForId(id);
        } else {
            avatar = queryForData(avatar.getImage());
        }
        return avatar;
    }

    @TypeConverters({Converter.class})
    @Query("SELECT * FROM avatars WHERE image = :image")
    public abstract Avatar queryForData(Bitmap image);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Avatar avatar);

    @Query("SELECT * FROM avatars where id = :id")
    public abstract Avatar queryForId(long id);

    @Query("SELECT avatars.*" +
            "FROM avatars," +
            "sessions," +
            "users " +
            "WHERE sessions.avatarId = avatars.id " +
            "AND sessions.avatarId != -1 " +
            "AND sessions.userId = users.id " +
            "AND users.uuid = :uuid " +
            "ORDER BY sessions.login DESC LIMIT 1;")
    public abstract Avatar queryForLast(String uuid);

}
