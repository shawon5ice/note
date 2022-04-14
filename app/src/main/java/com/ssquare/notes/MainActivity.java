package com.ssquare.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssquare.notes.Adapters.NotesListAdapter;
import com.ssquare.notes.DataBase.RoomDB;
import com.ssquare.notes.Models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NotesListAdapter adapter;
    List<Note> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView;
    TextView noNotesFound;
    boolean listView;
    Note selectedNote;
    ImageView mainMenu;
    private UiModeManager uiModeManager;

    private static String LIST_VIEW_KEY = "LIST_VIEW";



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_homePage);
        fab_add = findViewById(R.id.fab_add);
        searchView = findViewById(R.id.searchView_Home);
        noNotesFound = findViewById(R.id.no_Notes_TV);
        mainMenu = findViewById(R.id.main_menu);


        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        listView = sharedPref.getBoolean(LIST_VIEW_KEY,false);


        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor editor = sharedPref.edit();
            @Override
            public void onClick(View view) {
                if(!listView){
                    listView = true;
                    editor.putBoolean(LIST_VIEW_KEY,true).commit();
                    mainMenu.setImageDrawable(getDrawable(R.drawable.ic_grid_day));
                }else{
                    editor.putBoolean(LIST_VIEW_KEY,false).commit();
                    mainMenu.setImageDrawable(getDrawable(R.drawable.ic_list_day));
                    listView = false;
                }
                System.out.println(sharedPref.getBoolean(LIST_VIEW_KEY,false));
                updateRecyclerView(notes,false);
            }
        });

        database = RoomDB.getInstance(getApplicationContext());
        notes = new ArrayList<>();
        notes.addAll(database.noteDAO().getAllPinned(true));
        notes.addAll(database.noteDAO().getAllNotes(false));
        updateRecyclerView(notes,false);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,101);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String newText) {
        List<Note> filterNotes = new ArrayList<>();
        for(Note singleNote: notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            ||singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())
            ||singleNote.getDate().toLowerCase().contains(newText.toLowerCase())){
                filterNotes.add(singleNote);
            }
        }
        updateRecyclerView(filterNotes,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode== Activity.RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                database.noteDAO().insert(new_note);
                notes.clear();
                notes.addAll(database.noteDAO().getAllPinned(true));
                notes.addAll(database.noteDAO().getAllNotes(false));
                updateRecyclerView(notes,false);
            }
        }

        if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                database.noteDAO().update(new_note.getID(),new_note.getTitle(),new_note.getNotes());
                notes.clear();
                notes.addAll(database.noteDAO().getAllPinned(true));
                notes.addAll(database.noteDAO().getAllNotes(false));
                updateRecyclerView(notes,false);
            }
        }
    }

    private void updateRecyclerView(List<Note> notes,boolean fromFilter) {
        if(!fromFilter){
            if(notes.size()==0){
                noNotesFound.setVisibility(View.VISIBLE);
            }else {
                noNotesFound.setVisibility(View.INVISIBLE);
            }

        }else{
            if(notes.size()==0){
                noNotesFound.setText("No results found based on your query");
                noNotesFound.setVisibility(View.VISIBLE);
            }else {
                noNotesFound.setVisibility(View.INVISIBLE);
            }
        }
        recyclerView.setHasFixedSize(true);
        Log.i("listView","LIST:"+listView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(listView?1:2, LinearLayout.VERTICAL));

        adapter = new NotesListAdapter(notes,notesClickListener,MainActivity.this,listView);
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
            selectedNote = new Note();
            selectedNote = note;
            showPopUp(cardView);
        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.long_press_popup_menu);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    database.noteDAO().pin(selectedNote.getID(),false);
                }else{
                    database.noteDAO().pin(selectedNote.getID(),true);
                }
                notes.clear();
                notes.addAll(database.noteDAO().getAllPinned(true));
                notes.addAll(database.noteDAO().getAllNotes(false));
                updateRecyclerView(notes,false);
                return  true;

            case R.id.deleteNote:
                database.noteDAO().delete(selectedNote);
                notes.clear();
                notes.addAll(database.noteDAO().getAllPinned(true));
                notes.addAll(database.noteDAO().getAllNotes(false));
                updateRecyclerView(notes,false);
                return true;
        }
        return false;
    }

}