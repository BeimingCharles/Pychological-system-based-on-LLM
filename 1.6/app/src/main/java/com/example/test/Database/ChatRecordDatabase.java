package com.example.test.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatRecord.class}, version = 4, exportSchema = false)
public abstract class ChatRecordDatabase extends RoomDatabase {
    public abstract ChatRecordDao chatRecordDao();
}

