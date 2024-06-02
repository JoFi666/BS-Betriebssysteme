package abgabe03.Aufgabe2;

public abstract class AllgemeinerTisch {
    Spieler spieler1 = null;
    Spieler spieler2 = null;
    String spieler1Zug;
    String spieler2Zug;
    int rundenZahl;
    int unentschiedenZahl;
    int gewinneSpieler1;
    int gewinneSpieler2;

    public AllgemeinerTisch() {
        this.spieler1Zug = null;
        this.spieler2Zug = null;
        this.rundenZahl = 0;
        this.unentschiedenZahl = 0;
        this.gewinneSpieler1 = 0;
        this.gewinneSpieler2 = 0;
    }

    public void doZug(Spieler spieler, String zug) throws InterruptedException {
    }

    public void zugAuswerten() throws InterruptedException{
    }
    public boolean spielerEinschreiben(Spieler spieler, int playerNumber) {
        if(playerNumber == 1) {
            spieler1 = spieler;
        } else if(playerNumber == 2) {
            spieler2 = spieler;
        } else {
            return false;
        }
        return true;
    }

    public int getRundenZahl() {
        return rundenZahl;
    }

    public int getUnentschiedenZahl() {
        return unentschiedenZahl;
    }

    public int getGewinneSpieler1() {
        return gewinneSpieler1;
    }

    public int getGewinneSpieler2() {
        return gewinneSpieler2;
    }
}
