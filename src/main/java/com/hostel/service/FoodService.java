package com.hostel.service;

import com.hostel.entity.Food;

/**
 * Service contract for managing {@link Food} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving a student's food plan for a given booking year.</p>
 */
public interface FoodService {

    /**
     * Creates a new food record for a student.
     *
     * <p>Resolves the owning {@link com.hostel.entity.Student} from
     * {@code studentId} and attaches it to the food record before
     * persisting, so that callers (typically controllers) need only
     * supply the parent identifier rather than a hydrated
     * {@link com.hostel.entity.Student} entity.</p>
     *
     * @param studentId the identifier of the student this food record
     *                  belongs to
     * @param food      the food record to create
     * @return the created food record, with its generated identifier populated
     */
    Food createFood(Long studentId, Food food);

    /**
     * Updates an existing food record.
     *
     * @param foodId the identifier of the food record to update
     * @param food   the updated food details
     * @return the updated food record
     */
    Food updateFood(Long foodId, Food food);

    /**
     * Finds a food record by its identifier.
     *
     * @param foodId the food record identifier
     * @return the matching food record
     */
    Food findFoodById(Long foodId);

    /**
     * Finds the food record belonging to a given student.
     *
     * @param studentId the student identifier
     * @return the matching food record
     */
    Food findFoodByStudent(Long studentId);
}
