package ru.neosvet.notes.repository;

import android.os.Handler;

public class Deleter {
    private final int delay;
    private final int id;
    private Thread timer;
    private boolean isStart = false;
    private final Handler doDelete = new Handler(msg -> {
        CurrentBase.get().deleteNote(msg.what);
        return false;
    });

    public Deleter(int delay, int id) {
        this.delay = delay;
        this.id = id;
    }

    public void start() {
        timer = new Thread(() -> {
            try {
                Thread.sleep(delay);
                doDelete.sendEmptyMessage(id);
            } catch (InterruptedException e) {
            }
            isStart = false;
        });
        timer.start();
        isStart = true;
    }

    public void cancel() {
        isStart = false;
        timer.interrupt();
    }

    public void now() {
        cancel();
        CurrentBase.get().deleteNote(id);
    }

    public boolean isStart() {
        return isStart;
    }
}
