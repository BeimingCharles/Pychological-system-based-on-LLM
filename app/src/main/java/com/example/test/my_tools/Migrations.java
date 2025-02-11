package com.example.test.my_tools;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;

public class Migrations {
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 例如，假设我们需要添加一个新列到 ChatRecord 表
            database.execSQL("ALTER TABLE ChatRecord ADD COLUMN new_column_name TEXT");
        }
    };
}
