package com.hostel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
 * Represents a hostel property.
 *
 * <p>Currently scoped to a single hostel (Juhu), with the schema designed
 * to support multiple hostels in the future without structural changes.</p>
 *
 * <p>A {@code Hostel} is the top of the location hierarchy:
 * {@code Hostel -> Floor -> Room -> Bed}.</p>
 *
 * <p>This is a master table. No cascade removal is applied to its
 * {@link Floor} collection — deleting a hostel must never implicitly
 * delete its floors.</p>
 */
@Entity
@Table(name = "hostel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Hostel {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hostel_id")
    private Long hostelId;

    /**
     * Display name of the hostel (e.g. "Juhu").
     *
     * <p>Must be unique across all hostels.</p>
     */
    @Column(name = "hostel_name", nullable = false, unique = true)
    private String hostelName;

    /**
     * Short unique code identifying the hostel, used as a prefix in
     * booking numbers (e.g. "JHU" in {@code JHU-2025-0001}).
     *
     * <p>Must be unique across all hostels.</p>
     */
    @Column(name = "hostel_code", nullable = false, unique = true)
    private String hostelCode;

    /**
     * Floors belonging to this hostel.
     *
     * <p>Inverse side of the relationship; {@link Floor} owns the
     * foreign key. No cascade removal — floors are not deleted
     * automatically when a hostel is removed.</p>
     */
    @OneToMany(mappedBy = "hostel", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Floor> floors = new ArrayList<>();
}
