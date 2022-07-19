package com.rockykhan.hnotes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotesDAO {

    @Query("SELECT * FROM Note")
    List<Note> getNotes();

    @Insert
    void addNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

}
