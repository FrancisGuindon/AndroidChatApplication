package com.pam.travail5.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pam.travail5.model.Message;
import com.pam.travail5.model.MessageSession;
import com.pam.travail5.model.Session;

import java.util.List;

@Dao
public abstract class MessageDao {

    public Message insert(Session session, int type, String data)
    {
        long id = insert(new Message(data, type, session.getId()));
        return queryById(id);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Message user);

    @Query("SELECT * FROM MESSAGES WHERE id = :id")
    public abstract MessageSession queryByIdWithSession(long id);

    @Query(value ="SELECT * FROM MESSAGES")
    public abstract List<MessageSession> queryForAll();

    @Query(value = "SELECT * from messages where id = :id")
    public abstract Message queryById(long id);


}
