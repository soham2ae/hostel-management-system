package com.hostel.entity;

import com.hostel.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an application user with access to the Hostel ERP system.
 *
 * <p>This is a standalone entity with no foreign key relationships to
 * any other table.</p>
 *
 * <p>{@link UserRole#SDA} (Senior Data Analyst) has full access,
 * including user management, dashboard, reports, and all setup
 * screens. {@link UserRole#ADMIN} has operational access only —
 * booking, search, payments, and installments — and is excluded from
 * user management, dashboard configuration, setup screens, and
 * reports.</p>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * Login username.
     *
     * <p>Must be unique across all users.</p>
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Hashed password.
     *
     * <p>Excluded from {@code toString()} so credential material is
     * never accidentally logged.</p>
     */
    @Column(name = "_password", nullable = false)
    @ToString.Exclude
    private String password;

    /**
     * Application access role assigned to this user.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}
