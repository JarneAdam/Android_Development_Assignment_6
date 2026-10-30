package be.vives.jarne.assignment_6.utility

import be.vives.jarne.assignment_6.models.User
import java.util.Locale

object Utility {
    /**
     * Formats number of hours to 2 decimal places.
     */
    fun formatHours(hours: Number): String {
        return String.format(Locale.US, "%.2f", hours.toDouble())
    }

    /**
     * Formats a User's full name or returns a default fallback text if no user is assigned.
     */
    fun getUserDisplayName(user: User?, fallbackText: String = "Select User (Optional)"): String {
        return user?.let { "${it.firstName} ${it.lastName}" } ?: fallbackText
    }
}
