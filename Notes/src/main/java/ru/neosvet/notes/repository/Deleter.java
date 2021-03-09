package ru.neosvet.notes.repository;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;

import ru.neosvet.notes.R;

public class Deleter {
    private final int noteId;
    private Thread timer;
    private boolean isStart = false;
    private final Handler doDelete = new Handler(msg -> {
        CurrentBase.get().deleteNote(msg.what);
        return false;
    });

    public Deleter(int noteId) {
        this.noteId = noteId;
    }

    public void deleteAfterMills(int mills) {
        timer = new Thread(() -> {
            try {
                Thread.sleep(mills);
                doDelete.sendEmptyMessage(noteId);
            } catch (InterruptedException e) {
            }
            isStart = false;
        });
        timer.start();
        isStart = true;
    }

    public void abortDelayedDelete() {
        isStart = false;
        timer.interrupt();
    }

    public void deleteNow() {
        if (!isStart)
            return;
        abortDelayedDelete();
        CurrentBase.get().deleteNote(noteId);
    }

    public void deleteByDialog(Context context, String noteTitle) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.ask_delete)
                .setMessage(noteTitle)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CurrentBase.get().deleteNote(noteId);
                            }
                        })
                .create().show();
    }

    public boolean equalsId(int id) {
        return noteId == id;
    }
}
