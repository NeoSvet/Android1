package ru.neosvet.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.neosvet.notes.list.ListItem;
import ru.neosvet.notes.list.NotesAdapter;
import ru.neosvet.notes.list.NotesHandler;
import ru.neosvet.notes.note.BaseItem;
import ru.neosvet.notes.note.CurrentBase;

public class ListFragment extends Fragment implements NotesHandler {
    private final NotesAdapter adapter = new NotesAdapter(this);
    private final Handler updateAdapter = new Handler(msg -> {
        adapter.notifyDataSetChanged();
        return false;
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.list, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        loadList();
    }

    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadList() {
        adapter.addItems(getList(0));
    }

    private ListItem[] getList(int offset) {
        BaseItem[] items = CurrentBase.get().getList(offset, 10);
        if (items == null)
            return null;
        ListItem[] list = new ListItem[items.length];
        for (int i = 0; i < items.length; i++) {
            list[i] = new ListItem(items[i].getTitle(), items[i].getDateString());
        }
        return list;
    }

    @Override
    public void onItemClicked(int position) {
        MainActivity main = (MainActivity) getActivity();
        main.openNote(position);
    }

    @Override
    public void updateList(int offset) {
        adapter.addItems(getList(offset));
        updateAdapter.sendEmptyMessage(0);
    }
}