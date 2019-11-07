package fr.romain.assignment3.shared;

import fr.romain.assignment3.ClassRoom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static fr.romain.assignment3.shared.ActorState.UP;

public abstract class Actor extends Thread {
    private static int idGenerator = 1;
    protected final int id;
    protected ClassRoom classRoom;
    protected ActorState state;
    protected long hasWaitedFor;

    protected boolean assigned;
    private BlockingQueue<Consumer<Actor>> tasks;

    public Actor() {
        this.id = idGenerator++;
        this.hasWaitedFor = 0;
        this.state = UP;
        this.assigned = false;
        this.tasks = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        while (!super.isInterrupted()) {
            try {
                Consumer<Actor> task = tasks.poll(1, TimeUnit.SECONDS);

                if (task != null)
                    task.accept(this);
            } catch (InterruptedException e) {}
        }
    }

    public abstract ActorType type();

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public void submit(Consumer<Actor>... tasks) throws InterruptedException {
        for (Consumer<Actor> task: tasks)
            this.tasks.put(task);
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public ActorState getActorState() {
        return this.state;
    }

    public void setActorState(ActorState state) {
        this.state = state;
    }

    public long hasWaitedFor() {
        return hasWaitedFor;
    }

    public void setHasWaitedFor(long hasWaitedFor) {
        this.hasWaitedFor = hasWaitedFor;
    }

    public int getActorId() {
        return this.id;
    }

    public synchronized boolean assign() {
        if (assigned) return false;
        return (assigned = true);
    }

    public synchronized void free() {
        this.assigned = false;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Actor && ((Actor) o).id == id;
    }
}
