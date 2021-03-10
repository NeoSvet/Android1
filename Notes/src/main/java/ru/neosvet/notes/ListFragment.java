package ru.neosvet.notes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import ru.neosvet.notes.list.ListCallbacks;
import ru.neosvet.notes.list.ListItem;
import ru.neosvet.notes.list.NotesAdapter;
import ru.neosvet.notes.list.SwipeHelper;
import ru.neosvet.notes.observer.ObserverNote;
import ru.neosvet.notes.observer.PublisherNote;
import ru.neosvet.notes.repository.BaseItem;
import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Deleter;

public class ListFragment extends Fragment implements ListCallbacks, ObserverNote {
    private final NotesAdapter adapter = new NotesAdapter(this);
    private final int TIME_TO_DELETE = 3000;
    private RecyclerView recyclerView;
    private Deleter deleter;
    private Snackbar snackbar;

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
            CurrentBase.get().addNote();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNote(@NonNull BaseItem note) {
        adapter.addItem(new ListItem(note.getId(), note.getTitle(), note.getDateString()));
        recyclerView.scrollToPosition(0);
        onItemClicked(note.getId());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        if (adapter.getItemCount() == 0)
            CurrentBase.get().loadNextPage();
        else
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        PublisherNote.queryChanges(this);
        PublisherNote.subscribe(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        PublisherNote.unsubscribe(this);
        if (deleter != null)
            deleter.deleteNow();
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
                                if (deleter != null) {
                                    deleter.deleteNow();
                                    snackbar.dismiss();
                                }
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

    private void removeItem(int pos) {
        final ListItem item = adapter.getItem(pos);
        int id = item.getId();
        adapter.removeItem(pos);

        snackbar = Snackbar.make(recyclerView, R.string.note_deleted, TIME_TO_DELETE);
        snackbar.setAction(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublisherNote.cancelDelete(id);
                deleter.abortDelayedDelete();
                adapter.restoreItem(item, pos);
                recyclerView.scrollToPosition(pos);
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.teal_200, requireActivity().getTheme()));
        snackbar.show();
        deleter = new Deleter(id);
        deleter.deleteAfterMills(TIME_TO_DELETE);
    }

    private int getColor(int id) {
        return ResourcesCompat.getColor(getResources(), id, null);
    }

    private Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
        //return  ResourcesCompat.getDrawable(getResources(), id, null);
    }

    public void refreshList() {
        adapter.addItems(getList(adapter.getItemCount()));
        adapter.notifyDataSetChanged();
    }

    private ListItem[] getList(int offset) {
        BaseItem[] items = CurrentBase.get().getList(offset);
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
    public void onLongItemClicked(int id) {
        RenameFragment.create(id).show(getChildFragmentManager(), null);
    }

    @Override
    public void updateList() {
        CurrentBase.get().loadNextPage();
    }

    private int findPosById(int id) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).getId() == id)
                return i;
        }
        return -1;
    }

    @Override
    public void updateNote(BaseItem note) {
        int pos = findPosById(note.getId());
        if (pos == -1)
            return;
        adapter.getItem(pos).setTitle(note.getTitle());
        adapter.getItem(pos).setSubtitle(note.getDateString());
        adapter.notifyItemChanged(pos);
    }

    @Override
    public void deletedNote(int id) {
        int pos = findPosById(id);
        if (pos > -1)
            adapter.removeItem(pos);
        if (snackbar != null && snackbar.isShown() && deleter.equalsId(id))
            snackbar.dismiss();
    }
}