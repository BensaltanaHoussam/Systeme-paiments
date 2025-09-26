package dao;

import entities.Paiement;
import enums.StatutPaiement;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaiementDAOImpl implements PaiementDAOI {

    private static final String SQL_INSERT =
            "INSERT INTO paiement (idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut) VALUES (?,?,?,?,?,?)";
    private static final String SQL_SELECT_ID = "SELECT * FROM paiement WHERE idPaiement = ?";
    private static final String SQL_SELECT_ABO = "SELECT * FROM paiement WHERE idAbonnement = ? ORDER BY dateEcheance";
    private static final String SQL_SELECT_ALL = "SELECT * FROM paiement ORDER BY dateEcheance DESC";
    private static final String SQL_UPDATE =
            "UPDATE paiement SET idAbonnement=?, dateEcheance=?, datePaiement=?, typePaiement=?, statut=? WHERE idPaiement=?";
    private static final String SQL_DELETE = "DELETE FROM paiement WHERE idPaiement=?";
    private static final String SQL_UNPAID_ABO =
            "SELECT * FROM paiement WHERE idAbonnement=? AND statut <> 'PAYE'";
    private static final String SQL_LAST =
            "SELECT * FROM paiement ORDER BY COALESCE(datePaiement, dateEcheance) DESC LIMIT ?";
    private static final String SQL_BETWEEN_DUE =
            "SELECT * FROM paiement WHERE dateEcheance BETWEEN ? AND ? ORDER BY dateEcheance";

    @Override
    public Paiement create(Paiement p) throws SQLException {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT)) {
            ps.setString(1, p.getIdPaiement());
            ps.setString(2, p.getIdAbonnement());
            ps.setDate(3, Date.valueOf(p.getDateEcheance()));
            if (p.getDatePaiement() != null) ps.setDate(4, Date.valueOf(p.getDatePaiement()));
            else ps.setNull(4, Types.DATE);
            ps.setString(5, p.getTypePaiement());
            ps.setString(6, p.getStatut().name());
            ps.executeUpdate();
            return p;
        }
    }

    @Override
    public Optional<Paiement> findById(String idPaiement) throws SQLException {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ID)) {
            ps.setString(1, idPaiement);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }












}
