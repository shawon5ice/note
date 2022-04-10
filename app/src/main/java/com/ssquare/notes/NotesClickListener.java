package com.ssquare.notes;

import androidx.cardview.widget.CardView;

import com.ssquare.notes.Models.Note;

public interface NotesClickListener {
    void onPress(Note note);
    void onLongPress(Note note, CardView cardView);
}
