package entities;
import enums.StatutAbonnement;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {


    public AbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatutAbonnement status) {
        super(nomService, montantMensuel, dateDebut, dateFin, status);
    }
}