package fr.romain.assignment3.actors;

import fr.romain.assignment3.shared.Actor;
import fr.romain.assignment3.shared.ActorType;
import fr.romain.assignment3.shared.LecturerId;

public class Lecturer extends Actor {
    private final LecturerId id;

    public Lecturer(LecturerId id) {
        this.id = id;
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public ActorType type() {
        return ActorType.LECTURER;
    }

    public LecturerId getLecturerId() {
        return this.id;
    }
}
