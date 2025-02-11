package com.example.test.Database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ChatRecord.class}, version = 3, exportSchema = false)
public abstract class ChatRecordDatabase extends RoomDatabase {
    public abstract ChatRecordDao chatRecordDao();

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 假设你需要添加一个新列 "new_column_name" 到 ChatRecord 表
            database.execSQL("ALTER TABLE ChatRecord ADD COLUMN new_column_name TEXT");
        }
    };


}
