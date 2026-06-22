package com.hostel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Represents a historical record of a {@link Student}'s room/bed
 * assignment for a given period.
 *
 * <p><b>Important:</b> this table is strictly append-only. Records
 * must be preserved indefinitely, even after a student checks out or
 * the underlying room/bed assignment changes. No cascade removal must
 * ever be applied that could delete these rows as a side effect of
 * deleting a {@link Student}, {@link Room}, or {@link Bed}.</p>
 *
 * <p>This is the durable record of where a student lived across
 * multiple booking years, independent of the student's current
 * {@link Student#getBed()} assignment, which is cleared on checkout.</p>
 */
@Entity
@Table(name = "student_room_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StudentRoomHistory {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    /**
     * The student this history record belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    /**
     * The room occupied during this period.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @ToString.Exclude
    private Room room;

    /**
     * The bed occupied during this period.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    @ToString.Exclude
    private Bed bed;

    /**
     * Date this room/bed assignment began.
     */
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    /**
     * Date this room/bed assignment ended, if applicable.
     *
     * <p>{@code null} while the assignment is still current.</p>
     */
    @Column(name = "to_date")
    private LocalDate toDate;
}
