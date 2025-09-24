package dao;

import entities.Abonnement;
import entities.AbonnementAvecEngagement;
import entities.AbonnementSansEngagement;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class AbonnementDAOImpl implements AbonnementDAOI {

    private static final String SQL_INSERT = "INSERT INTO abonnement " +
            "(id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID = "SELECT id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois " +
            "FROM abonnement WHERE id = ?";

    private static final String SQL_SELECT_ALL = "SELECT id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois " +
            "FROM abonnement ORDER BY dateDebut DESC, id";

    private static final String SQL_UPDATE = "UPDATE abonnement SET " +
            "nomService = ?, montantMensuel = ?, dateDebut = ?, dateFin = ?, statut = ?, typeAbonnement = ?, dureeEngagementMois = ? " +
            "WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM abonnement WHERE id = ?";


    private static final String SQL_FIND_ACTIVE = "SELECT id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois " +
            "FROM abonnement " +
            "WHERE dateDebut <= CURRENT_DATE() AND (dateFin IS NULL OR dateFin >= CURRENT_DATE()) " +
            "ORDER BY dateDebut DESC";



}
