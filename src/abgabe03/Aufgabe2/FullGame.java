package abgabe03.Aufgabe2;

public class FullGame {
    public static void main(String[] args) throws InterruptedException {
        int simulationsZeit = 10_000;

        //AllgemeinerTisch tisch = new Tisch();
        AllgemeinerTisch tisch = new ConcurrentTisch();
        Spieler spieler1 = new Spieler(tisch,0);
        Spieler spieler2 = new Spieler(tisch, 0);
        Schiedsrichter schiedsrichter = new Schiedsrichter(tisch, 0);

        tisch.spielerEinschreiben(spieler1, 1);
        tisch.spielerEinschreiben(spieler2, 2);

        spieler1.start();
        spieler2.start();
        schiedsrichter.start();

        Thread.sleep(simulationsZeit);

        // Threads unterbrechen
        spieler1.interrupt();
        spieler2.interrupt();
        schiedsrichter.interrupt();

        // Auf das Ende der Threads warten
        spieler1.join();
        spieler2.join();
        schiedsrichter.join();

        // Gesamtauswertung
        System.out.println("Gesamtanzahl gespielter Runden: " + tisch.getRundenZahl());
        System.out.println("Anzahl Unentschieden: " + tisch.getUnentschiedenZahl());
        System.out.println("Anzahl Gewinne Spieler 1: " + tisch.getGewinneSpieler1());
        System.out.println("Anzahl Gewinne Spieler 2: " + tisch.getGewinneSpieler2());


    }
}
