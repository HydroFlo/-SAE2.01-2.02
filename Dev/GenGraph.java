import java.util.ArrayList;
import java.util.HashMap;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

/**
 * GenGraph
 * Classe regrouppant les fonctions permettant la transformation de données en graphes.
 */
public class GenGraph {
    private String csvFile;

    public GenGraph(String csvFile) {
        this.csvFile = csvFile;
    }

    public ArrayList<Arete> creatArete(ArrayList<ArrayList<String>> data, ArrayList<Sommet> listSommets, int nbModalite){ //renvoie la liste des arêtes du graphe
        ArrayList<Arete> res = new ArrayList<Arete>();
        for(int i = 0; i < data.size(); i++){ //pour chaque ligne dans data, ajoute une arrete bidirectionnel à la liste revoyée
            HashMap<TypeCout,Double> poids= new HashMap<TypeCout,Double>();
            for(int j = 3; j < 3+nbModalite; j ++){ //fait une liste ayant les différents poids (pollution, durée, prix)
                poids.put(TypeCout.values()[j-3], Double.parseDouble(data.get(i).get(j)));
            }
            res.add(new Arete(this.rechercheSommet(listSommets, data.get(i).get(0)), this.rechercheSommet(listSommets, data.get(i).get(1)), ModaliteTransport.valueOf(data.get(i).get(2).toUpperCase()),poids));
            res.add(new Arete(this.rechercheSommet(listSommets, data.get(i).get(1)), this.rechercheSommet(listSommets, data.get(i).get(0)), ModaliteTransport.valueOf(data.get(i).get(2).toUpperCase()),poids));
        }
        return res;
    }

    public Sommet rechercheSommet(ArrayList<Sommet> l, String trucChercher){ //fouille la liste des sommet, et renvoie une sommet si la String passé en param équivaut à un sommet.
        for(int i = 0; i < l.size(); i ++){
            if(l.get(i).getNom().equals(trucChercher)){
                return l.get(i);
            }
        }
        return new Sommet(null, this.csvFile);
    }

    public ArrayList<Sommet> creatSommet(ArrayList<ArrayList<String>> data){ //renvoie une liste contenant l'ensemble des sommet du graphe
        ArrayList<Sommet> res = new ArrayList<Sommet>();
        ArrayList<String> source = new ArrayList<String>(); //liste qui sert a stoqué l'ensemble des points de départ et d'arrivée
        for(int i = 0; i < data.size(); i ++){
            source.add(data.get(i).get(0)); //ajoute à la liste "source" l'ensemble des point de départ
            source.add(data.get(i).get(1)); //ajoute à la liste "source" l'ensemble des point d'arrivée
        }
        ArrayList<String> verif = new ArrayList<String>(); 
        for(int i = 0; i < source.size(); i++){
            if(!verif.contains(source.get(i))){ //si l'élément n'est pas déjà dans vérif il y est ajouté
                verif.add(source.get(i));
            }
        }
        for(int i = 0; i < verif.size(); i ++){
            res.add(new Sommet(verif.get(i), this.csvFile)); //crée un sommet de chaque point de "vérif" et l'ajoute a la liste de sommet renvoyé à la fin
        }
        return res;
    }

    public MultiGrapheOrienteValue creationGraph(ArrayList<ArrayList<String>> data, int nbModalite, TypeCout modalite, ArrayList<Sommet> listeTotalSommet){
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();
        ArrayList<Sommet> listeSommet = listeTotalSommet;
        ArrayList<Arete> listeAretes = creatArete(data, listeSommet, nbModalite);
        for(int i = 0; i < listeSommet.size(); i ++){ //ajoute les sommets dans le graphe en paramètre
            graphe.ajouterSommet(listeSommet.get(i));
        }
        for(int i = 0; i < listeAretes.size(); i ++){ //ajoute les arêtes au graphe
            graphe.ajouterArete(listeAretes.get(i), listeAretes.get(i).getPoids().get(modalite));
        }
        return graphe;
    }
}