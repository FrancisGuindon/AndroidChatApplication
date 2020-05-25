package com.pam.travail5.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pam.travail5.model.Session;

@Dao
public abstract class SessionDao {

    @Insert
    public abstract long insert(Session session);

    @Query("SELECT * FROM sessions where id = :id")
    public abstract Session queryForId(long id);

    @Query("SELECT sessions.* FROM sessions INNER JOIN users ON sessions.userId = users.id " +
            "WHERE users.uuid = :userUuid ORDER BY sessions.login DESC LIMIT 1")
    public abstract Session queryForLast(String userUuid);
}
