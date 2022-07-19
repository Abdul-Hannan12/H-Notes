package com.rockykhan.hnotes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "text")
    String text;

    @Ignore
    public Note() {
    }

    public Note(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    @Ignore
    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
