package be.vives.jarne.assignment_6.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for the Bored API response from https://bored-api.appbrewery.com/random.
 *
 * NOTE: Unlike the legacy boredapi.com where accessibility was a Double/Float,
 * in appbrewery's Bored API, "accessibility" is a descriptive String (e.g. "Few to no challenges")
 * and "price" is a Double.
 */
@Serializable
data class BoredActivityDto(
    @SerialName("activity")
    val activity: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("participants")
    val participants: Int = 1,
    @SerialName("price")
    val price: Double = 0.0,
    @SerialName("accessibility")
    val accessibility: String = "",
    @SerialName("availability")
    val availability: Double? = null,
    @SerialName("duration")
    val duration: String? = null,
    @SerialName("kidFriendly")
    val kidFriendly: Boolean? = null,
    @SerialName("link")
    val link: String? = null,
    @SerialName("key")
    val key: String? = null,
)

/**
 * Formats the BoredActivity fields type, participants, price, and accessibility
 * into a structured multi-line description for the ToDo item.
 */
fun BoredActivityDto.toDescription(): String {
    return """
        Type: $type
        Participants: $participants
        Price: $price
        Accessibility: $accessibility
    """.trimIndent()
}
