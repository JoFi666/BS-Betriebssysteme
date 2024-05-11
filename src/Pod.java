import java.util.Random;

class Pod extends Thread implements Comparable<Pod> {
    private final Random random = new Random();
    private final String name;
    private long totalTime;
    private int completedRounds;
    private boolean finished = false;
    private final PodRacer racer;
    public Pod(PodRacer racer,String name) {
        this.name = name;
        this.racer = racer;
    }


    public String getPodName() {
        return name;
    }

    public long getTotalTime() {
        return totalTime;
    }

    @SuppressWarnings("unused")
    public int getCompletedRounds() {
        return completedRounds;
    }

    public boolean isFinished() {
        return finished;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        for (int round = 1; round <= racer.getTrackLength(); round++) {
            // Überprüfe, ob der Thread unterbrochen wurde (Unfall)
            if (Thread.interrupted()) {
                if(racer.isDebug()) {
                    System.out.println(name + " wurde gestoppt. Rennen wird abgebrochen.");
                }
                return;
            }

            long roundTime = random.nextInt(101); // Zufällige Rundenzeit zwischen 0 und 100 ms
            if(racer.isDebug()) {
                System.out.println(name + " - Runde " + round + " abgeschlossen in " + roundTime + " ms");
            }
            totalTime += roundTime;
            completedRounds++;
            try {
                Thread.sleep(roundTime); // Pause für die Dauer der Runde
            } catch (InterruptedException e) {
                if(racer.isDebug()) {
                    System.out.println(name + " wurde im Schlaf gestoppt. Rennen wird abgebrochen. ");
                }
                return;
            }
        }
        if(racer.isDebug()) {
            System.out.println(name + " hat das Ziel erreicht!");
        }
        finished = true;

    }

    @Override
    public int compareTo(Pod other) {
        return Long.compare(totalTime, other.totalTime);
    }
}
