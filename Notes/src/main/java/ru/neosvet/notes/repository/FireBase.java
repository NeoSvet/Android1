package ru.neosvet.notes.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBase implements Base, OnFailureListener {
    public static final String TABLE = "notes", COL_ID = "id", COL_TITLE = "title",
            COL_DATE = "date", COL_DES = "description";
    private FirebaseFirestore base;
    private BaseCallbacks callbacks;
    private int last_id = -1;
    private DocumentSnapshot lastDoc = null;
    private List<Note> notes = new ArrayList<>();
    private boolean isBusy = false;

    @Override
    public void open(BaseCallbacks callbacks) {
        this.callbacks = callbacks;
        base = FirebaseFirestore.getInstance();
        if (last_id == -1)
            loadLastId();
        else
            loadNextPage();
    }

    private void loadLastId() {
        if (checkBusy())
            return;
        base.collection(TABLE)
                .orderBy(COL_ID, Query.Direction.DESCENDING)
                .limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot docs) {
                        isBusy = false;
                        if (docs.size() == 0) {
                            callbacks.listIsReady();
                            return;
                        }
                        long id = (long) docs.getDocuments().get(0).get(COL_ID);
                        last_id = (int) id;
                        loadNextPage();
                    }
                })
                .addOnFailureListener(this);
    }

    private boolean checkBusy() {
        if (isBusy)
            return true;
        isBusy = true;
        callbacks.startProgress();
        return false;
    }

    @Override
    public void loadNextPage() {
        if (checkBusy())
            return;
        Query load;
        if (lastDoc == null)
            load = base.collection(TABLE);
        else
            load = base.collection(TABLE).startAfter(lastDoc);

        load.limit(LIMIT).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot response) {
                        isBusy = false;
                        List<DocumentSnapshot> docs = response.getDocuments();
                        if (docs.size() == 0) {
                            callbacks.listIsReady();
                            return;
                        }
                        lastDoc = docs.get(docs.size() - 1);
                        List<Note> items = response.toObjects(Note.class);
                        notes.addAll(items);
                        callbacks.listIsReady();
                    }
                })
                .addOnFailureListener(this);
    }

    @Override
    public Note[] getList(int offset) {
        int limit = LIMIT;
        if (offset + limit > notes.size())
            limit = notes.size() - offset;
        if (limit < 1)
            return null;
        Note[] m = new Note[limit];
        for (int i = 0; i < limit; i++) {
            m[i] = notes.get(i + offset);
        }
        return m;
    }

    @Override
    public Note getNote(int id) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == id)
                return notes.get(i);
        }
        return null;
    }

    @Override
    public void deleteNote(final int id) {
        if (checkBusy())
            return;
        base.collection(TABLE)
                .document(String.valueOf(id))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for (int i = 0; i < notes.size(); i++) {
                            if (notes.get(i).getId() == id) {
                                notes.remove(i);
                                break;
                            }
                        }
                        isBusy = false;
                        callbacks.deleteNote(id);
                    }
                })
                .addOnFailureListener(this);
    }

    @Override
    public void pushNote(final Note note) {
        if (checkBusy())
            return;
        final Map<String, Object> map = getMapFrom(note);
        base.collection(TABLE)
                .document(String.valueOf(note.getId()))
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isBusy = false;
                        callbacks.updateNote(note);
                    }
                })
                .addOnFailureListener(this);
    }

    private Map<String, Object> getMapFrom(Note note) {
        final Map<String, Object> map = new HashMap<>();
        map.put(COL_ID, note.getId());
        map.put(COL_TITLE, note.getTitle());
        map.put(COL_DATE, note.getDate());
        map.put(COL_DES, note.getDescription());
        return map;
    }

    @Override
    public void addNote() {
        if (checkBusy())
            return;
        last_id++;
        Note note = new Note(last_id, "title #" + last_id,
                System.currentTimeMillis(), "description #" + last_id);
        final Map<String, Object> map = getMapFrom(note);

        base.collection(TABLE)
                .document(String.valueOf(last_id))
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isBusy = false;
                        notes.add(note);
                        callbacks.addNote(note);
                    }
                })
                .addOnFailureListener(this);
    }

    public void onFailure(@NonNull Exception e) {
        isBusy = false;
        callbacks.onError(e.getMessage());
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
