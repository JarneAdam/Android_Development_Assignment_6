package be.vives.jarne.assignment_6.ui

import androidx.lifecycle.ViewModel
import be.vives.jarne.assignment_6.models.MockupToDo
import be.vives.jarne.assignment_6.models.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ToDoListUiState(
    val toDos: List<ToDo> = emptyList(),
)

class ToDoListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ToDoListUiState(toDos = MockupToDo.getToDos()))
    val uiState: StateFlow<ToDoListUiState> = _uiState.asStateFlow()
}
