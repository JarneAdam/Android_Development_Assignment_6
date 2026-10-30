package be.vives.jarne.assignment_6.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import be.vives.jarne.assignment_6.data.BoredRepository
import be.vives.jarne.assignment_6.data.NetworkBoredRepository
import be.vives.jarne.assignment_6.data.toDescription
import be.vives.jarne.assignment_6.models.MockupToDo
import be.vives.jarne.assignment_6.models.ToDo
import be.vives.jarne.assignment_6.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val isNewToDo: Boolean
        get() = initialToDo == null
}

class AddEditToDoViewModel(
    toDoId: Int?,
    private val boredRepository: BoredRepository = NetworkBoredRepository(),
) : ViewModel() {
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

    class Factory(
        private val toDoId: Int?,
        private val boredRepository: BoredRepository = NetworkBoredRepository(),
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(AddEditToDoViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return AddEditToDoViewModel(toDoId, boredRepository) as T
        }
    }

    fun onSurpriseMe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            boredRepository.getRandomActivity()
                .onSuccess { activityDto ->
                    _uiState.update {
                        it.copy(
                            title = activityDto.activity,
                            description = activityDto.toDescription(),
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage ?: "Failed to fetch surprise activity",
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
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
