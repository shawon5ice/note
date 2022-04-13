package com.ssquare.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssquare.notes.Models.Note;
import com.ssquare.notes.databinding.ActivityAddNoteBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    private ActivityAddNoteBinding binding;

    EditText ET_title,ET_notes;
    Date date = new Date();
    ImageView save,back_IV;
    TextView displayChars;
    Note note;
    boolean isOldNote = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        DateFormat formatter = new SimpleDateFormat("d MMMM hh:mm");

        String displayDate = formatter.format(date);


        displayChars = view.findViewById(R.id.displayChars);
        save = view.findViewById(R.id.save_IV);
        back_IV = view.findViewById(R.id.back_IV);
        ET_title = view.findViewById(R.id.editText_title);
        ET_notes = view.findViewById(R.id.editText_notes);

        displayChars.setText(displayDate+" | 0 characters");
        note = new Note();

        try {
            note = (Note) getIntent().getSerializableExtra("old_note");
            ET_title.setText(note.getTitle());
            ET_notes.setText(note.getNotes());
            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        ET_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = ET_title.getText().toString().replace(" ", "").length()+ET_notes.getText().toString().replace(" ", "").length();
                displayChars.setText(displayDate+" | "+length+" characters");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ET_notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = ET_title.getText().toString().replace(" ", "").length()+ET_notes.getText().toString().replace(" ", "").length();
                displayChars.setText(displayDate+" | "+length+" characters");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        String title = ET_title.getText().toString();
        String notes = ET_notes.getText().toString();

        if(title.isEmpty()){
            Toast.makeText(AddNoteActivity.this,"Please add a title",Toast.LENGTH_SHORT).show();
            return;
        }
        if(notes.isEmpty()){
            Toast.makeText(AddNoteActivity.this,"Please add some text",Toast.LENGTH_SHORT).show();
            return;
        }


        if(!isOldNote){
            note = new Note();
        }

        note.setTitle(title);
        note.setNotes(notes);
        DateFormat formatter = new SimpleDateFormat("d MMM yyyy hh:mm a");
        note.setDate(formatter.format(date));

        Intent intent = new Intent();
        intent.putExtra("note",note);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData();
    }
}