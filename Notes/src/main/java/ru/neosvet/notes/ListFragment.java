package ru.neosvet.notes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ru.neosvet.notes.list.ListItem;
import ru.neosvet.notes.list.NotesAdapter;
import ru.neosvet.notes.list.NotesHandler;
import ru.neosvet.notes.list.SwipeHelper;
import ru.neosvet.notes.repository.BaseItem;
import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Remover;

public class ListFragment extends Fragment implements NotesHandler {
    private final NotesAdapter adapter = new NotesAdapter(this);
    private final Handler updateAdapter = new Handler(msg -> {
        adapter.notifyDataSetChanged();
        return false;
    });
    private static final String ARG_ID_FOR_REMOVE = "ARG_REM_ID";
    private final int TIME_TO_REMOVE = 3000;
    private RecyclerView recyclerView;
    private Remover remover;

    public static ListFragment newInstance(int id_for_remove) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_FOR_REMOVE, id_for_remove);
        fragment.setArguments(args);
        return fragment;
    }

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            BaseItem note = CurrentBase.get().addNote();
            adapter.addItem(new ListItem(note.getId(), note.getTitle(), note.getDateString()));
            recyclerView.scrollToPosition(0);
            onItemClicked(note.getId());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        loadList();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (remover != null && remover.isStart())
            remover.now();
    }

    private void initList(View view) {
        recyclerView = view.findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);

        final float radius = getResources().getDimension(R.dimen.default_radius);
        SwipeHelper swipeHelper = new SwipeHelper(requireContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        null,
                        getBitmap(R.mipmap.delete),
                        getColor(R.color.purple_200), radius,
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                removeItem(pos);
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        null,
                        getBitmap(R.mipmap.share),
                        getColor(R.color.teal_700), radius,
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                shareItem(pos);
                            }
                        }
                ));
            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);
    }

    private void shareItem(int pos) {
        Toast.makeText(requireContext(), getResources().getString(R.string.share) + pos, Toast.LENGTH_LONG).show();
    }

    public void removeItem(int pos) {
        final ListItem item = adapter.getItem(pos);
        int id = item.getId();
        adapter.removeItem(pos);
        MainActivity main = (MainActivity) getActivity();
        main.removeNoteFragment(id);

        Snackbar snackbar = Snackbar.make(recyclerView, R.string.note_removed, TIME_TO_REMOVE);
        snackbar.setAction(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remover.cancel();
                adapter.restoreItem(item, pos);
                recyclerView.scrollToPosition(pos);
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.teal_200, requireActivity().getTheme()));
        snackbar.show();
        remover = new Remover(TIME_TO_REMOVE, id);
        remover.start();
    }

    private int getColor(int id) {
        return ResourcesCompat.getColor(getResources(), id, null);
    }

    private Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
        //return  ResourcesCompat.getDrawable(getResources(), id, null);
    }

    private void loadList() {
        if (adapter.getItemCount() == 0)
            adapter.addItems(getList(0));
        int id = getArguments().getInt(ARG_ID_FOR_REMOVE, -1);
        if (id > -1) {
            int pos = findPosById(id);
            recyclerView.scrollToPosition(pos);
            removeItem(pos);
            setArguments(new Bundle());
        }
    }

    private ListItem[] getList(int offset) {
        BaseItem[] items = CurrentBase.get().getList(offset, 10);
        if (items == null)
            return null;
        ListItem[] list = new ListItem[items.length];
        for (int i = 0; i < items.length; i++) {
            list[i] = new ListItem(items[i].getId(), items[i].getTitle(), items[i].getDateString());
        }
        return list;
    }

    @Override
    public void onItemClicked(int id) {
        MainActivity main = (MainActivity) getActivity();
        main.openNote(id);
    }

    @Override
    public void updateList(int offset) {
        adapter.addItems(getList(offset));
        updateAdapter.sendEmptyMessage(0);
    }

    public int findPosById(int id) {
        int i = 0;
        while (true) {
            if (adapter.getItem(i).getId() == id) {
                return i;
            }
            i++;
            if (i == adapter.getItemCount()) {
                ListItem[] list = getList(i);
                if (list == null)
                    return -1;
                adapter.addItems(list);
            }
        }
    }
}