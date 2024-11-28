package com.example.test.my_tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OpenSqliteHelper extends SQLiteOpenHelper {
    private static final String TAG = "OpenSqliteHelper";
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PWD = "password";

    public OpenSqliteHelper(@Nullable Context context) {
        super(context,  "user.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "创建数据库...");
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " Integer Primary KEY AUTOINCREMENT," + COLUMN_NAME + "," + COLUMN_PWD + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "升级数据库...");
        // 可以添加具体的升级逻辑，如删除旧表、创建新表等
    }

    public String addOne(UserModel usermodel) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME,usermodel.getName());
        cv.put(COLUMN_PWD,usermodel.getPwd());

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        long insert=sqLiteDatabase.insert(TABLE_NAME,COLUMN_ID,cv);
        if(insert==-1){
            return "fail";
        }
        return "success";
    }
    public List<UserModel> getAll() {
        List<UserModel> list = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
        int pwdIndex = cursor.getColumnIndex(COLUMN_PWD);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String pwd = cursor.getString(pwdIndex);
            UserModel userModel = new UserModel(id, name, pwd); // 假设构造函数接受 id, name, pwd
            list.add(userModel);
        }

        cursor.close(); // 关闭游标
        sqLiteDatabase.close(); // 关闭数据库连接

        return list; // 返回列表
    }
    public boolean userExists(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_PWD + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        sqLiteDatabase.close();
        return exists;
    }

}









































