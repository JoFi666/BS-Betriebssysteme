package abgabe03;

import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Verladestation implements Comparable<Verladestation>{
    private static int idCounter = 1;
    private final int id;
    private final Semaphore semaphore;
    private final Lock lock;
    private int counter;

    public Verladestation() {
        this.id = idCounter++;
        this.semaphore = new Semaphore(1); //Nur ein LKW gleichzeitig
        this.lock = new ReentrantLock();
        this.counter = 0;
    }

    public void incrementCounter() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

    public int getId() {
        return id;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public Lock getLock() {
        return lock;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public int compareTo(Verladestation o) {
        return o.getCounter() - this.getCounter();
    }


}
