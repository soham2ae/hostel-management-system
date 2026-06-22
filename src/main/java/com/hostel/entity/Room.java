package com.hostel.entity;

import com.hostel.enums.RoomTypology;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room belonging to a {@link Floor}.
 *
 * <p>Sits between {@link Floor} and {@link Bed} in the location
 * hierarchy: {@code Hostel -> Floor -> Room -> Bed}.</p>
 *
 * <p><b>Important:</b> this entity intentionally has no
 * {@code room_status} / availability field. Room availability
 * (e.g. "2/3 Beds Available" or "BOOKED") must always be derived
 * dynamically from the statuses of its {@link Bed} records, never
 * stored on the room itself.</p>
 *
 * <p>This is a master table. No cascade removal is applied to its
 * {@link Bed} collection — deleting a room must never implicitly
 * delete its beds.</p>
 */
@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Room {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    /**
     * The floor this room is located on.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    @ToString.Exclude
    private Floor floor;

    /**
     * Room number as identified within the hostel (e.g. "101").
     *
     * <p>Not globally unique — the same room number may exist on
     * different floors or hostels.</p>
     */
    @Column(name = "room_no", nullable = false)
    private String roomNo;

    /**
     * Sharing type / typology of this room.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "room_typology", nullable = false)
    private RoomTypology roomTypology;

    /**
     * Beds located in this room.
     *
     * <p>Inverse side of the relationship; {@link Bed} owns the
     * foreign key. No cascade removal — beds are not deleted
     * automatically when a room is removed.</p>
     */
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Bed> beds = new ArrayList<>();
}
