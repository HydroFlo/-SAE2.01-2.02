import fr.ulille.but.sae_s2_2024.ModaliteTransport;

/**
 * Voyageur
 */
public class Voyageur {
    private TypeCout critereAOptimiser;
    private ModaliteTransport modeDeDeplacement;
    private TypeCout critereSecondaire;
    private double borneCritSec;

    

    public Voyageur(TypeCout critereAOptimiser/*, ModaliteTransport modeDeDeplacement*/, TypeCout critereSecondaire, double borneCritSec) {
        this.critereAOptimiser = critereAOptimiser;
        //this.modeDeDeplacement = modeDeDeplacement;
        this.critereSecondaire = critereSecondaire;
        this.borneCritSec = borneCritSec;
    }

    public Voyageur(TypeCout critereAOptimiser/*,ModaliteTransport modeDeDeplacement*/) {
        this(critereAOptimiser, /*modeDeDeplacement, */critereAOptimiser, Double.MAX_VALUE);
    }

    public TypeCout getCritereAOptimiser() {
        return critereAOptimiser;
    }

    public ModaliteTransport getModeDeDeplacement() {
        return modeDeDeplacement;
    }

    public TypeCout getCritereSecondaire() {
        return critereSecondaire;
    }

    public double getBorneCritSec() {
        return borneCritSec;
    }

}