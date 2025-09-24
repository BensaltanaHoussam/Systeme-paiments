package entities;

import enums.StatutAbonnement;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {

    public AbonnementSansEngagement(String nomService,
                                    BigDecimal montantMensuel,
                                    LocalDate dateDebut,
                                    LocalDate dateFin,
                                    StatutAbonnement statut) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
    }

}