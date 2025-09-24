package ui;

import dao.AbonnementDAOI;
import dao.AbonnementDAOImpl;
import entities.Abonnement;
import entities.AbonnementAvecEngagement;
import entities.AbonnementSansEngagement;
import enums.StatutAbonnement;
import services.AbonnementService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private final Scanner sc = new Scanner(System.in);
    private final AbonnementService service;

    public Main() {
        AbonnementDAOI dao = new AbonnementDAOImpl();
        this.service = new AbonnementService(dao);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            System.out.println("\n==== Menu Abonnements ====");
            System.out.println("1\\) Lister tous");
            System.out.println("2\\) Créer \\(sans engagement\\)");
            System.out.println("3\\) Créer \\(avec engagement\\)");
            System.out.println("4\\) Rechercher par ID");
            System.out.println("5\\) Mettre à jour le statut");
            System.out.println("6\\) Supprimer par ID");
            System.out.println("7\\) Lister actifs \\(dates valides\\)");
            System.out.println("0\\) Quitter");
            System.out.print("Choix: ");
            String ch = sc.nextLine().trim();

            try {
                switch (ch) {
                    case "1":
                        listAll();
                        break;
                    case "2":
                        createSansEngagement();
                        break;
                    case "3":
                        createAvecEngagement();
                        break;
                    case "4":
                        findById();
                        break;
                    case "5":
                        updateStatut();
                        break;
                    case "6":
                        deleteById();
                        break;
                    case "7":
                        listActive();
                        break;
                    case "0":
                        System.out.println("Au revoir.");
                        return;
                    default:
                        System.out.println("Choix invalide.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }


    private void listAll() throws SQLException {
        List<Abonnement> all = service.findAll();
        if (all.isEmpty()) {
            System.out.println("\\- Aucun abonnement.");
            return;
        }
        all.forEach(this::printAbonnement);
    }

    private void listActive() throws SQLException {
        List<Abonnement> actifs = service.findActiveSubscriptions();
        if (actifs.isEmpty()) {
            System.out.println("\\- Aucun abonnement actif.");
            return;
        }
        actifs.forEach(this::printAbonnement);
    }

    private void createSansEngagement() throws SQLException {
        String nom = prompt("Nom du service: ");
        BigDecimal montant = promptBigDecimal("Montant mensuel \\(ex: 12.99\\): ");
        LocalDate debut = promptDate("Date début \\(yyyy-MM-dd\\): ", true);
        LocalDate fin = promptDate("Date fin \\(yyyy-MM-dd ou vide\\): ", false);
        StatutAbonnement statut = promptStatut();

        Abonnement a = new AbonnementSansEngagement(nom, montant, debut, fin, statut);
        a = service.create(a);
        System.out.println("Créé avec ID: " + a.getId());
    }

    private void createAvecEngagement() throws SQLException {
        String nom = prompt("Nom du service: ");
        BigDecimal montant = promptBigDecimal("Montant mensuel \\(ex: 12.99\\): ");
        LocalDate debut = promptDate("Date début \\(yyyy-MM-dd\\): ", true);
        LocalDate fin = promptDate("Date fin \\(yyyy-MM-dd ou vide\\): ", false);
        StatutAbonnement statut = promptStatut();
        int duree = promptInt("Durée engagement en mois: ");

        Abonnement a = new AbonnementAvecEngagement(nom, montant, debut, fin, statut, duree);
        a = service.create(a);
        System.out.println("Créé avec ID: " + a.getId());
    }

    private void findById() throws SQLException {
        String id = prompt("ID: ");
        Optional<Abonnement> opt = service.findById(id);
        if (!opt.isPresent()) {
            System.out.println("\\- Introuvable.");
        } else {
            printAbonnement(opt.get());
        }
    }

    private void updateStatut() throws SQLException {
        String id = prompt("ID: ");
        Optional<Abonnement> opt = service.findById(id);
        if (!opt.isPresent()) {
            System.out.println("\\- Introuvable.");
            return;
        }
        StatutAbonnement nouveau = promptStatut();
        Abonnement a = opt.get();
        a.setStatut(nouveau);
        boolean ok = service.update(a);
        System.out.println(ok ? "Statut mis à jour." : "Aucune mise à jour.");
    }

    private void deleteById() throws SQLException {
        String id = prompt("ID: ");
        boolean ok = service.delete(id);
        System.out.println(ok ? "Supprimé." : "Aucune suppression.");
    }

    private void printAbonnement(Abonnement a) {
        System.out.println("ID=" + a.getId()
                + ", service=" + a.getNomService()
                + ", montant=" + a.getMontantMensuel()
                + ", début=" + a.getDateDebut()
                + ", fin=" + a.getDateFin()
                + ", statut=" + a.getStatut()
                + ", type=" + a.getTypeAbonnement()
                + ", engagementMois=" + a.getDureeEngagementMois());
    }

    private String prompt(String label) {
        System.out.print(label);
        return sc.nextLine().trim();
    }

    private BigDecimal promptBigDecimal(String label) {
        while (true) {
            try {
                String s = prompt(label);
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Entrée invalide \\(nombre\\).");
            }
        }
    }

    private LocalDate promptDate(String label, boolean required) {
        while (true) {
            String s = prompt(label);
            if (!s.isEmpty()) {
                try {
                    return LocalDate.parse(s);
                } catch (Exception e) {
                    System.out.println("Format invalide \\(yyyy-MM-dd\\).");
                }
            } else if (!required) {
                return null;
            } else {
                System.out.println("Valeur requise.");
            }
        }
    }

    private int promptInt(String label) {
        while (true) {
            try {
                String s = prompt(label);
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println("Entrée invalide \\(entier\\).");
            }
        }
    }

    private StatutAbonnement promptStatut() {
        while (true) {
            String s = prompt("Statut \\(ACTIVE, SUSPENDU, RESILIE\\): ").toUpperCase();
            try {
                return StatutAbonnement.valueOf(s);
            } catch (Exception e) {
                System.out.println("Statut invalide.");
            }
        }
    }
}