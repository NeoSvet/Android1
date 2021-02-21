package ru.neosvet.notes.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import ru.neosvet.notes.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<ListItem> notes = new ArrayList<>();
    private final NotesHandler handler;
    private boolean isFinish = false;

    public NotesAdapter(NotesHandler handler) {
        this.handler = handler;
    }

    public void addItems(ListItem[] items) {
        if (items == null) {
            isFinish = true;
            return;
        }
        for (int i = 0; i < items.length; i++) {
            notes.add(items[i]);
        }
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.onBind(notes.get(position));
        if (!isFinish && position == notes.size() - 1)
            handler.updateList(notes.size());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView tvTitle, tvSubtitle;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_title);
            tvSubtitle = itemView.findViewById(R.id.item_subtitle);
        }

        public void onBind(ListItem item) {
            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSubtitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        handler.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}