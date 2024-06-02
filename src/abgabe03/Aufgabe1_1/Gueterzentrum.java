package abgabe03.Aufgabe1_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gueterzentrum {
    public static void main(String[] args) throws InterruptedException {
        int anzahlVerladerampen = 10;
        int anzahlLkws = 20;
        int simulationsZeit = 20_000; //20s

        List<Verladestation> verladestationen = new ArrayList<>();
        for (int i = 0; i < anzahlVerladerampen; i++) {
            verladestationen.add(new Verladestation());
        }

        List<Lkw> lkwList = new ArrayList<>();
        for (int i = 0; i < anzahlLkws; i++) {
            Lkw lkw = new Lkw(verladestationen);
            lkwList.add(lkw);
            lkw.start();
        }

        for(int i = 0; i < simulationsZeit; i+=1000) {
            float prozent = ((float) i /simulationsZeit) * 100;
            System.out.println(Math.round(prozent) + "%");
            Thread.sleep(1000);
        }


        for (Lkw lkw : lkwList) {
            lkw.interrupt();
        }

        for (Lkw lkw : lkwList) {
            lkw.join();
        }

        Collections.sort(verladestationen);
        for(Verladestation station : verladestationen){
            System.out.println("Verladestation " + station.getId() + ": " + station.getCounter() + " Container umgeschlagen.");
        }
    }


}
