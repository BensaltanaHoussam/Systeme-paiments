package dao;

import entities.Paiement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaiementDAOI {
    Paiement create(Paiement p) throws SQLException;
    Optional<Paiement> findById(String idPaiement) throws SQLException;
    List<Paiement> findByAbonnement(String idAbonnement) throws SQLException;
    List<Paiement> findAll() throws SQLException;
    boolean update(Paiement p) throws SQLException;
    boolean delete(String idPaiement) throws SQLException;
    List<Paiement> findUnpaidByAbonnement(String idAbonnement) throws SQLException;
    List<Paiement> findLastPayments(int limit) throws SQLException;
    List<Paiement> findBetweenDueDates(LocalDate start, LocalDate end) throws SQLException;
}
