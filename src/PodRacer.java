import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PodRacer {
    static final int NUM_RACERS = 5; // Anzahl der Fahrer*innen
    static final int TRACK_LENGTH = 3; // Länge der Strecke in Runden
    static final boolean DEBUG = false;
    private static final int CRASH_PROBABILITY = 40; // Wahrscheinlichkeit eines Unfalls (in Prozent)


    public static void main(String[] args) {
        List<Pod> racers = new ArrayList<>();

        // Pod-Racer Threads erstellen und starten
        for (int i = 1; i <= NUM_RACERS; i++) {
            Pod pod = new Pod("Pod " + i);
            racers.add(pod);
            pod.start();
        }
        boolean hasAccident = false;
        Thread accident = new Thread() {
            public void run() {
                Random random = new Random();
                while (!Thread.interrupted()) {
                    if (random.nextInt(100) < CRASH_PROBABILITY) {
                        System.out.println("Ein Unfall ist passiert! Rennen wird abgebrochen.");
                        // Stoppe alle laufenden Pod-Racer-Threads
                        for (Pod pod : racers) {
                            pod.interrupt();
                        }
                        break;
                    }
                    // Warte eine Weile, bevor der nächste Unfall überprüft wird
                    try {
                        Thread.sleep(50); // Wartezeit in Millisekunden
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

        };
        accident.start();

        // Auf das Ende des Rennens warten
        boolean raceCompleted = true;
        for (Pod pod : racers) {
            try {
                pod.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!pod.isFinished()) {
                if(DEBUG) {
                    System.out.println("Pod " + pod.getName() + " nicht beendet");
                }
                raceCompleted = false;
                break;
            }
        }

        accident.interrupt();
        // Ergebnistabelle sortieren und ausgeben
        if(raceCompleted) {
            Collections.sort(racers);
            System.out.println("\nGesamtergebnistabelle:");
            System.out.println("Teilnehmer: " + NUM_RACERS + " | Runden:" + TRACK_LENGTH   );
            for (int i = 0; i < NUM_RACERS; i++) {
                Pod pod = racers.get(i);
                System.out.println("Platz " + (i + 1) + ": " + pod.getPodName() + " - Gesamtlaufzeit: " + pod.getTotalTime() + " ms");
            }

        }
        else {
            System.out.println("Das Rennen wurde vorzeitig abgebrochen, keine Platzierung möglich.");
        }


    }
}

class Pod extends Thread implements Comparable<Pod> {
    private final Random random = new Random();
    private final String name;
    private long totalTime;
    private int completedRounds;
    private boolean finished = false;
    public Pod(String name) {
        this.name = name;
    }


    public String getPodName() {
        return name;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getCompletedRounds() {
        return completedRounds;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        for (int round = 1; round <= PodRacer.TRACK_LENGTH; round++) {
            // Überprüfe, ob der Thread unterbrochen wurde (Unfall)
            if (Thread.interrupted()) {
                if(PodRacer.DEBUG) {
                    System.out.println(name + " wurde gestoppt. Rennen wird abgebrochen.");
                }
                return;
            }

            long roundTime = random.nextInt(101); // Zufällige Rundenzeit zwischen 0 und 100 ms
            if(PodRacer.DEBUG) {
                System.out.println(name + " - Runde " + round + " abgeschlossen in " + roundTime + " ms");
            }
            totalTime += roundTime;
            completedRounds++;
            try {
                Thread.sleep(roundTime); // Pause für die Dauer der Runde
            } catch (InterruptedException e) {
                if(PodRacer.DEBUG) {
                    System.out.println(name + " wurde im Schlaf gestoppt. Rennen wird abgebrochen. ");
                }
                return;
            }
        }
        if(PodRacer.DEBUG) {
            System.out.println(name + " hat das Ziel erreicht!");
        }
        finished = true;

    }

    @Override
    public int compareTo(Pod other) {
        return Long.compare(totalTime, other.totalTime);
    }
}
