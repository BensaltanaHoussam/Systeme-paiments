package services;

import dao.AbonnementDAOI;
import dao.PaiementDAOI;
import entities.Abonnement;
import entities.Paiement;
import enums.StatutPaiement;
import utils.DateUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PaiementService {

    private final PaiementDAOI paiementDAO;
    private final AbonnementDAOI abonnementDAO;

    public PaiementService(PaiementDAOI paiementDAO, AbonnementDAOI abonnementDAO) {
        this.paiementDAO = Objects.requireNonNull(paiementDAO);
        this.abonnementDAO = Objects.requireNonNull(abonnementDAO);
    }

    public Paiement create(Paiement p) throws SQLException {
        validate(p, true);
        return paiementDAO.create(p);
    }

    public Optional<Paiement> findById(String id) throws SQLException {
        return paiementDAO.findById(id);
    }

    public List<Paiement> findByAbonnement(String aboId) throws SQLException {
        return paiementDAO.findByAbonnement(aboId);
    }

    public boolean update(Paiement p) throws SQLException {
        validate(p, false);
        return paiementDAO.update(p);
    }

    public boolean delete(String id) throws SQLException {
        if (blank(id)) throw new IllegalArgumentException("idPaiement requis");
        return paiementDAO.delete(id);
    }

    public List<Paiement> generateEcheances(String abonnementId) throws SQLException {
        Abonnement abo = abonnementDAO.findById(abonnementId)
                .orElseThrow(() -> new IllegalArgumentException("Abonnement introuvable"));
        LocalDate start = abo.getDateDebut();
        LocalDate end = abo.getDateFin() != null ? abo.getDateFin() : start.plusMonths(12);
        List<LocalDate> dates = DateUtils.monthlyDates(start, end);
        List<Paiement> existing = paiementDAO.findByAbonnement(abonnementId);
        List<Paiement> created = new ArrayList<>();
        for (LocalDate d : dates) {
            boolean exists = existing.stream().anyMatch(p -> p.getDateEcheance().equals(d));
            if (!exists) {
                Paiement p = new Paiement(abonnementId, d, null, "AUTO", StatutPaiement.NON_PAYE);
                paiementDAO.create(p);
                created.add(p);
            }
        }
        return created;
    }

    public Paiement pay(String paiementId, LocalDate datePaiement, String type) throws SQLException {
        Paiement p = paiementDAO.findById(paiementId)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable"));
        p.setDatePaiement(datePaiement != null ? datePaiement : LocalDate.now());
        p.setTypePaiement(type != null ? type : "MANUEL");
        p.setStatut(StatutPaiement.PAYE);
        paiementDAO.update(p);
        return p;
    }

    public int updateOverdueStatuses() throws SQLException {
        LocalDate today = LocalDate.now();
        List<Paiement> all = paiementDAO.findAll();
        int changed = 0;
        for (Paiement p : all) {
            if (p.getStatut() != StatutPaiement.PAYE &&
                    p.getDatePaiement() == null &&
                    p.getDateEcheance().isBefore(today)) {
                p.setStatut(StatutPaiement.EN_RETARD);
                paiementDAO.update(p);
                changed++;
            }
        }
        return changed;
    }

    public List<Paiement> unpaidByAbonnement(String aboId) throws SQLException {
        updateOverdueStatuses();
        return paiementDAO.findUnpaidByAbonnement(aboId);
    }

    public BigDecimal totalUnpaidAmount(String aboId) throws SQLException {
        Abonnement abo = abonnementDAO.findById(aboId)
                .orElseThrow(() -> new IllegalArgumentException("Abonnement introuvable"));
        BigDecimal unit = abo.getMontantMensuel();
        long count = unpaidByAbonnement(aboId).stream()
                .filter(p -> p.getStatut() != StatutPaiement.PAYE)
                .count();
        return unit.multiply(BigDecimal.valueOf(count));
    }

    public BigDecimal totalPaidForAbonnement(String aboId) throws SQLException {
        Abonnement abo = abonnementDAO.findById(aboId)
                .orElseThrow(() -> new IllegalArgumentException("Abonnement introuvable"));
        BigDecimal unit = abo.getMontantMensuel();
        long count = paiementDAO.findByAbonnement(aboId).stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .count();
        return unit.multiply(BigDecimal.valueOf(count));
    }

    public List<Paiement> lastPayments(int limit) throws SQLException {
        return paiementDAO.findLastPayments(limit);
    }

    public Map<String,Object> monthlyReport(int year, int month) throws SQLException {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        List<Paiement> list = paiementDAO.findBetweenDueDates(start, end);
        updateStatusesInline(list);

        long payes = list.stream().filter(p -> p.getStatut()==StatutPaiement.PAYE).count();
        long retard = list.stream().filter(p -> p.getStatut()==StatutPaiement.EN_RETARD).count();
        long nonPayes = list.stream().filter(p -> p.getStatut()==StatutPaiement.NON_PAYE).count();

        BigDecimal montantPayes = sumAmount(list, StatutPaiement.PAYE);
        BigDecimal montantPotentiel = sumAmount(list, null);

        Map<String,Object> m = new LinkedHashMap<>();
        m.put("periode", year + "-" + (month < 10 ? "0"+month : month));
        m.put("totalEcheances", list.size());
        m.put("payes", payes);
        m.put("enRetard", retard);
        m.put("nonPayes", nonPayes);
        m.put("montantPaye", montantPayes);
        m.put("montantPotentiel", montantPotentiel);
        return m;
    }

    public Map<String,Object> annualReport(int year) throws SQLException {
        Map<Integer, Map<String,Object>> mois = new LinkedHashMap<>();
        for (int m=1; m<=12; m++) {
            mois.put(m, monthlyReport(year, m));
        }
        BigDecimal total = mois.values().stream()
                .map(v -> (BigDecimal) v.get("montantPaye"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String,Object> r = new LinkedHashMap<>();
        r.put("annee", year);
        r.put("mois", mois);
        r.put("totalPaye", total);
        return r;
    }

    private BigDecimal sumAmount(List<Paiement> list, StatutPaiement filter) throws SQLException {
        if (list.isEmpty()) return BigDecimal.ZERO;
        Map<String, Long> counts = list.stream()
                .filter(p -> filter == null || p.getStatut()==filter)
                .collect(Collectors.groupingBy(Paiement::getIdAbonnement, Collectors.counting()));
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Long> e : counts.entrySet()) {
            Abonnement abo = abonnementDAO.findById(e.getKey()).orElse(null);
            if (abo != null) {
                total = total.add(abo.getMontantMensuel().multiply(BigDecimal.valueOf(e.getValue())));
            }
        }
        return total;
    }

    private void updateStatusesInline(List<Paiement> list) {
        LocalDate today = LocalDate.now();
        list.stream()
                .filter(p -> p.getStatut()!=StatutPaiement.PAYE &&
                        p.getDatePaiement()==null &&
                        p.getDateEcheance().isBefore(today))
                .forEach(p -> p.setStatut(StatutPaiement.EN_RETARD));
    }

    private void validate(Paiement p, boolean creating) {
        if (p == null) throw new IllegalArgumentException("Paiement null");
        if (creating && (p.getIdPaiement()==null || p.getIdPaiement().isEmpty()))
            throw new IllegalArgumentException("idPaiement manquant");
        if (blank(p.getIdAbonnement())) throw new IllegalArgumentException("idAbonnement requis");
        if (p.getDateEcheance()==null) throw new IllegalArgumentException("dateEcheance requise");
        if (p.getStatut()==null) throw new IllegalArgumentException("statut requis");
    }

    private boolean blank(String s) { return s==null || s.trim().isEmpty(); }
}
