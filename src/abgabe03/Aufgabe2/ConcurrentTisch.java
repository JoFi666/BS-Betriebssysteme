package abgabe03.Aufgabe2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentTisch extends AllgemeinerTisch{
    private final Lock lock;
    private final Condition spielerCondition;
    private final Condition schiedsrichterCondition;


    public ConcurrentTisch() {
        super();
        this.lock = new ReentrantLock();
        this.spielerCondition = lock.newCondition();
        this.schiedsrichterCondition = lock.newCondition();
    }

    @Override
    public void doZug(Spieler spieler, String zug) throws InterruptedException {
        lock.lock();
        try {
            if (spieler.equals(this.spieler1)) {
                while (spieler1Zug != null) {
                    spielerCondition.await();
                }
                spieler1Zug = zug;
            } else if (spieler.equals(this.spieler2)) {
                while (spieler2Zug != null) {
                    spielerCondition.await();
                }
                spieler2Zug = zug;
            }

            if (spieler1Zug != null && spieler2Zug != null) {
                schiedsrichterCondition.signal();
            } else {
                spielerCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void zugAuswerten() throws InterruptedException {
        lock.lock();
        try {
            while (spieler1Zug == null || spieler2Zug == null) {
                schiedsrichterCondition.await();
            }

            rundenZahl++;
            if (spieler1Zug.equals(spieler2Zug)) {
                unentschiedenZahl++;
            } else if ((spieler1Zug.equals("Schere") && spieler2Zug.equals("Papier")) ||
                    (spieler1Zug.equals("Stein") && spieler2Zug.equals("Schere")) ||
                    (spieler1Zug.equals("Papier") && spieler2Zug.equals("Stein"))) {
                gewinneSpieler1++;
            } else {
                gewinneSpieler2++;
            }

            spieler1Zug = null;
            spieler2Zug = null;
            spielerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }


}
