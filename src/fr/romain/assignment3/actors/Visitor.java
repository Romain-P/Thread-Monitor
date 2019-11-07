package fr.romain.assignment3.actors;

import fr.romain.assignment3.shared.Actor;
import fr.romain.assignment3.shared.ActorType;

public class Visitor extends Actor {
    @Override
    public void run() {
        super.run();
    }

    @Override
    public ActorType type() {
        return ActorType.VISITOR;
    }
}
