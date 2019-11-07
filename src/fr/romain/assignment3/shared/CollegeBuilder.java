package fr.romain.assignment3.shared;

import fr.romain.assignment3.ClassRoom;
import fr.romain.assignment3.College;
import fr.romain.assignment3.actors.Lecturer;
import fr.romain.assignment3.actors.Student;
import fr.romain.assignment3.actors.Visitor;
import fr.romain.assignment3.gui.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollegeBuilder {
    private List<ClassRoom> rooms;
    private List<Lecturer> lecturers;
    private List<Actor> actors;
    private GUI monitor;

    private CollegeBuilder() {
        this.actors = new ArrayList<>();
    }

    public CollegeBuilder withRooms(ClassRoomId... rooms) {
        this.rooms = Arrays.stream(rooms).map(id -> new ClassRoom(id, monitor)).collect(Collectors.toList());
        return this;
    }

    public CollegeBuilder withStudents(int studentCount) {
        for (int i = 0; i < studentCount; ++i)
            this.actors.add(new Student());
        return this;
    }

    public CollegeBuilder withVisitors(int visitorCount) {
        for (int i = 0; i < visitorCount; ++i)
            this.actors.add(new Visitor());
        return this;
    }

    public CollegeBuilder withLecturers(LecturerId... types) {
        this.lecturers = Arrays.stream(types).map(Lecturer::new).collect(Collectors.toList());
        return this;
    }

    public CollegeBuilder withMonitor(GUI monitor) {
        this.monitor = monitor;
        return this;
    }

    public College createNewCollege() {
        return new College(rooms, lecturers, actors, monitor).start();
    }

    public static CollegeBuilder builder() {
        return new CollegeBuilder();
    }
}
