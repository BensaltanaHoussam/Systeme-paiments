// java
package entities;

import enums.StatutAbonnement;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement {
    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService,
                                    BigDecimal montantMensuel,
                                    LocalDate dateDebut,
                                    LocalDate dateFin,
                                    StatutAbonnement statut,
                                    int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
        this.dureeEngagementMois = dureeEngagementMois;
    }

    @Override
    public Integer getDureeEngagementMois() {
        return this.dureeEngagementMois;
    }

    public void setDureeEngagementMois(Integer dureeEngagementMois) {
        this.dureeEngagementMois = (dureeEngagementMois != null) ? dureeEngagementMois : 0;
    }
}