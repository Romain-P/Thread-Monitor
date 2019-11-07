package fr.romain.assignment3;

import fr.romain.assignment3.actors.Lecturer;
import fr.romain.assignment3.shared.Actor;

import static fr.romain.assignment3.shared.ActorType.LECTURER;

public class Log {
    public static synchronized void info(Actor actor, String msg, Object... args) {
        String actorType = actor.type().name();
        String roomId = actor.getClassRoom() == null ? "CORRIDOR" : actor.getClassRoom().getId().name();

        String delay = "";
        if (actor.hasWaitedFor() != 0) {
            delay = actor.hasWaitedFor() == 0 ? "" : "- is " + actor.hasWaitedFor() + " ms late";
            actor.setHasWaitedFor(0);
        }

        String actorId;
        if (actor.type() == LECTURER)
            actorId = ((Lecturer) actor).getLecturerId().name();
        else
            actorId = String.valueOf(actor.getActorId());

        System.out.println(String.format("[%s %s - %s]: %s %s", actorType, actorId, roomId, String.format(msg, args), delay));
    }
}
