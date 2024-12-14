import java.util.ArrayList;

//import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

/**
 * UsePlateforme
 */
public class UsePlateforme {
    public final static String CSV_FILE_NAME = "correspondance.csv";

    public static void main(String[] args) {
        MultiGrapheOrienteValue g1 = new MultiGrapheOrienteValue();
        String[] data = new String[] {
                "Lille;Paris;Train;40;752;1",
                "Lille;Paris;Bus;20;5640;3",
                "Lille;Paris;Avion;622;15604;4.5",
                "Paris;Osaka;Avion;918;798875;13",
                "Paris;Narita;Avion;851;806428;12",
                "Osaka;Kyoto;Train;17;412;1.16",
                "Osaka;Kyoto;Bus;20;3090;1.5",
                "Osaka;Nagoya;Avion;203;14110;4.93",
                "Osaka;Narita;Avion;140;40753;1",
                "Kyoto;Tokyo;Train;80;1936;2.16",
                "Kyoto;Nagoya;Train;30;596;1.5",
                "Nagoya;Tokyo;Train;70;640;2.79",
                "Nagoya;Tokyo;Avion;180;30212;1",
                "Tokyo;Narita;Train;11;200;2.30",
                "Tokyo;Narita;Bus;13;1500;1.28"
        };
        Voyageur user = new Voyageur(TypeCout.PRIX/*, ModaliteTransport.TRAIN , TypeCout.TEMPS, 180 */);
        Plateforme appli = new Plateforme(data, 4, user, CSV_FILE_NAME);
        // if(appli.checkPtVirgule(data)) System.out.println("Cristobal a pas merdé");
        ArrayList<ArrayList<String>> trueData = appli.getInfo(data);
        System.out.println(trueData);
        g1 = appli.graphSelonMod(data, 3);
        System.out.println(g1.sommets());
        System.out.println(appli.voyagePossible(g1, appli.getEnsembleSommet().get(0), appli.getEnsembleSommet().get(3)));
        System.out.println(appli.getEnsembleSommet());
        System.out.println(appli.getEnsembleArete());
        /*if (appli.voyagePossible(g1, appli.getEnsembleSommet().get(0), appli.getEnsembleSommet().get(3)))
        System.out.println(appli.plusCourtChemin(appli.getEnsembleSommet().get(0), appli.getEnsembleSommet().get(3), g1));*/
        if (appli.voyagePossible(g1, appli.getEnsembleSommet().get(0), appli.getEnsembleSommet().get(3)))
            System.out.println(appli.affichage(appli.plusCourtChemin(appli.getEnsembleSommet().get(0), appli.getEnsembleSommet().get(3), g1)));
        Sommet t = new Sommet("Lille", CSV_FILE_NAME);
        System.out.println("TypeCout[0] = " + TypeCout.values()[0] + ", TypeCout[1] = " + TypeCout.values()[1]+ ", TypeCout[2] = " + TypeCout.values()[2]);
        System.out.println(t);
        System.out.println(t.SOURCE_FILE);
        System.out.println(t.getCorrespondance());
    }
}