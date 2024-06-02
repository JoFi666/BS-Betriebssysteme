package abgabe03.Aufgabe2;

class Schiedsrichter extends Thread {
    private final AllgemeinerTisch tisch;
    int benoetigteZeit = 500;

    public Schiedsrichter(AllgemeinerTisch tisch) {
        this.tisch = tisch;
    }
    public Schiedsrichter(AllgemeinerTisch tisch, int time) {
        this.tisch = tisch;
        this.benoetigteZeit = time;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                tisch.zugAuswerten();
                Thread.sleep(benoetigteZeit); // Simuliert die Zeit, die für die Auswertung benötigt wird
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
