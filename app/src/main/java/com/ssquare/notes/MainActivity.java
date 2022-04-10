package com.ssquare.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.RoomDatabase;

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

    private void updateRecyclerView(List<Note> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));

        adapter = new NotesListAdapter(notes,notesClickListener,MainActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onPress(Note note) {

        }

        @Override
        public void onLongPress(Note note, CardView cardView) {

        }
    };
}