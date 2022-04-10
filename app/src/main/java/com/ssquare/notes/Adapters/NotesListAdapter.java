package com.ssquare.notes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ssquare.notes.Models.Note;
import com.ssquare.notes.NotesClickListener;
import com.ssquare.notes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    List<Note> notes;
    NotesClickListener notesClickListener;
    Context context;

    public NotesListAdapter(List<Note> notes, NotesClickListener notesClickListener, Context context) {
        this.notes = notes;
        this.notesClickListener = notesClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.single_note,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.title_TV.setText(notes.get(position).getTitle());
        holder.title_TV.setSelected(true);

        holder.notes_TV.setText(notes.get(position).getNotes());

        holder.date_TV.setText(notes.get(position).getDate());
        holder.date_TV.setSelected(true);

        if(notes.get(position).isPinned()){
            holder.pin_IV.setImageResource(R.drawable.ic_baseline_push_pin_24);
        }else{
            holder.pin_IV.setImageResource(0);
        }

        holder.title_TV.setText(notes.get(position).getTitle());

        int color_code = getRandomColor();
        holder.notes_cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code,null));

        holder.notes_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesClickListener.onPress(notes.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notesClickListener.onLongPress(notes.get(holder.getAdapterPosition()), holder.notes_cardView);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add (R.color.color1);
        colorCode.add (R.color.color2);
        colorCode.add (R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add (R.color.color5);
        Random random = new Random();
        int random_color = random.nextInt (colorCode.size());
        return random_color;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


}

class NotesViewHolder extends RecyclerView.ViewHolder {
    CardView notes_cardView;
    TextView title_TV,notes_TV,date_TV;
    ImageView pin_IV;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_cardView = itemView.findViewById(R.id.note_cardView);
        title_TV = itemView.findViewById(R.id.title_TV);
        notes_TV = itemView.findViewById(R.id.notes_TV);
        date_TV = itemView.findViewById(R.id.date_TV);
        pin_IV = itemView.findViewById(R.id.pin_IV);
    }
}