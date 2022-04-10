package com.ssquare.notes.DataBase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ssquare.notes.Models.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert(onConflict = REPLACE)
    void insert(Note note);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id,String title, String notes);

    @Delete
    void delete(Note note);


}
