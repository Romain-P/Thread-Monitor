package fr.romain.assignment3.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class GUI extends JFrame {
    private final Monitor monitor;

    public GUI(int frameRate) {
        setResizable(false);
        setTitle("Assignment3 - Romain PILLOT - Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 955, 815);

        this.monitor = new Monitor(frameRate);
        setContentPane(this.monitor);
    }

    public Monitor monitor() {
        return this.monitor;
    }

    public static GUI run(int frameRate, TimeUnit unit) throws InvocationTargetException, InterruptedException {
        AtomicReference<GUI> frame = new AtomicReference<>();
        EventQueue.invokeAndWait(() -> {
            try {
                frame.set(new GUI((int) unit.toMillis(frameRate)));
                frame.get().setVisible(true);
                frame.get().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        frame.get().monitor.worker().start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return frame.get();
    }
}
