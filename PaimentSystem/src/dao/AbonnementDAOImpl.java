package dao;

import entities.Abonnement;
import entities.AbonnementAvecEngagement;
import entities.AbonnementSansEngagement;
import enums.StatutAbonnement;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbonnementDAOImpl implements AbonnementDAOI {

    private static final String SQL_INSERT =
            "INSERT INTO abonnement " +
                    "(id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois " +
                    "FROM abonnement WHERE id = ?";

    private static final String SQL_SELECT_ALL =
            "SELECT id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois " +
                    "FROM abonnement ORDER BY dateDebut DESC, id";

    private static final String SQL_UPDATE =
            "UPDATE abonnement SET " +
                    "nomService = ?, montantMensuel = ?, dateDebut = ?, dateFin = ?, statut = ?, typeAbonnement = ?, dureeEngagementMois = ? " +
                    "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM abonnement WHERE id = ?";

    private static final String SQL_FIND_ACTIVE =
            "SELECT id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois " +
                    "FROM abonnement " +
                    "WHERE dateDebut <= CURRENT_DATE() AND (dateFin IS NULL OR dateFin >= CURRENT_DATE()) " +
                    "ORDER BY dateDebut DESC";

    @Override
    public Abonnement create(Abonnement a) throws SQLException {
        if (a.getId() == null || a.getId().isEmpty()) {
            a.setId(java.util.UUID.randomUUID().toString());
        }
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, a.getId());
            ps.setString(2, a.getNomService());
            ps.setBigDecimal(3, a.getMontantMensuel() != null ? a.getMontantMensuel() : BigDecimal.ZERO);

            if (a.getDateDebut() != null) ps.setDate(4, Date.valueOf(a.getDateDebut()));
            else ps.setNull(4, Types.DATE);

            if (a.getDateFin() != null) ps.setDate(5, Date.valueOf(a.getDateFin()));
            else ps.setNull(5, Types.DATE);

            if (a.getStatut() != null) ps.setString(6, a.getStatut().name());
            else ps.setNull(6, Types.VARCHAR);

            String type = (a instanceof AbonnementAvecEngagement) ? "AVEC_ENGAGEMENT" : "SANS_ENGAGEMENT";
            ps.setString(7, type);

            if (a instanceof AbonnementAvecEngagement) {
                ps.setInt(8, ((AbonnementAvecEngagement) a).getDureeEngagementMois());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.executeUpdate();
            return a;
        }
    }

    @Override
    public Optional<Abonnement> findById(String id) throws SQLException {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Abonnement> findAll() throws SQLException {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Abonnement> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    @Override
    public boolean update(Abonnement a) throws SQLException {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, a.getNomService());
            ps.setBigDecimal(2, a.getMontantMensuel() != null ? a.getMontantMensuel() : BigDecimal.ZERO);

            if (a.getDateDebut() != null) ps.setDate(3, Date.valueOf(a.getDateDebut()));
            else ps.setNull(3, Types.DATE);

            if (a.getDateFin() != null) ps.setDate(4, Date.valueOf(a.getDateFin()));
            else ps.setNull(4, Types.DATE);

            if (a.getStatut() != null) ps.setString(5, a.getStatut().name());
            else ps.setNull(5, Types.VARCHAR);

            String type = (a instanceof AbonnementAvecEngagement) ? "AVEC_ENGAGEMENT" : "SANS_ENGAGEMENT";
            ps.setString(6, type);

            if (a instanceof AbonnementAvecEngagement) {
                ps.setInt(7, ((AbonnementAvecEngagement) a).getDureeEngagementMois());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, a.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_DELETE)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }




}