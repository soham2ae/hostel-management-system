package com.hostel.repository;

import com.hostel.entity.FoodTransaction;
import com.hostel.entity.Student;
import com.hostel.enums.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data access repository for {@link FoodTransaction}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>Aggregation methods on this repository are read-side projections
 * used for food revenue reconciliation (billed vs. collected vs.
 * pending). They do not persist derived values — nothing computed
 * here is written back to the database.</p>
 */
public interface FoodTransactionRepository extends JpaRepository<FoodTransaction, Long> {

    /**
     * Finds all food transactions belonging to a given student.
     *
     * @param student the student
     * @return food transactions for the student
     */
    List<FoodTransaction> findByStudent(Student student);

    /**
     * Finds all food transactions of a given food type.
     *
     * @param foodType the food type
     * @return food transactions of the given type
     */
    List<FoodTransaction> findByFoodType(FoodType foodType);

    /**
     * Calculates the total billed food revenue across all food
     * transactions.
     *
     * <p>Read-side projection; not a stored value.</p>
     *
     * @return the total billed food revenue, or {@code null} if there
     *         are no food transactions
     */
    @Query("SELECT SUM(ft.amountBilled) FROM FoodTransaction ft")
    BigDecimal sumTotalBilledFoodRevenue();

    /**
     * Calculates the total collected food revenue across all food
     * transactions.
     *
     * <p>Read-side projection; not a stored value. Uses
     * {@code COALESCE} so that transactions with a {@code null}
     * {@code amountPaid} (nothing yet collected) contribute zero
     * rather than nullifying the sum.</p>
     *
     * @return the total collected food revenue, or {@code null} if
     *         there are no food transactions
     */
    @Query("SELECT SUM(COALESCE(ft.amountPaid, 0)) FROM FoodTransaction ft")
    BigDecimal sumTotalCollectedFoodRevenue();

    /**
     * Calculates the total pending food revenue (billed minus
     * collected) across all food transactions.
     *
     * <p>Read-side projection; not a stored value.</p>
     *
     * @return the total pending food revenue, or {@code null} if
     *         there are no food transactions
     */
    @Query("SELECT SUM(ft.amountBilled - COALESCE(ft.amountPaid, 0)) FROM FoodTransaction ft")
    BigDecimal sumTotalPendingFoodRevenue();
}
