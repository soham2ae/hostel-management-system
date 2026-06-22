package com.hostel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Represents a floor belonging to a {@link Hostel}.
 *
 * <p>Sits between {@link Hostel} and {@link Room} in the location
 * hierarchy: {@code Hostel -> Floor -> Room -> Bed}.</p>
 *
 * <p>This is a master table. No cascade removal is applied to its
 * {@link Room} collection — deleting a floor must never implicitly
 * delete its rooms.</p>
 */
@Entity
@Table(name = "floor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Floor {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id")
    private Long floorId;

    /**
     * The hostel this floor belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    @ToString.Exclude
    private Hostel hostel;

    /**
     * Floor number (e.g. 0 for Ground Floor, 1 for First Floor).
     */
    @Column(name = "floor_no", nullable = false)
    private Integer floorNo;

    /**
     * Total number of rooms configured on this floor.
     */
    @Column(name = "total_rooms")
    private Integer totalRooms;

    /**
     * Rooms located on this floor.
     *
     * <p>Inverse side of the relationship; {@link Room} owns the
     * foreign key. No cascade removal — rooms are not deleted
     * automatically when a floor is removed.</p>
     */
    @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
}
