package com.ssquare.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.RoomDatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssquare.notes.Adapters.NotesListAdapter;
import com.ssquare.notes.DataBase.RoomDB;
import com.ssquare.notes.Models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotesListAdapter adapter;
    List<Note> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_homePage);
        fab_add = findViewById(R.id.fab_add);

        database = RoomDB.getInstance(getApplicationContext());
        notes = database.noteDAO().getAllNotes();
        updateRecyclerView(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,101);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode== Activity.RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                database.noteDAO().insert(new_note);
                notes.clear();
                notes.addAll(database.noteDAO().getAllNotes());
                adapter.notifyDataSetChanged();
            }
        }

        if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                database.noteDAO().update(new_note.getID(),new_note.getTitle(),new_note.getNotes());
                notes.clear();
                notes.addAll(database.noteDAO().getAllNotes());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecyclerView(List<Note> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));

        adapter = new NotesListAdapter(notes,notesClickListener,MainActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onPress(Note note) {
            Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
            intent.putExtra("old_note",note);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongPress(Note note, CardView cardView) {

        }
    };
}