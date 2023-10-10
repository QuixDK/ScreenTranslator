package ru.ssugt;

import java.util.concurrent.CountDownLatch;

public class DoneSignal {
    public CountDownLatch doneSignal = new CountDownLatch(4);

    public CountDownLatch getDoneSignal() {
        return doneSignal;
    }
    public void setDoneSignal(CountDownLatch doneSignal) {
        this.doneSignal = doneSignal;
    }
}
