package com.example.test.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatRecordDao {
    @Insert
    long insertDataOne(ChatRecord chat_record);

    //添加  可以传递多个参数 对象
    @Insert
    void insertDataS(ChatRecord... chat_record);

    //删除
    @Delete
    int deleteDataOne(ChatRecord chat_record);

    @Query("DELETE FROM ChatRecord WHERE id = :id")
    int deleteById(int id); // 按 ID 删除

    //修改 传入对象 设置 id 进行修改某一个
    @Update
    int updateData(ChatRecord... chat_record);

    //查询 根据id倒序
    @Query("select * from ChatRecord order by id")
    List<ChatRecord> getChatRecords();

    //根据id查询
    @Query("select * from ChatRecord where id =:numb")
    ChatRecord getChatRecord(int numb);

    //删除表数据
    @Query("delete from ChatRecord")
    void deleteTableData();
}
