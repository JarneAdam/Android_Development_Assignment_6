package be.vives.jarne.assignment_6.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.jarne.assignment_6.models.MockupToDo
import be.vives.jarne.assignment_6.models.ToDo
import be.vives.jarne.assignment_6.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddEditToDoUiState(
    val title: String = "",
    val description: String = "",
    val selectedUser: User? = null,
    val users: List<User> = emptyList(),
    val analysisDone: Boolean = false,
    val developmentDone: Boolean = false,
    val reviewAndTestingDone: Boolean = false,
    val acceptanceDone: Boolean = false,
    val initialToDo: ToDo? = null,
)

class AddEditToDoViewModel(toDoId: Int?) : ViewModel() {
    private val initialToDo = toDoId?.let { id ->
        checkNotNull(MockupToDo.getToDos().firstOrNull { it.number == id }) {
            "No ToDo found with id $id"
        }
    }

    private val _uiState = MutableStateFlow(
        AddEditToDoUiState(
            title = initialToDo?.title.orEmpty(),
            description = initialToDo?.description.orEmpty(),
            selectedUser = initialToDo?.assignedToUser,
            users = MockupToDo.getUsers(),
            analysisDone = initialToDo?.analysisDone ?: false,
            developmentDone = initialToDo?.developmentDone ?: false,
            reviewAndTestingDone = initialToDo?.reviewAndTestingDone ?: false,
            acceptanceDone = initialToDo?.acceptanceDone ?: false,
            initialToDo = initialToDo,
        )
    )
    val uiState: StateFlow<AddEditToDoUiState> = _uiState.asStateFlow()

    class Factory(private val toDoId: Int?) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(AddEditToDoViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return AddEditToDoViewModel(toDoId) as T
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onUserSelected(user: User?) {
        _uiState.update { it.copy(selectedUser = user) }
    }

    fun onAnalysisDoneChange(done: Boolean) {
        _uiState.update { it.copy(analysisDone = done) }
    }

    fun onDevelopmentDoneChange(done: Boolean) {
        _uiState.update { it.copy(developmentDone = done) }
    }

    fun onReviewAndTestingDoneChange(done: Boolean) {
        _uiState.update { it.copy(reviewAndTestingDone = done) }
    }

    fun onAcceptanceDoneChange(done: Boolean) {
        _uiState.update { it.copy(acceptanceDone = done) }
    }

    fun getUpdatedToDo(): ToDo {
        val state = _uiState.value
        val baseToDo = state.initialToDo ?: MockupToDo.getToDos().first()
        return baseToDo.copy(
            title = state.title,
            description = state.description,
            assignedToUser = state.selectedUser,
            analysisDone = state.analysisDone,
            developmentDone = state.developmentDone,
            reviewAndTestingDone = state.reviewAndTestingDone,
            acceptanceDone = state.acceptanceDone,
        )
    }
}
