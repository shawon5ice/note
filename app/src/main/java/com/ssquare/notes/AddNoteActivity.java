package com.ssquare.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ssquare.notes.Models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    EditText ET_title,ET_notes;
    ImageView save;
    Note note;
    boolean isOldNote = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        save = findViewById(R.id.save_IV);
        ET_title = findViewById(R.id.editText_title);
        ET_notes = findViewById(R.id.editText_notes);

        note = new Note();

        try {
            note = (Note) getIntent().getSerializableExtra("old_note");
            ET_title.setText(note.getTitle());
            ET_notes.setText(note.getNotes());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ET_title.getText().toString();
                String notes = ET_notes.getText().toString();

                if(notes.isEmpty()){
                    Toast.makeText(AddNoteActivity.this,"Please add some text",Toast.LENGTH_SHORT).show();
                    return;
                }
                DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if(!isOldNote){
                    note = new Note();
                }

                note.setTitle(title);
                note.setNotes(notes);
                note.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note",note);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}