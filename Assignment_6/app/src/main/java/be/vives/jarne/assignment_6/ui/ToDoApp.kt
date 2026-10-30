package be.vives.jarne.assignment_6.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

private data object ToDoListRoute
private data class ToDoDetailRoute(val id: Int)
private data class AddEditToDoRoute(val id: Int? = null)

@Composable
fun ToDoApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val backStack = remember { mutableStateListOf<Any>(ToDoListRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<ToDoListRoute> {
                ToDoListScreen(
                    modifier = modifier,
                    onToDoClick = { toDo ->
                        backStack.add(ToDoDetailRoute(toDo.number))
                    },
                    onCreateClick = {
                        backStack.add(AddEditToDoRoute())
                    },
                    onNavigateBack = { (context as? Activity)?.finish() },
                )
            }

            entry<ToDoDetailRoute> { route ->
                val detailViewModel: ToDetailViewModel = viewModel(
                    factory = ToDetailViewModel.Factory(route.id),
                )
                ToDetailScreen(
                    modifier = modifier,
                    viewModel = detailViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToEdit = {
                        backStack.add(AddEditToDoRoute(route.id))
                    },
                )
            }

            entry<AddEditToDoRoute> { route ->
                val addEditViewModel: AddEditToDoViewModel = viewModel(
                    factory = AddEditToDoViewModel.Factory(route.id),
                )
                AddEditToDoScreen(
                    modifier = modifier,
                    viewModel = addEditViewModel,
                    onNavigateBack = { backStack.removeLastOrNull() },
                )
            }
        },
    )
}
