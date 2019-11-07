package fr.romain.assignment3.actors;

import fr.romain.assignment3.shared.ActorType;

public class Student extends Visitor {
    @Override
    public void run() {
        super.run();
    }

    @Override
    public ActorType type() {
        return ActorType.STUDENT;
    }
}
