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

    @Query("SELECT * FROM notes WHERE pinned = :pinned ORDER BY date DESC ")
    List<Note> getAllNotes(boolean pinned);

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id,String title, String notes);

    @Delete
    void delete(Note note);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);

    @Query("SELECT * FROM notes WHERE pinned = :pinned")
    List<Note> getAllPinned(boolean pinned);

}
