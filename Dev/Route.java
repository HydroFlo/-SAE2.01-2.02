import java.util.List;

import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Trancon;

/**
 * Route
 */
public class Route implements Chemin, Comparable<Route>{
    private List<Trancon> aretes;
    private double poids;

    public Route(List<Trancon> aretes, double poids) {
        this.aretes = aretes;
        this.poids = poids;
    }

    @Override
    public List<Trancon> aretes() {
        return this.aretes;
    }

    @Override
    public double poids() {
        return this.poids;
    }

    public void setAretes(List<Trancon> aretes) {
        this.aretes = aretes;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    @Override
    public String toString() {
        return "Chemin : "+aretes + ", Poids : " + poids + "]";
    }

    @Override
    public int compareTo(Route other) {
        return (int) (this.poids - other.poids);
    }

    
}