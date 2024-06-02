package abgabe03.Aufgabe2;

public class SynchronisedTisch extends AllgemeinerTisch {
    private boolean zugBereit;
    public SynchronisedTisch() {
        super();
    }

    @Override
    public synchronized void doZug(Spieler spieler, String zug) throws InterruptedException {
        while(zugBereit) {
            wait();
        }
        if(spieler == spieler1) {
            spieler1Zug = zug;
        }
        else if(spieler == spieler2) {
            spieler2Zug = zug;
        }
        else {
            throw new IllegalArgumentException("Spieler ist kein eingetragener Spieler!");
        }


        if (spieler1Zug != null && spieler2Zug != null) {
            zugBereit = true;
            notifyAll();
        } else {
            notify();
        }
    }

    public synchronized void zugAuswerten() throws InterruptedException {
        while (!zugBereit) {
            wait();
        }
        if (spieler1Zug != null && spieler2Zug != null) {
            rundenZahl++;

            if(spieler1Zug.equals(spieler2Zug)){
                unentschiedenZahl++;
            }
            else if ((spieler1Zug.equals("Schere") && spieler2Zug.equals("Papier")) ||
                    (spieler1Zug.equals("Stein") && spieler2Zug.equals("Schere")) ||
                    (spieler1Zug.equals("Papier") && spieler2Zug.equals("Stein"))) {
                gewinneSpieler1++;
            } else {
                gewinneSpieler2++;
            }

            spieler1Zug = null;
            spieler2Zug = null;
            zugBereit = false;
            notifyAll();
        }
    }


}
