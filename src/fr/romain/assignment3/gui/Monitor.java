package fr.romain.assignment3.gui;

import fr.romain.assignment3.ClassRoom;
import fr.romain.assignment3.actors.Lecturer;
import fr.romain.assignment3.gui.components.RoomMonitorPanel;
import fr.romain.assignment3.shared.Actor;
import fr.romain.assignment3.shared.ClassRoomId;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static fr.romain.assignment3.shared.ActorState.WAITING;
import static fr.romain.assignment3.shared.ActorType.STUDENT;

public class Monitor extends JPanel {
    private final RoomMonitorPanel w101;
    private final RoomMonitorPanel w102;
    private final RoomMonitorPanel w201;
    private final RoomMonitorPanel w202;
    private final HashMap<ClassRoomId, BlockingQueue<Runnable>> tasks;
    private final Timer worker;
    private final int frameRate;

    public Monitor(int frameRate) {
        this.frameRate = frameRate;
        this.tasks = new HashMap<>();
        for (ClassRoomId id : ClassRoomId.values())
            tasks.put(id, new LinkedBlockingQueue<>());
        this.worker = pollEvents();

        setLayout(null);
        w101 = new RoomMonitorPanel("W101", 10, 11, 406, 371, 20);
        add(w101);
        w201 = new RoomMonitorPanel("W201", 523, 11, 406, 371, 60);
        add(w201);
        w102 = new RoomMonitorPanel("W102", 10, 403, 406, 371, 30);
        add(w102);
        w202 = new RoomMonitorPanel("W202", 523, 403, 406, 371, 60);
        add(w202);
    }

    public void onEnter(Actor actor, int place) {
        final ClassRoomId id = actor.getClassRoom().getId();
        final RoomMonitorPanel monitor = panel(id);
        final boolean hasWaited = actor.getActorState() == WAITING;

        postEvent(id, () -> {
            switch (actor.type()) {
                case STUDENT:
                    if (hasWaited)
                        monitor.delWaitingStudent();

                    monitor.room().setColor(place, Color.ORANGE);
                    monitor.addStudent();
                    break;
                case VISITOR:
                    if (hasWaited)
                        monitor.delWaitingVisitor();

                    monitor.room().setColor(place, Color.ORANGE);
                    monitor.addVisitor();
                    break;
                case LECTURER:
                    if (hasWaited)
                        monitor.delWaitingLecturer();

                    monitor.setLecturer(((Lecturer) actor).getLecturerId().name());
                    break;
            }
            monitor.room().repaint();
        });
    }

    public void onLeave(Actor actor, int place) {
        final ClassRoomId id = actor.getClassRoom().getId();
        final RoomMonitorPanel monitor = panel(id);

        postEvent(id, () -> {

            switch (actor.type()) {
                case STUDENT:
                    monitor.room().setColor(place, Color.GREEN);
                    monitor.delStudent();
                    break;
                case VISITOR:
                    monitor.room().setColor(place, Color.GREEN);
                    monitor.delVisitor();
                    break;
                case LECTURER:
                    monitor.removeLecturer();
                    break;
            }
            monitor.room().repaint();
        });
    }

    public void onSit(Actor actor, int place) {
        final ClassRoomId id = actor.getClassRoom().getId();
        final RoomMonitorPanel monitor = panel(id);

        postEvent(id, () -> {

            if (actor.type() == STUDENT)
                monitor.room().setColor(place, Color.RED);
            else
                monitor.room().setColor(place, Color.RED);
            monitor.room().repaint();
        });
    }

    public void onWait(Actor actor, ClassRoom room) {
        final ClassRoomId id = room.getId();
        final RoomMonitorPanel monitor = panel(id);

        postEvent(id, () -> {
            switch (actor.type()) {
                case LECTURER:
                    monitor.addWaitingLecturer();
                    break;
                case STUDENT:
                    monitor.addWaitingStudent();
                    break;
                case VISITOR:
                    monitor.addWaitingVisitor();
                    break;
            }
        });
    }

    private RoomMonitorPanel panel(ClassRoomId id) {
        switch (id) {
            case W201:
                return w201;
            case W202:
                return w202;
            case W101:
                return w101;
            case W102:
                return w102;
            default:
                return null;
        }
    }

    private Timer pollEvents() {
        Timer worker = new Timer(frameRate, (event) -> {
            for (BlockingQueue<Runnable> tasks : this.tasks.values()) {
                Runnable task = tasks.poll();

                if (task == null) continue;
                task.run();
            }
        });

        worker.setRepeats(true);
        return worker;
    }

    private void postEvent(ClassRoomId id, Runnable task) {
        try {
            this.tasks.get(id).put(task);
        } catch (Exception e) {
        }
    }

    public Timer worker() {
        return this.worker;
    }
}
