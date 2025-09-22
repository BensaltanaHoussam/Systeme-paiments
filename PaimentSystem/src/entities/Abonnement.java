package entities;
import enums.StatutAbonnement;

public abstract class Abonnement {

    private String id ;
    private String nomService ;
    private double montantMensuel ;
    private String dateDebut ;
    private String dateFin ;
    private StatutAbonnement status ;
}
