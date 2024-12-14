
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;

class Plateforme {
    final int NB_VOYAGE_ELIGIBLE;
    final int NB_DONNEES;
    private GenGraph generateur;
    private ArrayList<Sommet> ensembleSommet;
    private ArrayList<Arete> ensembleArete;
    private Voyageur user;
    
    //Data = liste des donnée, NB_VOYAGE = nombre de voyage a afficher au final, csvFile = nom du fichier csv contenant les donnée pour changement de transport
    public Plateforme(String[] data, int NB_VOYAGE_ELIGIBLE, Voyageur user, String csvFile){
        this.NB_DONNEES = cptNbDonnée(data[0]);
        this.generateur = new GenGraph(csvFile);
        this.NB_VOYAGE_ELIGIBLE = NB_VOYAGE_ELIGIBLE;
        this.ensembleSommet = this.generateur.creatSommet(this.getInfo(data));
        this.ensembleArete = this.generateur.creatArete(this.getInfo(data), this.ensembleSommet, this.NB_DONNEES-3);
        this.user = user;
    }

    public ArrayList<Sommet> getEnsembleSommet() {
        return ensembleSommet;
    }

    public ArrayList<Arete> getEnsembleArete() {
        return ensembleArete;
    }

    public int cptNbDonnée(String chaine){  //Compte le nombre de donnée dans data (ne vérifie pas si le format des chaine est correcte)
        int res = 0;
        for(int i = 0; i < chaine.length(); i ++){
            if(chaine.charAt(i) == ';'){
                res++;
            }
        }
        if(res == 0){
            if(chaine.isEmpty()) return 0;
            return 1;
        }
        return res+1;
    }


    public boolean checkPtVirgule(String[] data) {  //vérifie si la structure des chaines de data est correcte
        if (data.length <= 0)
            return false;
        int caraindex = 1;
        int nbPtVirg = 0;

        for (int i = 0; i < data.length; i++) {
            caraindex = 1;
            nbPtVirg = 0;
            // Check de la chaine courante
            if (data[i].length() == 0)
                return false;
            if (data[i].charAt(0) == ';')
                return false;
            if (data[i].charAt(data[i].length() - 1) == ';')
                return false;

            // Check de chaque char pour voir si la structure est bonne
            while (caraindex < data[i].length()) {
                if (data[i].charAt(caraindex) == ';') {
                    if (data[i].charAt(caraindex + 1) == ';')
                        return false;
                    nbPtVirg++;
                }
                caraindex++;
            }
            if (nbPtVirg != NB_DONNEES - 1)
                return false;
        }

        return true;
    }

    public ArrayList<ArrayList<String>> getInfo(String[] data) {    //Convertie les data sous forme de tableau de chaine en une ArrayList pour l'exploiter
        int cpt = 0;
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < data.length; i++) {
            ArrayList<String> SmolData = new ArrayList<String>();
            for (int j = 0; j < NB_DONNEES; j++) {
                if (j == 0) {
                    SmolData.add(subSemiCol(data[i], 0));
                    cpt += SmolData.get(j).length()+1;
                } else {
                    SmolData.add(subSemiCol(data[i], cpt));
                    cpt += SmolData.get(j).length()+1;
                }
            }
            cpt = 0;
            result.add(SmolData);
        }
        return result;
    }

    private String subSemiCol(String chaine, int prevIndex) {
        if(chaine.indexOf(';', prevIndex)==-1){
            return chaine.substring(prevIndex);
        }
        return chaine.substring(prevIndex, chaine.indexOf(';', prevIndex));
    }

    /*private String transportToString(ModaliteTransport modalite){
        return modalite.toString().substring(0,1)+modalite.toString().substring(1).toLowerCase();
    }

    private ArrayList<ArrayList<String>> filtreSelonTransport(ArrayList<ArrayList<String>> data){
        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < data.size(); i ++){
            if(data.get(i).contains(this.transportToString(user.getModeDeDeplacement()))){
                res.add(data.get(i));
            }
        }
        return res;
    }*/

    //Crée le bon graphe selon la modalité choisie
    public MultiGrapheOrienteValue graphSelonMod(String[] data, int nbModalite) {
        return generateur.creationGraph(/*filtreSelonTransport(*/this.getInfo(data)/*)*/, nbModalite, user.getCritereAOptimiser(), this.ensembleSommet);
    }

    //revoie les NB_VOYAGE_ELIGIBLE plus courts chemin correspondant à la modalité choisie par l'utilisateur
    public List<Route> plusCourtChemin(Sommet depart, Sommet arrivee, MultiGrapheOrienteValue graphe) {
        return coutSuppSiChange(filtreSelonCritSec(AlgorithmeKPCC.kpcc(graphe, depart, arrivee, NB_VOYAGE_ELIGIBLE)));
    }

    private List<Chemin> filtreSelonCritSec(List<Chemin> lChemin){  //Retire de la List de chemin, tout chemin ne respectant pas la borne secondaire de l'utilisateur
        List<Chemin> res = new ArrayList<Chemin>();
        for(Chemin c : lChemin){
            double sommeVerif = 0;
            for(int i = 0; i < c.aretes().size(); i++){
                Arete tmp = (Arete) c.aretes().get(i);
                sommeVerif += tmp.getPoids().get(this.user.getCritereSecondaire());
            }
            if(sommeVerif < user.getBorneCritSec()) res.add(c);
        }
        return res;
    }

    public boolean voyagePossible(MultiGrapheOrienteValue graphe, Sommet depart, Sommet arrivee){ //determine si il existe un chemin entre 2 ville d'après le graphe en paramètre
        if(graphe.sommets().isEmpty()) return false;
        if(graphe.aretes().isEmpty()) return false;
        if(!graphe.sommets().contains(depart)) return false;
        if(!graphe.sommets().contains(arrivee)) return false;
        if(this.plusCourtChemin(depart, arrivee, graphe).size() == 0) return false;
        return true;
    }

    public String affichage(List<Route> lc){ //permet l'affichage d'un chemin sous la forme <Départ>, <changement si il y a>, <Arrivée>
        String res = "";
        for(Chemin c : lc){
            List<Trancon> tmp = c.aretes();
            res += "Départ à "+tmp.get(0).getDepart()+" en "+tmp.get(0).getModalite()+changementTransport(c)+". Arrivée : "+tmp.get(tmp.size()-1).getArrivee()+'\n';
            //res += ". Durée : " + c.get(i).poids()+" minutes\n";
        }
        try(FileWriter fw = new FileWriter(new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"Dev"+System.getProperty("file.separator")+"historique.txt"), true)) {
            fw.write(res);                
        } catch (IOException e) {
                e.getMessage();
        }
        return res;
    }

    private String changementTransport(Chemin c){ //crée la ligne indiquant un changement de transport pour l'affichage
        String res = "";
        ModaliteTransport check = c.aretes().get(0).getModalite();
        for(int i = 1; i < c.aretes().size(); i++){
            if(c.aretes().get(i).getModalite() != check){
                res += ". Une fois à : "+c.aretes().get(i).getDepart()+", changement de "+check+ " à "+c.aretes().get(i).getModalite();
                check = c.aretes().get(i).getModalite();
            }
        }
        return res;
    }

    private List<Route> coutSuppSiChange(List<Chemin> lc){  //Crée une liste qui à chaque chemin ajoute les cout supplémentaire dû au changement de transport, la trie puis la renvoie
        List<Route> res = new ArrayList<Route>();
        for(Chemin c : lc){
            ModaliteTransport[] check = new ModaliteTransport[2];
            ModaliteTransport[] reverseCheck = new ModaliteTransport[2];
            check[0] = c.aretes().get(0).getModalite();
            reverseCheck[1] = check[0];
            Route r = new Route(c.aretes(), c.poids());
            for(int i = 1; i < c.aretes().size(); i++){
                if(c.aretes().get(i).getModalite() != check[0]){
                    check[1] = c.aretes().get(i).getModalite();
                    reverseCheck[0] = check[1];
                    Sommet depTmp = (Sommet) c.aretes().get(i).getDepart();
                    if(depTmp.getCorrespondance().containsKey(check)){
                        r.setPoids(r.poids() + depTmp.getCorrespondance().get(check).get(user.getCritereAOptimiser()));
                    }
                    if(depTmp.getCorrespondance().containsKey(reverseCheck)){
                        r.setPoids(r.poids() + depTmp.getCorrespondance().get(reverseCheck).get(user.getCritereAOptimiser()));
                    }
                    check[0] = c.aretes().get(i).getModalite();
                    reverseCheck[1] = check[0];
                }
            }
            res.add(r);
        }
        Collections.sort(res);
        return res;
    }

}