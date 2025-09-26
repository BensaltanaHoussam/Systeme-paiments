// java
package ui;

import dao.AbonnementDAOI;
import dao.AbonnementDAOImpl;
import dao.PaiementDAOI;
import dao.PaiementDAOImpl;
import services.AbonnementService;
import services.PaiementService;

public class Main {

    private final AbonnementService abonnementService;
    private final PaiementService paiementService;

    public Main() {
        AbonnementDAOI abonnementDAO = new AbonnementDAOImpl();
        PaiementDAOI paiementDAO = new PaiementDAOImpl();
        this.abonnementService = new AbonnementService(abonnementDAO);
        this.paiementService = new PaiementService(paiementDAO, abonnementDAO);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        AbonnementMenu aboMenu = new AbonnementMenu(abonnementService);
        PaiementMenu payMenu = new PaiementMenu(paiementService);

        while (true) {
            Console.header("Application - Menu principal");
            System.out.println("1) Gestion des abonnements");
            System.out.println("2) Gestion des paiements");
            Console.line();
            System.out.println("0) Quitter");
            String ch = Console.prompt("Choix: ");
            switch (ch) {
                case "1": aboMenu.show(); break;
                case "2": payMenu.show(); break;
                case "0": System.out.println("Fin."); return;
                default: System.out.println("Choix inconnu.");
            }
        }
    }
}
