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
    private List<ListItem> data = new ArrayList<>();
    private final ListCallbacks callbacks;

    public NotesAdapter(ListCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void addItem(ListItem item) {
        data.add(item);
    }

    public void insertItem(ListItem item) {
        data.add(0, item);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ListItem getItem(int pos) {
        return data.get(pos);
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ListItem item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
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
                        callbacks.onItemClicked(data.get(getAdapterPosition()).getId());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        callbacks.onLongItemClicked(data.get(getAdapterPosition()).getId());
                    }
                    return true;
                }
            });
        }
    }
}
