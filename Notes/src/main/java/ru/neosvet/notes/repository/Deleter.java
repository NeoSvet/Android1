package ru.neosvet.notes.repository;

import android.os.Handler;

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
        if (isStart)
            abortDelayedDelete();
        CurrentBase.get().deleteNote(noteId);
    }

    public boolean isStart() {
        return isStart;
    }
}
