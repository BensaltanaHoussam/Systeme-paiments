// java
package ui;

import entities.Abonnement;
import entities.AbonnementAvecEngagement;
import entities.AbonnementSansEngagement;
import enums.StatutAbonnement;
import services.AbonnementService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AbonnementMenu {

    private final AbonnementService service;

    public AbonnementMenu(AbonnementService service) {
        this.service = service;
    }

    public void show() {
        while (true) {
            Console.header("Gestion des abonnements");
            System.out.println("1) Créer abonnement");
            System.out.println("2) Lister abonnements");
            System.out.println("3) Rechercher par ID");
            System.out.println("4) Mettre à jour statut");
            System.out.println("5) Supprimer");
            System.out.println("6) Lister abonnements actifs");
            Console.line();
            System.out.println("0) Retour");
            String ch = Console.prompt("Choix: ");
            try {
                switch (ch) {
                    case "1": create(); break;
                    case "2": listAll(); break;
                    case "3": findById(); break;
                    case "4": updateStatut(); break;
                    case "5": deleteById(); break;
                    case "6": listActive(); break;
                    case "0": return;
                    default: System.out.println("Choix inconnu.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
            Console.pause();
        }
    }

    private void create() throws SQLException {
        String nom = Console.prompt("Nom service: ");
        BigDecimal montant = Console.promptBigDecimal("Montant mensuel: ");
        LocalDate debut = Console.promptDate("Date début (yyyy-MM-dd): ", false);
        LocalDate fin = Console.promptDate("Date fin (optionnel): ", true);
        StatutAbonnement statut = promptStatut();
        String type = Console.prompt("Type (1=avec engagement / autre=sans): ");

        Abonnement a;
        if ("1".equals(type)) {
            int duree = Integer.parseInt(Console.prompt("Durée engagement (mois): "));
            a = new AbonnementAvecEngagement(nom, montant, debut, fin, statut, duree);
        } else {
            a = new AbonnementSansEngagement(nom, montant, debut, fin, statut);
        }
        a = service.create(a);
        System.out.println("Créé. ID=" + a.getId());
    }

    private void listAll() throws SQLException {
        List<Abonnement> list = service.findAll();
        if (list.isEmpty()) {
            System.out.println("(Aucun)");
            return;
        }
        list.forEach(this::printAbonnement);
    }

    private void findById() throws SQLException {
        String id = Console.prompt("ID: ");
        Optional<Abonnement> opt = service.findById(id);
        if (!opt.isPresent()) {
            System.out.println("Introuvable");
            return;
        }
        printAbonnement(opt.get());
    }

    private void updateStatut() throws SQLException {
        String id = Console.prompt("ID: ");
        Optional<Abonnement> opt = service.findById(id);
        if (!opt.isPresent()) {
            System.out.println("Introuvable.");
            return;
        }
        StatutAbonnement nouveau = promptStatut();
        Abonnement a = opt.get();
        a.setStatut(nouveau);
        boolean ok = service.update(a);
        System.out.println(ok ? "Statut mis à jour." : "Aucune mise à jour.");
    }

    private void deleteById() throws SQLException {
        String id = Console.prompt("ID: ");
        boolean ok = service.delete(id);
        System.out.println(ok ? "Supprimé." : "Aucune suppression.");
    }

    private void listActive() throws SQLException {
        List<Abonnement> list = service.findActiveSubscriptions();
        if (list.isEmpty()) {
            System.out.println("(Aucun actif)");
            return;
        }
        list.forEach(this::printAbonnement);
    }

    private void printAbonnement(Abonnement a) {
        System.out.println("ID=" + a.getId()
                + " | service=" + a.getNomService()
                + " | montant=" + a.getMontantMensuel()
                + " | début=" + a.getDateDebut()
                + " | fin=" + a.getDateFin()
                + " | statut=" + a.getStatut()
                + " | type=" + a.getTypeAbonnement()
                + " | engagementMois=" + a.getDureeEngagementMois());
    }

    private StatutAbonnement promptStatut() {
        while (true) {
            String s = Console.prompt("Statut (ACTIVE/SUSPENDU/RESILIE): ").toUpperCase(Locale.ROOT);
            try {
                return StatutAbonnement.valueOf(s);
            } catch (Exception e) {
                System.out.println("Valeur invalide.");
            }
        }
    }
}
