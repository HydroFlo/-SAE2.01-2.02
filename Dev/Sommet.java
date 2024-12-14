import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;

class Sommet implements Lieu {
    public final String SOURCE_FILE = System.getProperty("user.dir")+System.getProperty("file.separator")+"Dev"+System.getProperty("file.separator");
    public final int NB_MODALITE = 3;
    private String nom;
    private Map<ModaliteTransport[], Map<TypeCout, Double>> correspondance;

    public Sommet(String nom, String correspondanceFileName) {
        this.nom = nom;
        try {
            this.correspondance = rangementDesDonnee(CSVToString(new File(SOURCE_FILE+correspondanceFileName)), NB_MODALITE);
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier n'existe pas");
        } catch (IOException e){
            System.out.println("IOException : " + e.getMessage());
        }
    }

    public String getNom() {
        return this.nom;
    }

    public String toString() {
        return this.nom;
    }

    private ArrayList<ArrayList<String>> CSVToString(File csv) throws FileNotFoundException, IOException{   //Parcourt un fichier CSV et renvoie une ArrayList des donnée contenu
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try (Scanner sc = new Scanner(csv)) {
                sc.useDelimiter(System.getProperty("line.separator"));
                while (sc.hasNext()) {
                    Scanner sc2 = new Scanner(sc.next());
                    sc2.useDelimiter(";");
                    ArrayList<String> sousList = new ArrayList<String>();
                    do {
                        sousList.add(sc2.next());
                    } while (sc2.hasNext());
                    list.add(sousList);
                    sc2.close();
                }
            } catch (FileNotFoundException e) {
                System.out.println("There is no file");
            }
        return list;
    }

    private Map<ModaliteTransport[], Map<TypeCout, Double>> rangementDesDonnee(ArrayList<ArrayList<String>> data, int nbModalite){  //Range correctement les donnée dans une Map pour la création de Sommet
        Map<ModaliteTransport[], Map<TypeCout, Double>> res = new HashMap<ModaliteTransport[], Map<TypeCout, Double>>();
        for(ArrayList<String> list : data){
            if(this.nom.equals(list.get(0))){
                //Création du tableau de ModaliteTransport qui servira de clé pour la Map correspondance 
                //(nécessite que les transport soit bien spécifier en 2e et 3e position dans les lignes du csv)
                ModaliteTransport[] key = new ModaliteTransport[2];
                key[0] = ModaliteTransport.valueOf(list.get(1).toUpperCase()); 
                key[1] = ModaliteTransport.valueOf(list.get(2).toUpperCase());
                HashMap<TypeCout,Double> poids= new HashMap<TypeCout,Double>();
                int i = TypeCout.values().length-1;
                for(int j = 3; j < 3+nbModalite; j ++){ //fait une liste ayant les différents poids (pollution, durée, prix)
                    poids.put(TypeCout.values()[i], Double.parseDouble(list.get(j)));
                    i--;
                }
                res.put(key, poids);
            }
        }
        return res;
    }

    public Map<ModaliteTransport[], Map<TypeCout, Double>> getCorrespondance() {
        return correspondance;
    }

}
