package com.hostel.service.impl;

import com.hostel.entity.Bed;
import com.hostel.enums.BedStatus;
import com.hostel.enums.PaymentMode;
import com.hostel.enums.StudentStatus;
import com.hostel.repository.BedRepository;
import com.hostel.repository.CommercialPlanRepository;
import com.hostel.repository.FoodTransactionRepository;
import com.hostel.repository.PaymentRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.repository.WalletRepository;
import com.hostel.repository.WalletTransactionRepository;
import com.hostel.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link DashboardService}.
 *
 * <p>Aggregates KPIs across student, occupancy, revenue, food, and
 * wallet data. Depends directly on repositories rather than other
 * services — an intentional, approved exception, since this service
 * owns no business aggregate of its own and exists purely to roll up
 * read-side data already owned elsewhere.</p>
 *
 * <p>Every {@code SUM}-based aggregate retrieved from a repository is
 * coalesced from {@code null} to {@link BigDecimal#ZERO} immediately
 * after retrieval, before any further arithmetic is performed.</p>
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final BedRepository bedRepository;
    private final CommercialPlanRepository commercialPlanRepository;
    private final PaymentRepository paymentRepository;
    private final FoodTransactionRepository foodTransactionRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    /**
     * Constructs a new {@code DashboardServiceImpl}.
     *
     * @param studentRepository           the student repository
     * @param bedRepository               the bed repository
     * @param commercialPlanRepository    the commercial plan repository
     * @param paymentRepository           the payment repository
     * @param foodTransactionRepository   the food transaction repository
     * @param walletRepository            the wallet repository
     * @param walletTransactionRepository the wallet transaction repository
     */
    public DashboardServiceImpl(StudentRepository studentRepository,
                                 BedRepository bedRepository,
                                 CommercialPlanRepository commercialPlanRepository,
                                 PaymentRepository paymentRepository,
                                 FoodTransactionRepository foodTransactionRepository,
                                 WalletRepository walletRepository,
                                 WalletTransactionRepository walletTransactionRepository) {
        this.studentRepository = studentRepository;
        this.bedRepository = bedRepository;
        this.commercialPlanRepository = commercialPlanRepository;
        this.paymentRepository = paymentRepository;
        this.foodTransactionRepository = foodTransactionRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getStudentKpis() {
        Map<String, Long> kpis = new LinkedHashMap<>();
        kpis.put("totalStudents", studentRepository.count());
        kpis.put("activeStudents", (long) studentRepository.findByStatus(StudentStatus.ACTIVE).size());
        kpis.put("confirmedStudents", (long) studentRepository.findByStatus(StudentStatus.CONFIRMED).size());
        kpis.put("midSeasonStudents", (long) studentRepository.findByStatus(StudentStatus.MID_SEASON).size());
        return kpis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getOccupancyKpis() {
        List<Bed> allBeds = bedRepository.findAll();
        long totalBeds = allBeds.size();
        long occupiedBeds = allBeds.stream()
                .filter(bed -> bed.getStatus() == BedStatus.OCCUPIED)
                .count();
        long vacantBeds = totalBeds - occupiedBeds;

        BigDecimal occupancyPercentage;
        if (totalBeds == 0) {
            occupancyPercentage = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            occupancyPercentage = BigDecimal.valueOf(occupiedBeds)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalBeds), 2, RoundingMode.HALF_UP);
        }

        Map<String, BigDecimal> kpis = new LinkedHashMap<>();
        kpis.put("totalBeds", BigDecimal.valueOf(totalBeds));
        kpis.put("occupiedBeds", BigDecimal.valueOf(occupiedBeds));
        kpis.put("vacantBeds", BigDecimal.valueOf(vacantBeds));
        kpis.put("occupancyPercentage", occupancyPercentage);
        return kpis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getRevenueKpis() {
        BigDecimal totalCommercialAmount = nullToZero(commercialPlanRepository.sumTotalCommercialAmount());
        BigDecimal collectedRevenue = nullToZero(paymentRepository.sumTotalCollectedRevenue());
        BigDecimal pendingAmount = totalCommercialAmount.subtract(collectedRevenue);

        Map<String, BigDecimal> kpis = new LinkedHashMap<>();
        kpis.put("totalCommercialAmount", totalCommercialAmount);
        kpis.put("collectedRevenue", collectedRevenue);
        kpis.put("pendingAmount", pendingAmount);
        return kpis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getPaymentModeBreakdown() {
        List<Object[]> rows = paymentRepository.sumCollectedRevenueByPaymentMode();

        Map<String, BigDecimal> breakdown = new LinkedHashMap<>();
        for (PaymentMode mode : PaymentMode.values()) {
            breakdown.put(mode.getDisplayLabel(), BigDecimal.ZERO);
        }

        for (Object[] row : rows) {
            PaymentMode mode = (PaymentMode) row[0];
            BigDecimal total = nullToZero((BigDecimal) row[1]);
            breakdown.put(mode.getDisplayLabel(), total);
        }

        return breakdown;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getFoodRevenueKpis() {
        BigDecimal totalBilled = nullToZero(foodTransactionRepository.sumTotalBilledFoodRevenue());
        BigDecimal totalCollected = nullToZero(foodTransactionRepository.sumTotalCollectedFoodRevenue());
        BigDecimal totalPending = nullToZero(foodTransactionRepository.sumTotalPendingFoodRevenue());

        Map<String, BigDecimal> kpis = new LinkedHashMap<>();
        kpis.put("totalBilledFoodRevenue", totalBilled);
        kpis.put("totalCollectedFoodRevenue", totalCollected);
        kpis.put("totalPendingFoodRevenue", totalPending);
        return kpis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getWalletStatistics() {
        BigDecimal totalCurrentBalance = nullToZero(walletRepository.sumTotalCurrentBalance());
        BigDecimal totalTopupAmount = nullToZero(walletTransactionRepository.sumTotalTopupAmount());
        BigDecimal totalDebitAmount = nullToZero(walletTransactionRepository.sumTotalDebitAmount());

        Map<String, BigDecimal> kpis = new LinkedHashMap<>();
        kpis.put("totalWalletBalance", totalCurrentBalance);
        kpis.put("totalTopupAmount", totalTopupAmount);
        kpis.put("totalDebitAmount", totalDebitAmount);
        return kpis;
    }

    /**
     * Converts a {@code null} aggregate result to {@link BigDecimal#ZERO}.
     *
     * @param value the aggregate value, possibly {@code null}
     * @return {@code value} if non-null, otherwise {@link BigDecimal#ZERO}
     */
    private BigDecimal nullToZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
