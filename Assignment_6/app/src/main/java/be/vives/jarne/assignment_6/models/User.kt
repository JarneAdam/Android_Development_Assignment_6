package be.vives.jarne.assignment_6.models

data class User(
    var id: Int,
    var userName: String,
    var firstName: String,
    var lastName: String,
    var password: String,
    var isActive: Boolean
) {
    // Empty constructor
    constructor() : this(0, "", "", "", "", false)
}
