// java
package ui;

import entities.Paiement;
import services.PaiementService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PaiementMenu {

    private final PaiementService paiementService;

    public PaiementMenu(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    public void show() {
        while (true) {
            Console.header("Gestion des paiements");
            System.out.println("1) Générer échéances d'un abonnement");
            System.out.println("2) Lister paiements d'un abonnement");
            System.out.println("3) Payer une échéance");
            System.out.println("4) Impayés + total impayé");
            System.out.println("5) Total déjà payé pour un abonnement");
            System.out.println("6) Derniers 5 paiements");
            System.out.println("7) Rapport mensuel");
            System.out.println("8) Rapport annuel");
            Console.line();
            System.out.println("0) Retour");
            String ch = Console.prompt("Choix: ");
            try {
                switch (ch) {
                    case "1": genEcheances(); break;
                    case "2": listPaiements(); break;
                    case "3": payer(); break;
                    case "4": impayes(); break;
                    case "5": totalPaye(); break;
                    case "6": last5(); break;
                    case "7": rapportMensuel(); break;
                    case "8": rapportAnnuel(); break;
                    case "0": return;
                    default: System.out.println("Choix inconnu.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
            Console.pause();
        }
    }

    private void genEcheances() throws SQLException {
        String aboId = Console.prompt("ID abonnement: ");
        List<Paiement> created = paiementService.generateEcheances(aboId);
        System.out.println("Échéances créées: " + created.size());
    }

    private void listPaiements() throws SQLException {
        String aboId = Console.prompt("ID abonnement: ");
        List<Paiement> list = paiementService.findByAbonnement(aboId);
        if (list.isEmpty()) {
            System.out.println("(Aucun paiement)");
            return;
        }
        list.forEach(this::printPaiement);
    }

    private void payer() throws SQLException {
        String id = Console.prompt("ID paiement: ");
        LocalDate d = Console.promptDate("Date paiement (vide=aujourd'hui): ", true);
        String type = Console.prompt("Type (ex: CB/SEPA) (vide=MANUEL): ");
        Paiement p = paiementService.pay(id, d != null ? d : LocalDate.now(), type.isEmpty() ? "MANUEL" : type);
        System.out.println("Payé:");
        printPaiement(p);
    }

    private void impayes() throws SQLException {
        String aboId = Console.prompt("ID abonnement: ");
        List<Paiement> list = paiementService.unpaidByAbonnement(aboId);
        if (list.isEmpty()) {
            System.out.println("Aucun impayé.");
        } else {
            list.forEach(this::printPaiement);
        }
        System.out.println("Montant total impayé: " + paiementService.totalUnpaidAmount(aboId));
    }

    private void totalPaye() throws SQLException {
        String aboId = Console.prompt("ID abonnement: ");
        System.out.println("Total payé: " + paiementService.totalPaidForAbonnement(aboId));
    }

    private void last5() throws SQLException {
        List<Paiement> list = paiementService.lastPayments(5);
        if (list.isEmpty()) {
            System.out.println("(Aucun)");
            return;
        }
        list.forEach(this::printPaiement);
    }

    private void rapportMensuel() throws SQLException {
        int year = Integer.parseInt(Console.prompt("Année: "));
        int month = Integer.parseInt(Console.prompt("Mois (1-12): "));
        Map<String,Object> r = paiementService.monthlyReport(year, month);
        r.forEach((k,v) -> System.out.println(k + ": " + v));
    }

    private void rapportAnnuel() throws SQLException {
        int year = Integer.parseInt(Console.prompt("Année: "));
        Map<String,Object> r = paiementService.annualReport(year);
        r.forEach((k,v) -> {
            if ("mois".equals(k)) {
                System.out.println("Détails mensuels:");
                @SuppressWarnings("unchecked")
                Map<Integer,Map<String,Object>> mm = (Map<Integer,Map<String,Object>>) v;
                mm.forEach((m,vals) -> {
                    System.out.println("  Mois " + m + ":");
                    vals.forEach((k2,v2) -> System.out.println("    " + k2 + "=" + v2));
                });
            } else {
                System.out.println(k + ": " + v);
            }
        });
    }

    private void printPaiement(Paiement p) {
        System.out.println("PaiementID=" + p.getIdPaiement()
                + " | abo=" + p.getIdAbonnement()
                + " | échéance=" + p.getDateEcheance()
                + " | payéLe=" + p.getDatePaiement()
                + " | type=" + p.getTypePaiement()
                + " | statut=" + p.getStatut());
    }
}
