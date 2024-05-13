import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PodRace {
    private final int numRacers = 5; // Anzahl der Fahrer*innen
    private final int trackLength = 3; // Länge der Strecke in Runden
    private final boolean debug = false;
    private final int crashProbability = 40; // Wahrscheinlichkeit eines Unfalls (in Prozent)
    private final int accidentSleeptime = 500;
    private final List<Pod> racers = new ArrayList<>();

    public int getRacerCount() {
        return numRacers;
    }
    public int getTrackLength() {
        return trackLength;
    }
    public boolean isDebug() {
        return debug;
    }
    public int getCrashProbability() {
        return crashProbability;
    }
    public int getAccidentSleepTime() {
        return accidentSleeptime;
    }

    public boolean startRace() {

        boolean hasAccident = false;
        boolean raceCompleted = true;

        // Pod-Racer Threads erstellen und starten
        for (int i = 1; i <= numRacers; i++) {
            Pod pod = new Pod(this,"Pod " + i);
            racers.add(pod);
            pod.start();
        }

        // Accident Thread
        Thread accident = new Thread() {
            @SuppressWarnings("BusyWait")
            public void run() {
                Random random = new Random();
                while (!Thread.interrupted()) {
                    if (random.nextInt(100) < getCrashProbability()) {
                        if(debug) {
                            System.out.println("Ein Unfall ist passiert! Rennen wird abgebrochen.");
                        }
                        // Stoppe alle laufenden Pod-Racer-Threads
                        for (Pod pod : racers) {
                            pod.interrupt();
                        }
                        //Stoppe Accident-Thread.
                        Thread.currentThread().interrupt();
                        break;
                    }
                    // Warte eine Weile, bevor der nächste Unfall überprüft wird
                    try {
                        Thread.sleep(getAccidentSleepTime()); // Wartezeit in Millisekunden
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        accident.start();


        // Auf das Ende des Rennens warten
        for (Pod pod : racers) {
            try {
                pod.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Haben alle Pod das Rennen beendet?
            if (!pod.isFinished()) {
                if(debug) {
                    System.out.println("Pod " + pod.getName() + " nicht beendet");
                }
                raceCompleted = false;
                break;
            }
        }

        // Ergebnistabelle sortieren und ausgeben
        if(raceCompleted) {
            Collections.sort(racers);
            System.out.println("\nGesamtergebnistabelle:");
            System.out.println("Teilnehmer: " + numRacers + " | Runden:" + trackLength);
            for (int i = 0; i < numRacers; i++) {
                Pod pod = racers.get(i);
                System.out.println("Platz " + (i + 1) + ": " + pod.getPodName() + " - Gesamtlaufzeit: " + pod.getTotalTime() + " ms");
            }

        }
        //Warnung ausgeben.
        else {
            System.out.println("\nDas Rennen wurde vorzeitig abgebrochen, keine Platzierung möglich.");
        }
        accident.interrupt();

        return raceCompleted;
    }

    public static void main(String[] args) {
        int i=0;
        float d = 0;
        while(i<100) {
            PodRace podRacer = new PodRace();
            d += podRacer.startRace() ? 0.0f : 1.0f;
            i++;
        }


    }
}


