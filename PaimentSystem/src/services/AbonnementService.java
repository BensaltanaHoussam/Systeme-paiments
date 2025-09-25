package services;

import dao.AbonnementDAOI;
import entities.Abonnement;
import enums.StatutAbonnement;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbonnementService {

    private final AbonnementDAOI abonnementDAO;

    public AbonnementService(AbonnementDAOI abonnementDAO) {
        this.abonnementDAO = Objects.requireNonNull(abonnementDAO, "abonnementDAO ne doit pas être null");
    }

    private void validateForCreate(Abonnement a) {
        validateCommon(a);
    }

    private void validateForUpdate(Abonnement a) {
        if (isBlank(a.getId())) {
            throw new IllegalArgumentException("id obligatoire pour la mise à jour");
        }
        validateCommon(a);
    }

    private void validateCommon(Abonnement a) {
        if (a == null) throw new IllegalArgumentException("Abonnement null");
        if (isBlank(a.getNomService())) throw new IllegalArgumentException("nomService obligatoire");

        BigDecimal montant = a.getMontantMensuel();
        if (montant == null || montant.signum() <= 0) {
            throw new IllegalArgumentException("montantMensuel doit être > 0");
        }

        LocalDate debut = a.getDateDebut();
        if (debut == null) throw new IllegalArgumentException("dateDebut obligatoire");

        LocalDate fin = a.getDateFin();
        if (fin != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException("dateFin doit être >= dateDebut");
        }

        StatutAbonnement statut = a.getStatut();
        if (statut == null) throw new IllegalArgumentException("statut obligatoire");

        Integer duree = a.getDureeEngagementMois();
        if (duree != null && duree < 0) {
            throw new IllegalArgumentException("dureeEngagementMois doit être >= 0");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // API Service
    public Abonnement create(Abonnement a) throws SQLException {
        validateForCreate(a);
        return abonnementDAO.create(a);
    }

    public Optional<Abonnement> findById(String id) throws SQLException {
        if (isBlank(id)) throw new IllegalArgumentException("id obligatoire");
        return abonnementDAO.findById(id);
    }

    public List<Abonnement> findAll() throws SQLException {
        return abonnementDAO.findAll();
    }

    public boolean update(Abonnement a) throws SQLException {
        validateForUpdate(a);
        return abonnementDAO.update(a);
    }

    public boolean delete(String id) throws SQLException {
        if (isBlank(id)) throw new IllegalArgumentException("id obligatoire");
        return abonnementDAO.delete(id);
    }

    public List<Abonnement> findActiveSubscriptions() throws SQLException {
        return abonnementDAO.findActiveSubscriptions();
    }
}