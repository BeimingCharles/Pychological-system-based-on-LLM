package com.example.test.Database;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity(tableName = "ChatRecord")
public class ChatRecord {
    // 主键 自动生成
    @PrimaryKey(autoGenerate = true)
    private int id;

    // 列的名称
    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name ="by")
    private  boolean by;

    public ChatRecord(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getContent() { // 标准的 Getter
        return content;
    }

    public void setContent(String content) { // 标准的 Setter
        this.content = content;
    }

    public boolean getBy(){
        return by;
    }

    public void setBy(boolean by){
        this.by=by;
    }

    @Override
    public String toString() {
      /*  return "ChatRecord{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", by=" + by +
                '}';*/
        return content;
    }
}
