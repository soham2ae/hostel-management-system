package com.hostel.service;

import com.hostel.entity.CommercialPlan;

/**
 * Service contract for managing {@link CommercialPlan} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving a student's commercial (financial) terms for a given
 * booking year.</p>
 *
 * <p>The security deposit is already included within
 * {@link CommercialPlan#getCommercialAmount()} and is never tracked
 * separately. {@link CommercialPlan#getBookingAmount()} and
 * {@link CommercialPlan#getMoveinAmount()} may both be {@code null}.</p>
 */
public interface CommercialPlanService {

    /**
     * Creates a new commercial plan for a student.
     *
     * <p>Resolves the owning {@link com.hostel.entity.Student} from
     * {@code studentId} and attaches it to the commercial plan before
     * persisting, so that callers (typically controllers) need only
     * supply the parent identifier rather than a hydrated
     * {@link com.hostel.entity.Student} entity.</p>
     *
     * @param studentId the identifier of the student this commercial
     *                  plan belongs to
     * @param plan      the commercial plan to create
     * @return the created commercial plan, with its generated identifier populated
     */
    CommercialPlan createCommercialPlan(Long studentId, CommercialPlan plan);

    /**
     * Updates an existing commercial plan.
     *
     * @param planId the identifier of the commercial plan to update
     * @param plan   the updated commercial plan details
     * @return the updated commercial plan
     */
    CommercialPlan updateCommercialPlan(Long planId, CommercialPlan plan);

    /**
     * Finds a commercial plan by its identifier.
     *
     * @param planId the commercial plan identifier
     * @return the matching commercial plan
     */
    CommercialPlan findCommercialPlanById(Long planId);

    /**
     * Finds the commercial plan belonging to a given student.
     *
     * @param studentId the student identifier
     * @return the matching commercial plan
     */
    CommercialPlan findCommercialPlanByStudent(Long studentId);
}
