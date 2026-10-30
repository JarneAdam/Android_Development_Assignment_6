package be.vives.jarne.assignment_6.models

import java.util.Date

enum class Status {
    NEW, ASSIGNED, FINISHED
}

data class ToDo(
    var number: Int,
    var title: String,
    var description: String,
    var createdByUser: User,
    var createOnDate: Date,
    var assignedToUser: User?,
    var finishedOnDate: Date?,
    var timeEstimated: Int,
    var analysisDone: Boolean = false,
    var developmentDone: Boolean = false,
    var reviewAndTestingDone: Boolean = false,
    var acceptanceDone: Boolean = false,
) {
    val status: Status
        get() {
            return if ((assignedToUser != null) && (finishedOnDate != null)) {
                Status.FINISHED
            } else if (assignedToUser != null) {
                Status.ASSIGNED
            } else {
                Status.NEW
            }
        }

    val statusDescription: String
        get() = when (status) {
            Status.NEW -> "New"
            Status.ASSIGNED -> "Assigned"
            Status.FINISHED -> "Finished"
        }

    val timeRemaining: Double
        get() {
            return when {
                acceptanceDone -> 0.0
                reviewAndTestingDone -> timeEstimated * 0.10
                developmentDone -> timeEstimated * 0.30
                analysisDone -> timeEstimated * 0.85
                else -> timeEstimated.toDouble()
            }
        }
}
