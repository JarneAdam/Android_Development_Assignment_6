package be.vives.jarne.assignment_6.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.vives.jarne.assignment_6.models.MockupToDo
import be.vives.jarne.assignment_6.models.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ToDetailUiState(
    val toDo: ToDo? = null,
)

class ToDetailViewModel(toDoId: Int) : ViewModel() {
    private val selectedToDo = checkNotNull(
        MockupToDo.getToDos().firstOrNull { it.number == toDoId }
    ) { "No ToDo found with id $toDoId" }

    private val _uiState = MutableStateFlow(ToDetailUiState(toDo = selectedToDo))
    val uiState: StateFlow<ToDetailUiState> = _uiState.asStateFlow()

    class Factory(private val toDoId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(ToDetailViewModel::class.java)) {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return ToDetailViewModel(toDoId) as T
        }
    }
}
