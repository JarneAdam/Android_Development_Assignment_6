package be.vives.jarne.assignment_6.models

import java.util.Date

object MockupToDo {
    fun getUsers(): List<User> {
        return listOf(
            User(1, "dhostens", "Dirk", "Hostens", "pass123", true),
            User(2, "jdoe", "John", "Doe", "pass123", true),
            User(3, "asmith", "Alice", "Smith", "pass123", true),
            User(4, "bwilliams", "Bob", "Williams", "pass123", false)
        )
    }

    fun getToDos(): List<ToDo> {
        val users = getUsers()
        return listOf(
            ToDo(
                1,
                "Finish detail ToDo",
                "Add extra fields like assigned user, time estimated, ...to the ToDo detail screen",
                users[1],
                Date(),
                users[0],
                Date(),
                20,
                false, false, false, false
            ),
            ToDo(
                2,
                "Implement Models",
                "Create User and ToDo data classes",
                users[0],
                Date(),
                users[1],
                null,
                2,
                true, false, false, false
            ),
            ToDo(
                3,
                "Create Mock Data",
                "Generate a list of users and todos for testing",
                users[1],
                Date(),
                users[1],
                null,
                1,
                true, true, false, false
            ),
            ToDo(
                4,
                "Design UI",
                "Create Jetpack Compose layout for ToDo details",
                users[2],
                Date(),
                users[0],
                null,
                8,
                true, true, true, false
            ),
            ToDo(
                5,
                "Testing and QA",
                "Ensure everything works as expected",
                users[0],
                Date(),
                users[2],
                Date(),
                5,
                true, true, true, true
            )
        )
    }
}
