package abgabe03.Aufgabe1_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lkw extends Thread {
    private final List<Verladestation> verladestationList;
    private final Random random;

    public Lkw(List<Verladestation> verladestationList) {
        this.verladestationList = verladestationList;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            //Start nach Zufallszeit (0-3s)
            Thread.sleep(random.nextInt(3000));

            while (!Thread.currentThread().isInterrupted()) {
                Verladestation station = findeVerladestation();

                if(station != null) {
                    //Holt sich die Berechtigung für die Verladestation.
                    station.getSemaphore().acquire();

                    try {
                        //Container Umschlagen
                        Thread.sleep(random.nextInt(1000));
                        station.incrementCounter();
                    } finally {
                        //Berechtigungen wieder abgeben.
                        station.getSemaphore().release();
                    }
                    // Fahrt zum Zielort simulieren
                    Thread.sleep(random.nextInt(5000) + 5000); // Zufallszeit zwischen 5 und 10 Sekunden
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Verladestation findeVerladestation() {
        List<Verladestation> besteStationen = new ArrayList<>();

        //Locks holen auf alle Stationen
        for (Verladestation station : verladestationList) {
            station.getLock().lock();
        }

        //Station mit kürzester Queue Finden.
        try {
            int minQueueLength = Integer.MAX_VALUE;
            for(Verladestation station : verladestationList) {
                int aktQueueLength = station.getSemaphore().getQueueLength();
                if(aktQueueLength < minQueueLength) {
                    minQueueLength = aktQueueLength;
                    besteStationen.clear();
                    besteStationen.add(station);
                } else if (aktQueueLength == minQueueLength) {
                    //Alle kürstesten in eine Liste.
                    besteStationen.add(station);
                }
            }

        } finally {
            //Locks wieder freigeben.
            for(Verladestation station : verladestationList) {
                station.getLock().unlock();
            }
        }

        //Aus der kürstesten Liste eine Queue auswählen.
        return besteStationen.get(random.nextInt(besteStationen.size()));



    }
}
