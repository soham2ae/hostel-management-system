package com.hostel.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service contract for aggregating dashboard KPIs across the system.
 *
 * <p>This service is purely a read-side aggregator. It computes and
 * combines values already derivable from other services and
 * repositories — student counts, occupancy, revenue, pending amounts,
 * food revenue, and wallet statistics — without owning or mutating any
 * data itself.</p>
 *
 * <p>All monetary and percentage values returned here are computed
 * dynamically; none are persisted. Every aggregate that could
 * otherwise be {@code null} (e.g. a {@code SUM} over zero rows) is
 * coalesced to {@link BigDecimal#ZERO} before being returned.</p>
 *
 * <p><b>Intentionally omitted:</b> revenue broken down by room
 * typology is not exposed by this service. {@code Payment} rows have
 * no relationship to room typology in the frozen schema — the only
 * available path, {@code Payment -> Student -> current Bed -> Room},
 * reflects a student's <i>current</i> room assignment, not the room
 * typology in effect at the time each payment was made. Computing
 * this KPI via that path would silently misattribute revenue for any
 * student who changed rooms or checked out after paying. This KPI is
 * blocked pending a schema change that links payments to room
 * typology at the time of payment, and is deliberately not
 * approximated here.</p>
 */
public interface DashboardService {

    /**
     * Returns student KPIs: total students, active students, confirmed
     * students, and mid-season students.
     *
     * @return a map of KPI label to count
     */
    Map<String, Long> getStudentKpis();

    /**
     * Returns occupancy KPIs: total beds, occupied beds, vacant beds,
     * and occupancy percentage.
     *
     * <p>Occupancy percentage is computed dynamically from live bed
     * statuses and is never stored.</p>
     *
     * @return a map of KPI label to value
     */
    Map<String, BigDecimal> getOccupancyKpis();

    /**
     * Returns revenue KPIs: total commercial amount, collected revenue,
     * and pending amount.
     *
     * <p>Pending amount is computed dynamically as the difference
     * between total commercial amounts and collected payments, and is
     * never stored. This is a system-wide aggregate only — per-student
     * pending balance analysis is outside the scope of this service.</p>
     *
     * @return a map of KPI label to value
     */
    Map<String, BigDecimal> getRevenueKpis();

    /**
     * Returns total collected revenue broken down by payment mode
     * (Cash, UPI, Card, Bank Transfer, Cheque).
     *
     * @return a map of payment mode display label to total collected
     *         amount
     */
    Map<String, BigDecimal> getPaymentModeBreakdown();

    /**
     * Returns food revenue KPIs: total billed, total collected, and
     * total pending food revenue.
     *
     * @return a map of KPI label to value
     */
    Map<String, BigDecimal> getFoodRevenueKpis();

    /**
     * Returns wallet statistics: total wallet balance across all
     * students, total top-up amount, and total debit amount.
     *
     * @return a map of KPI label to value
     */
    Map<String, BigDecimal> getWalletStatistics();
}
