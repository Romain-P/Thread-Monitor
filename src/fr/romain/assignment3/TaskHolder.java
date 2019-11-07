package fr.romain.assignment3;

import fr.romain.assignment3.actors.Lecturer;
import fr.romain.assignment3.shared.Actor;
import fr.romain.assignment3.shared.ClassRoomId;
import fr.romain.assignment3.shared.Consumer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static fr.romain.assignment3.shared.ActorType.LECTURER;

public final class TaskHolder {
    private final College college;
    
    public TaskHolder(College college) {
        this.college = college;
    }

    public Consumer<Actor> enter(ClassRoomId id) {
        return (actor) -> {
            if (actor.type() == LECTURER)
                college.getRooms().get(id).enter((Lecturer) actor);
            else
                college.getRooms().get(id).enter(actor);
        };
    }

    public Consumer<Actor> waitAndEnter(ClassRoomId id, int delay, TimeUnit unit) {
        return (actor) -> {
            if (actor.type() == LECTURER)
                college.getRooms().get(id).enter((Lecturer) actor, delay, unit);
        };
    }

    public Consumer<Actor> leave() {
        return (actor) ->  {
            if (actor.type() == LECTURER)
                actor.getClassRoom().leave((Lecturer) actor);
            else
                actor.getClassRoom().leave(actor);
        };
    }

    public Consumer<Actor> sitDown() {
        return (actor) -> actor.getClassRoom().sitDown(actor);
    }

    public Consumer<Actor> startLecture() {
        return (lecturer) -> lecturer.getClassRoom().startLecture((Lecturer) lecturer);
    }

    public Consumer<Actor> waitFor(long time, TimeUnit unit) {
        return (actor) -> {
            long delay = unit.toMillis(time);
            Thread.sleep(delay);
            actor.setHasWaitedFor(delay);
        };
    }

    public Consumer<Actor> waitFor(int min, int max, TimeUnit unit) {
        return (actor) -> {
            int minMillis = (int) unit.toMillis(min);
            int maxMillis = (int) unit.toMillis(max);
            int delay = new Random().nextInt(maxMillis - minMillis) + minMillis;

            Thread.sleep(delay);
            actor.setHasWaitedFor(delay);
        };
    }
}
