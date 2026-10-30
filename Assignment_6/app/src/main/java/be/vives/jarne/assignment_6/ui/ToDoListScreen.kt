package be.vives.jarne.assignment_6.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.vives.jarne.assignment_6.models.MockupToDo
import be.vives.jarne.assignment_6.models.ToDo
import be.vives.jarne.assignment_6.ui.theme.Assignment_6Theme

@Composable
fun ToDoListScreen(
    modifier: Modifier = Modifier,
    viewModel: ToDoListViewModel = viewModel(),
    onToDoClick: (ToDo) -> Unit = {},
    onCreateClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onNavigateBack) {
                    Text(text = "←", fontSize = 24.sp)
                }
                IconButton(onClick = onCreateClick) {
                    Text(text = "+", fontSize = 28.sp)
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                ToDoListContent(
                    modifier = Modifier.fillMaxSize(),
                    toDos = uiState.toDos,
                    onToDoClick = onToDoClick,
                )
            }
        }
    }
}

@Composable
fun ToDoListContent(
    modifier: Modifier = Modifier,
    toDos: List<ToDo> = MockupToDo.getToDos(),
    onToDoClick: (ToDo) -> Unit = { _ -> },
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = toDos,
            key = { it.number },
        ) { toDo ->
            ToDoItemCard(
                toDo = toDo,
                onClick = { onToDoClick(toDo) },
            )
        }
    }
}

@Composable
fun ToDoItemCard(
    toDo: ToDo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "#${toDo.number}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = toDo.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = toDo.statusDescription,
                fontFamily = FontFamily.Cursive,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoListScreenPreview() {
    Assignment_6Theme {
        ToDoListContent()
    }
}
