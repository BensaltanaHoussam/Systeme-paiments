package entities;
import enums.StatutAbonnement;
import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement  {
    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatutAbonnement status, int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin, status);
        this.dureeEngagementMois = dureeEngagementMois;
    }

    public int getDureeEngagementMois() {
        return dureeEngagementMois;
    }

    public void setDureeEngagementMois(int dureeEngagementMois) {
        this.dureeEngagementMois = dureeEngagementMois;
    }
}
