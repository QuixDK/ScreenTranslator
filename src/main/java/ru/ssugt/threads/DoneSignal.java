package ru.ssugt.threads;

import lombok.Getter;

import java.util.concurrent.CountDownLatch;

@Getter
public class DoneSignal {
    public CountDownLatch doneSignal = new CountDownLatch(4);

    public void setDoneSignal(CountDownLatch doneSignal) {
        this.doneSignal = doneSignal;
    }
}
