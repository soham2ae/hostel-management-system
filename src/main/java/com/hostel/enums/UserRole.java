package com.hostel.enums;

/**
 * Represents the application access role assigned to a user.
 *
 * <p>{@code SDA} (Senior Data Analyst) has full access, including user
 * management, dashboard, reports, and all setup screens. {@code ADMIN}
 * has operational access only — booking, search, payments, and
 * installments — and is explicitly excluded from user management,
 * dashboard configuration, setup screens, and reports.</p>
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code users} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum UserRole {

    /** Senior Data Analyst — full access. */
    SDA,

    /** Admin — operational access only. */
    ADMIN;

    /**
     * Returns a human-readable display name for this user role.
     *
     * @return the display name
     */
    public String getDisplayLabel() {
        switch (this) {
            case SDA:
                return "Senior Data Analyst";
            case ADMIN:
                return "Admin";
            default:
                throw new IllegalStateException("Unhandled UserRole: " + this);
        }
    }
}
