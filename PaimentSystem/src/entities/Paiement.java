package entities;

import enums.StatutPaiement;
import java.time.LocalDate;

import java.util.UUID;

public abstract class Paiement {
    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEchance;
    private LocalDate datePaiement;
    private String typePaiement;
    private StatutPaiement statut;

    public Paiement(String idAbonnement, LocalDate dateEchance, LocalDate datePaiement, String typePaiement, StatutPaiement statut) {
        this.idAbonnement = idAbonnement;
        this.dateEchance = dateEchance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
    }


    public String getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(String idPaiement) {
        this.idPaiement = idPaiement;
    }

    public String getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(String idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public LocalDate getDateEchance() {
        return dateEchance;
    }

    public void setDateEchance(LocalDate dateEchance) {
        this.dateEchance = dateEchance;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }
}
