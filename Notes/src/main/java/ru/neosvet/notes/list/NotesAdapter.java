package ru.neosvet.notes.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import ru.neosvet.notes.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private String[] notes;
    private final NotesClicker clicker;

    public NotesAdapter(NotesClicker clicker) {
        this.clicker = clicker;
    }

    public void setItems(String[] items) {
        notes = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.onBind(notes[position]);
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView textView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_title);
        }

        public void onBind(String title) {
            textView.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        clicker.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}
