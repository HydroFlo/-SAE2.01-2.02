import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

public class PlateformeTest {
    public final static String CSV_FILE_NAME = "correspondance.csv";
    String[] data = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90"
        };
    Voyageur user = new Voyageur(TypeCout.TEMPS/*, ModaliteTransport.TRAIN*/);
    private Plateforme plat = new Plateforme(data, 4, user, "test");

    @Test
    void testCheckPtVirgule() {
        String[] stab1 = new String[]{"villeA;villeB;Train;60;1.7;80", "villeB;villeD;Train;22;2.4;40"};
        String[] stab2 = new String[]{"villeA;villeB;Train;60;1.7;80", "villeB;;Train;22;2.4;40"};
        String[] stab3 = new String[]{"", "villeB;;Train;22;2.4;40"};
        assertTrue(plat.checkPtVirgule(stab1));
        assertFalse(plat.checkPtVirgule(stab2));
        assertFalse(plat.checkPtVirgule(stab3));

    }

    @Test
    void testCptNbDonnée() {
        String s1 = "bla;b;;blabla;a";
        String s2 = "";
        String s3 = "blabla";
        assertTrue(plat.cptNbDonnée(s1) == 5);
        assertTrue(plat.cptNbDonnée(s2) == 0);
        assertTrue(plat.cptNbDonnée(s3) == 1);
    }

    @Test
    void testGetInfo() {
        String[] stab = new String[]{"ville;villeB;Train;60;1.7;80", "villeB;villeD;Train;22;2.4;40"};
        ArrayList<ArrayList<String>> verif = new ArrayList<ArrayList<String>>();
        ArrayList<String> l1 = new ArrayList<String>(); ArrayList<String> l2 = new ArrayList<String>();
        l1.add("ville");l1.add("villeB");l1.add("Train");l1.add("60");l1.add("1.7");l1.add("80");
        l2.add("villeB");l2.add("villeD");l2.add("Train");l2.add("22");l2.add("2.4");l2.add("40");
        verif.add(l1); verif.add(l2);
        assertTrue(plat.getInfo(stab).equals(verif));
    }

    @Test
    void testVoyagePossible() {
        MultiGrapheOrienteValue g1 = new MultiGrapheOrienteValue();
        Sommet a = new Sommet("villeA", CSV_FILE_NAME); Sommet b = new Sommet("villeB",CSV_FILE_NAME);
        HashMap<TypeCout, Double> poids = new HashMap<TypeCout, Double>();
        poids.put(TypeCout.TEMPS, 42.0);
        Arete ab = new Arete(a, b, ModaliteTransport.TRAIN, poids);
        g1.ajouterSommet(a); g1.ajouterSommet(b); g1.ajouterArete(ab, poids.get(TypeCout.TEMPS));
        MultiGrapheOrienteValue g2 = new MultiGrapheOrienteValue();

        assertTrue(plat.voyagePossible(g1, a, b));
        assertFalse(plat.voyagePossible(g2, a, b));

        g2.ajouterSommet(a); g2.ajouterSommet(b);
        assertFalse(plat.voyagePossible(g2, a, b));
    }
}
