package fr.romain.assignment3;

import fr.romain.assignment3.actors.Lecturer;
import fr.romain.assignment3.gui.GUI;
import fr.romain.assignment3.shared.Actor;
import fr.romain.assignment3.shared.ArrayWrapper;
import fr.romain.assignment3.shared.ClassRoomId;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static fr.romain.assignment3.shared.ActorState.*;
import static fr.romain.assignment3.shared.ActorType.STUDENT;

public class ClassRoom {
    private final ClassRoomId id;
    private final int capacity;
    private final GUI monitor;
    private Lecturer lecturer;
    private final ArrayWrapper<Actor> actors;
    private boolean inSession;

    private CountDownLatch sitDownLatch;

    private ReentrantLock sitDownLock;
    private Condition sitDownCondition;

    private ReentrantLock enterLeaveLock;
    private Condition enterLeaveCondition;

    private ReentrantLock enterLimitLock;
    private Condition enterLimitCondition;

    public ClassRoom(ClassRoomId id, GUI monitor) {
        this.id = id;
        this.capacity = id.capacity;
        this.monitor = monitor;
        this.actors = new ArrayWrapper<>(capacity);
        this.enterLeaveLock = new ReentrantLock();
        this.enterLeaveCondition = enterLeaveLock.newCondition();
        this.sitDownLock = new ReentrantLock();
        this.sitDownCondition = sitDownLock.newCondition();
        this.enterLimitLock = new ReentrantLock();
        this.enterLimitCondition = enterLimitLock.newCondition();
        inSession = false;
    }

    public void enter(Lecturer lecturer, int delay, TimeUnit unit) throws InterruptedException {
        waitIfInSession(lecturer, true);

        if (delay > 0)
            Thread.sleep(unit.toMillis(delay));

        if (!startSession()) {
            enter(lecturer, delay, unit);
            return;
        }

        this.lecturer = lecturer;
        lecturer.setClassRoom(this);

        monitor.monitor().onEnter(lecturer, -1);
        lecturer.setActorState(UP);
        Log.info(lecturer, "entered - session starts");
        Log.info(lecturer, "counts students - %d students found", actorsInRoom());
        sitDownLatch = new CountDownLatch(actorsInRoom());

        sitDownLock.lock();
        try {
            sitDownCondition.signalAll();
        } finally {
            sitDownLock.unlock();
        }
    }

    public void enter(Lecturer lecturer) throws InterruptedException {
        enter(lecturer, -1, null);
    }

    public void enter(Actor actor) throws InterruptedException {
        waitIfInSession(actor, true);
        waitIfFull(actor);

        if (!addActor(actor))
            enter(actor);
    }

    public void startLecture(Lecturer lecturer) throws InterruptedException {
        sitDownLatch.await();
        Log.info(lecturer, "started lecture");
        lecturer.setActorState(LECTURING);
    }

    public void leave(Lecturer lecturer) {
        monitor.monitor().onLeave(lecturer, -1);

        this.lecturer = null;
        lecturer.setClassRoom(null);
        lecturer.setActorState(UP);

        Log.info(lecturer, "left class room");
        endSession();

        enterLeaveLock.lock();
        try {
            enterLeaveCondition.signalAll();
        } finally {
            enterLeaveLock.unlock();
        }
    }

    public void leave(Actor actor) throws InterruptedException {
        if (actor.type() == STUDENT)
            waitIfInSession(actor, false);

        delActor(actor);
    }

    public void sitDown(Actor actor) throws InterruptedException {
        sitDownLock.lock();
        try {
            sitDownCondition.await();

            actor.setActorState(SITTING);
            Log.info(actor, "sat down");
            monitor.monitor().onSit(actor, indexOf(actor));

            sitDownLatch.countDown();
        } finally {
            sitDownLock.unlock();
        }
    }

    private void waitIfInSession(Actor actor, boolean enter) throws InterruptedException {
        if(!isInSession()) return;

        enterLeaveLock.lock();
        try {
            if (enter && actor.getActorState() != WAITING) {
                actor.setActorState(WAITING);
                monitor.monitor().onWait(actor, this);
            }
            enterLeaveCondition.await();
        } finally {
            enterLeaveLock.unlock();
        }
    }

    private void waitIfFull(Actor actor) throws InterruptedException {
        if (actorsInRoom() < capacity) return;

        enterLimitLock.lock();
        try {
            if (actor.getActorState() != WAITING) {
                actor.setActorState(WAITING);
                monitor.monitor().onWait(actor, this);
            }
            enterLimitCondition.await();
        } finally {
            enterLimitLock.unlock();
        }
    }

    public ClassRoomId getId() {
        return id;
    }

    /** synchronization of the list of actors, inSession **/

    private synchronized int actorsInRoom() {
        return actors.size();
    }

    private synchronized boolean addActor(Actor actor) {
        if (actors.size() < capacity && !inSession) {
            int index = actors.addIndexed(actor);
            actor.setClassRoom(this);

            monitor.monitor().onEnter(actor, index);
            actor.setActorState(UP);
            Log.info(actor, "entered");
            return true;
        } else
            return false;
    }

    private void delActor(Actor actor) {
        synchronized (this) {
            int index = actors.indexOf(actor);
            monitor.monitor().onLeave(actor, index);

            actor.setClassRoom(null);
            actor.setActorState(UP);
            actors.remove(actor);

            Log.info(actor, "left class room");
        }

        enterLimitLock.lock();
        try {
            enterLimitCondition.signal();
        } finally {
            enterLimitLock.unlock();
        }
    }

    private synchronized boolean startSession() {
        if (inSession)
            return false;
        inSession = true;
        return true;
    }

    private synchronized void endSession() {
        inSession = false;
    }

    private synchronized boolean isInSession() {
        return inSession;
    }

    private synchronized int indexOf(Actor actor) {
        return actors.indexOf(actor);
    }
}
