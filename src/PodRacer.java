import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PodRacer {
    private static final int NUM_RACERS = 5; // Anzahl der Fahrer*innen
    private static final int TRACK_LENGTH = 3; // Länge der Strecke in Runden
    private static final boolean DEBUG = false;
    private static final int CRASH_PROBABILITY = 40; // Wahrscheinlichkeit eines Unfalls (in Prozent)
    private static final int ACCIDENT_SLEEPTIME = 500;
    private List<Pod> racers = new ArrayList<>();

    public int getRacerCount() {
        return NUM_RACERS;
    }
    public int getTrackLength() {
        return TRACK_LENGTH;
    }
    public boolean isDebug() {
        return DEBUG;
    }
    public int getCrashProbability() {
        return CRASH_PROBABILITY;
    }
    public int getAccidentSleepTime() {
        return ACCIDENT_SLEEPTIME;
    }

    public void startRace() {

        boolean hasAccident = false;
        boolean raceCompleted = true;

        // Pod-Racer Threads erstellen und starten
        for (int i = 1; i <= NUM_RACERS; i++) {
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
                        if(DEBUG) {
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
                if(DEBUG) {
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
            System.out.println("Teilnehmer: " + NUM_RACERS + " | Runden:" + TRACK_LENGTH   );
            for (int i = 0; i < NUM_RACERS; i++) {
                Pod pod = racers.get(i);
                System.out.println("Platz " + (i + 1) + ": " + pod.getPodName() + " - Gesamtlaufzeit: " + pod.getTotalTime() + " ms");
            }

        }
        //Warnung ausgeben.
        else {
            System.out.println("\nDas Rennen wurde vorzeitig abgebrochen, keine Platzierung möglich.");
        }
        accident.interrupt();
    }

    public static void main(String[] args) {
        int i=0;
        while(i<3) {
            PodRacer podRacer = new PodRacer();
            podRacer.startRace();
            i++;
        }

    }
}


