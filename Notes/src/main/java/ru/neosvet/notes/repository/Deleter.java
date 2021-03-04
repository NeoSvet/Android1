package ru.neosvet.notes.repository;

public class Deleter {
    private final int delay;
    private final int id;
    private Thread timer;
    private boolean isStart = false;

    public Deleter(int delay, int id) {
        this.delay = delay;
        this.id = id;
    }

    public void start() {
        timer = new Thread(() -> {
            try {
                Thread.sleep(delay);
                CurrentBase.get().removeNote(id);
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
        CurrentBase.get().removeNote(id);
    }

    public boolean isStart() {
        return isStart;
    }
}
