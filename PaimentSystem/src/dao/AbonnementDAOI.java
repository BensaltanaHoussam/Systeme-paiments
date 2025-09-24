package dao;

import entities.Abonnement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AbonnementDAOI {
    Abonnement create(Abonnement a) throws SQLException;
    Optional<Abonnement> findById(String id) throws SQLException;
    List<Abonnement> findAll() throws SQLException;
    boolean update(Abonnement a) throws SQLException;
    boolean delete(String id) throws SQLException;
    List<Abonnement> findActiveSubscriptions() throws SQLException;
}
