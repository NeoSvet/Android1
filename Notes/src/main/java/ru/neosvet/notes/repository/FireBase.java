package ru.neosvet.notes.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FireBase implements Base, OnFailureListener {
    public static final String TABLE = "notes", COL_ID = "id", COL_TITLE = "title",
            COL_DATE = "date", COL_DES = "description";
    private FirebaseFirestore base;
    private BaseCallbacks callbacks;
    private boolean isBusy = false;

    @Override
    public void open(BaseCallbacks callbacks) {
        this.callbacks = callbacks;
        base = FirebaseFirestore.getInstance();
    }

    private boolean checkBusy() {
        if (isBusy)
            return true;
        isBusy = true;
        callbacks.startProgress();
        return false;
    }

    @Override
    public void requestList() {
        if (checkBusy())
            return;
        base.collection(TABLE).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot response) {
                        isBusy = false;
                        List<DocumentSnapshot> docs = response.getDocuments();
                        if (docs.size() == 0) {
                            callbacks.putList(null);
                            return;
                        }
                        List<Note> items = response.toObjects(Note.class);
                        callbacks.putList(items);
                    }
                })
                .addOnFailureListener(this);
    }

    @Override
    public void requestNote(String id) {
        if (checkBusy())
            return;
        base.collection(TABLE).limit(1).whereEqualTo(COL_ID, id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot response) {
                        isBusy = false;
                        List<DocumentSnapshot> docs = response.getDocuments();
                        if (docs.size() == 0) {
                            callbacks.putNote(null);
                            return;
                        }
                        Note note = response.getDocuments().get(0).toObject(Note.class);
                        callbacks.putNote(note);
                    }
                })
                .addOnFailureListener(this);
    }

    @Override
    public void deleteNote(final String id) {
        if (checkBusy())
            return;
        base.collection(TABLE)
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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
                .document(note.getId())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isBusy = false;
                        callbacks.putNote(note);
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
        String id = UUID.randomUUID().toString();
        Note note = new Note(id, "New note",
                System.currentTimeMillis(), "");
        final Map<String, Object> map = getMapFrom(note);

        base.collection(TABLE)
                .document(id)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isBusy = false;
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
