package com.hostel.entity;

import com.hostel.enums.BedStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a bed belonging to a {@link Room}.
 *
 * <p>Sits at the bottom of the location hierarchy:
 * {@code Hostel -> Floor -> Room -> Bed}.</p>
 *
 * <p>A bed may currently be occupied by zero or one {@link Student}.
 * {@link Student} owns the foreign key ({@code bed_id}), so this is
 * the inverse side of that one-to-one relationship.</p>
 *
 * <p>Upon checkout, {@code student.bed} is set to {@code null} and
 * this bed's {@link #status} must be updated to {@link BedStatus#VACANT}.
 * The historical assignment is preserved separately via
 * {@link StudentRoomHistory}.</p>
 *
 * <p>This is a master table. No cascade removal is applied — removing
 * a bed must never implicitly affect student or history records.</p>
 */
@Entity
@Table(name = "bed")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Bed {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Long bedId;

    /**
     * The room this bed is located in.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @ToString.Exclude
    private Room room;

    /**
     * Bed identifier within the room (e.g. "A", "B", "C").
     */
    @Column(name = "bed_no", nullable = false)
    private String bedNo;

    /**
     * Current occupancy status of this bed.
     *
     * <p>This is the source of truth for room-level availability,
     * which must always be derived dynamically and never stored
     * on {@link Room}.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BedStatus status;

    /**
     * The student currently occupying this bed, if any.
     *
     * <p>Inverse side of the relationship; {@link Student} owns the
     * foreign key ({@code bed_id}), which is nullable to support
     * checkout/cancellation.</p>
     */
    @OneToOne(mappedBy = "bed", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Student student;
}
