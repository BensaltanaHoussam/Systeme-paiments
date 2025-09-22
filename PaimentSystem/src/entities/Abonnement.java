package entities;
import enums.StatutAbonnement;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Abonnement {

    private String id ;
    private String nomService ;
    private double montantMensuel ;
    private LocalDate dateDebut ;
    private LocalDate dateFin ;
    private StatutAbonnement status ;


    public Abonnement ( String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatutAbonnement status) {
        this.id = UUID.randomUUID().toString();
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.status = status;
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

    public double getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(double montantMensuel) {
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

    public StatutAbonnement getStatus() {
        return status;
    }

    public void setStatus(StatutAbonnement status) {
        this.status = status;
    }
}
