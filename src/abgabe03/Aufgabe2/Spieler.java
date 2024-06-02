package abgabe03.Aufgabe2;

import java.util.Random;

public class Spieler extends Thread {
    private final AllgemeinerTisch tisch;
    private final Random rand = new Random();
    int benoetigteZeit = 500;

    public Spieler(AllgemeinerTisch tisch) {
        this.tisch = tisch;

    }
    public Spieler(AllgemeinerTisch tisch, int benoetigteZeit) {
        this.tisch = tisch;
        this.benoetigteZeit = benoetigteZeit;

    }

    @Override
    public void run() {
        String[] zuege = {"Schere", "Stein", "Papier"};
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String zug = zuege[rand.nextInt(zuege.length)];
                tisch.doZug(this, zug);
                Thread.sleep(benoetigteZeit); // Simuliert die Zeit, die für einen neuen Zug benötigt wird
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }






}
