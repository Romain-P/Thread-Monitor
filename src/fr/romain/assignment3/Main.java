package fr.romain.assignment3;

import fr.romain.assignment3.gui.GUI;
import fr.romain.assignment3.shared.CollegeBuilder;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static fr.romain.assignment3.shared.ActorType.STUDENT;
import static fr.romain.assignment3.shared.ActorType.VISITOR;
import static fr.romain.assignment3.shared.ClassRoomId.*;
import static fr.romain.assignment3.shared.LecturerId.*;

public class Main {
    private static void applyDefaultSystemLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        applyDefaultSystemLook();
        GUI gui = GUI.run(100, TimeUnit.MILLISECONDS); //specify a frame rate

        College griffith = CollegeBuilder.builder()
                .withMonitor(gui)
                .withRooms(W101, W102, W201, W202)
                .withLecturers(OSAMA, BARRY, FAHEEM, ALEX, AQEEL, WASEEM)
                .withStudents(400) /* 400 students */
                .withVisitors(35) /* 10 visitors */
                .createNewCollege();

        TaskHolder tasks = new TaskHolder(griffith);

        launchAssignment(griffith, tasks);
        griffith.await();
    }

    static void launchAssignment(College griffith, TaskHolder tasks) throws InterruptedException {
        OsamaLecture(griffith, tasks);
        AlexLecture(griffith, tasks);
        BarryFaheemLecture(griffith, tasks);
        AqeelWaseemLecture(griffith, tasks);
    }

    static void OsamaLecture(College griffith, TaskHolder tasks) throws InterruptedException {
        griffith.assignToActors(VISITOR, 3,
                tasks.enter(W101),
                tasks.sitDown(),
                tasks.leave()
        );

        griffith.assignToActors(STUDENT, 40,
                tasks.waitFor(400, 1500, TimeUnit.MILLISECONDS),
                tasks.enter(W101),
                tasks.sitDown(),
                tasks.leave()
        );

        griffith.assignToLecturer(OSAMA, 3, /* repeat the tasks 3 times */
                tasks.waitAndEnter(W101, 700, TimeUnit.MILLISECONDS),
                tasks.startLecture(),
                tasks.leave()
        );
    }

    static void AlexLecture(College griffith, TaskHolder tasks) throws InterruptedException {
        griffith.assignToActors(STUDENT, 120,
                tasks.enter(W201),
                tasks.sitDown(),
                tasks.leave()
        );

        griffith.assignToLecturer(ALEX, 2, /* repeat the tasks 2 times */
                tasks.waitAndEnter(W201, 2, TimeUnit.SECONDS),
                tasks.startLecture(),
                tasks.leave()
        );
    }


    static void BarryFaheemLecture(College griffith, TaskHolder tasks) throws InterruptedException {
        griffith.assignToActors(STUDENT, 60,
                tasks.enter(W102),
                tasks.sitDown(),
                tasks.leave()
        );

        griffith.assignToLecturer(BARRY, 1, /* repeat the tasks 1 time */
                tasks.waitAndEnter(W102, 2, TimeUnit.SECONDS),
                tasks.startLecture(),
                tasks.leave()
        );

        griffith.assignToLecturer(FAHEEM, 1, /* repeat the tasks 1 time */
                tasks.waitAndEnter(W102, 2, TimeUnit.SECONDS),
                tasks.startLecture(),
                tasks.leave()
        );
    }


    static void AqeelWaseemLecture(College griffith, TaskHolder tasks) throws InterruptedException {
        griffith.assignToActors(STUDENT, 76,
                tasks.enter(W202),
                tasks.sitDown(),
                tasks.leave()
        );

        griffith.assignToLecturer(AQEEL, 1, /* repeat the tasks 1 time */
                tasks.waitAndEnter(W202, 2, TimeUnit.SECONDS),
                tasks.startLecture(),
                tasks.leave()
        );

        griffith.assignToLecturer(WASEEM, 1, /* repeat the tasks 1 time */
                tasks.waitAndEnter(W202, 2, TimeUnit.SECONDS),
                tasks.startLecture(),
                tasks.leave()
        );
    }
}
