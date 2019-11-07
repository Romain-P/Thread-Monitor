package fr.romain.assignment3;

import fr.romain.assignment3.actors.Lecturer;
import fr.romain.assignment3.gui.GUI;
import fr.romain.assignment3.shared.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class College {
    private final Map<ClassRoomId, ClassRoom> rooms;
    private final Map<LecturerId, Lecturer> lecturers;
    private final List<Actor> actors;
    private final GUI monitor;

    public College(List<ClassRoom> rooms, List<Lecturer> lecturers, List<Actor> actors, GUI monitor) {
        this.rooms = rooms.stream().collect(Collectors.toMap(ClassRoom::getId, room -> room));
        this.lecturers = lecturers.stream().collect(Collectors.toMap(Lecturer::getLecturerId, lecturer -> lecturer));;
        this.actors = actors;
        this.monitor = monitor;
    }

    public College start() {
        for (Lecturer lecturer: lecturers.values())
            lecturer.start();
        for (Actor actor: actors)
            actor.start();
        return this;
    }

    public College stop() {
        for (Lecturer lecturer: lecturers.values())
            lecturer.interrupt();
        for (Actor actor: actors)
            actor.interrupt();
        return this;
    }

    public void await() throws InterruptedException {
        for (Lecturer lecturer: lecturers.values())
            lecturer.join();
        for (Actor actor: actors)
            actor.join();
    }

    public void assignToLecturer(LecturerId id, int repeat, Consumer<Actor>... tasks) throws InterruptedException {
        for (int i=0; i < repeat; ++i)
            lecturers.get(id).submit(tasks);
    }

    public void assignToActors(ActorType type, int actorsCount, Consumer<Actor>... tasks) throws InterruptedException {
        List<Actor> actors = this.actors.stream()
                .filter(actor -> actor.type() == type)
                .filter(Actor::assign) /* takes actors not already assigned **/
                .limit(actorsCount)
                .collect(Collectors.toList());

        for (Actor actor: actors) {
            actor.submit(tasks);
            actor.submit(Actor::free);
        }
    }

    public Map<ClassRoomId, ClassRoom> getRooms() {
        return rooms;
    }

    public Map<LecturerId, Lecturer> getLecturers() {
        return lecturers;
    }

    public List<Actor> getActors() {
        return actors;
    }
}
