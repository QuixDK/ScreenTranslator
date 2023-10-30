package ru.ssugt.listeners.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StopBroadcastingListener implements ActionListener {

    private final List<Thread> threadList;

    public StopBroadcastingListener(List<Thread> threadList) {
        this.threadList = threadList;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if ( threadList.isEmpty() ) {
            return;
        }
        for ( Thread t: threadList ) {
            if (t != null && t.isAlive()) {
                t.interrupt();
            }
        }
    }
}
