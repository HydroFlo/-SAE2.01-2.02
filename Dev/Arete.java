import java.util.Map;

import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

class Arete implements Trancon{
    private Lieu depart;
    private Lieu arrivee;
    private ModaliteTransport modalite;
    private Map<TypeCout,Double> poids;
    
    public Arete(Sommet depart, Sommet arrivee, ModaliteTransport modalite, Map<TypeCout,Double> poids) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
        this.poids = poids;
    }

    public Lieu getDepart() {
        return depart;
    }

    public Lieu getArrivee() {
        return arrivee;
    }

    public ModaliteTransport getModalite() {
        return modalite;
    }

    public String toString(){
        return "("+getDepart() +","+getArrivee()+','+getModalite()+")";
    }

    public Map<TypeCout,Double> getPoids() {
        return poids;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((depart == null) ? 0 : depart.hashCode());
        result = prime * result + ((arrivee == null) ? 0 : arrivee.hashCode());
        result = prime * result + ((modalite == null) ? 0 : modalite.hashCode());
        result = prime * result + ((poids == null) ? 0 : poids.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arete other = (Arete) obj;
        if (depart == null) {
            if (other.depart != null)
                return false;
        } else if (!depart.equals(other.depart))
            return false;
        if (arrivee == null) {
            if (other.arrivee != null)
                return false;
        } else if (!arrivee.equals(other.arrivee))
            return false;
        if (modalite != other.modalite)
            return false;
        if (poids == null) {
            if (other.poids != null)
                return false;
        } else if (!poids.equals(other.poids))
            return false;
        return true;
    }

    

    

}