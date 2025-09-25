// java
package entities;

import enums.StatutAbonnement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Abonnement {

    private String id;
    private String nomService;
    private BigDecimal montantMensuel;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutAbonnement statut;

    protected Abonnement(String nomService,
                         BigDecimal montantMensuel,
                         LocalDate dateDebut,
                         LocalDate dateFin,
                         StatutAbonnement statut) {
        this.id = UUID.randomUUID().toString();
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    protected Abonnement(String nomService,
                         double montantMensuel,
                         LocalDate dateDebut,
                         LocalDate dateFin,
                         StatutAbonnement statut) {
        this(nomService, BigDecimal.valueOf(montantMensuel), dateDebut, dateFin, statut);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public BigDecimal getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(BigDecimal montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutAbonnement getStatut() {
        return statut;
    }

    public void setStatut(StatutAbonnement statut) {
        this.statut = statut;
    }

    // Alias de compatibilité
    public StatutAbonnement getStatus() {
        return getStatut();
    }

    public void setStatus(StatutAbonnement statut) {
        setStatut(statut);
    }

    // Champs "virtuels"
    public String getTypeAbonnement() {
        return (this instanceof AbonnementAvecEngagement) ? "AVEC_ENGAGEMENT" : "SANS_ENGAGEMENT";
    }

    public Integer getDureeEngagementMois() {
        if (this instanceof AbonnementAvecEngagement) {
            AbonnementAvecEngagement a = (AbonnementAvecEngagement) this;
            return a.getDureeEngagementMois();
        }
        return null;
    }
}