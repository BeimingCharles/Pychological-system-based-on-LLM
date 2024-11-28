package com.example.test.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatRecord.class}, version = 3, exportSchema = false)
public abstract class ChatRecordDatabase extends RoomDatabase {
    public abstract ChatRecordDao chatRecordDao(); // 注意方法名以小写开头
}
