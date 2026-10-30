package be.vives.jarne.assignment_6.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.vives.jarne.assignment_6.R
import be.vives.jarne.assignment_6.models.ToDo
import be.vives.jarne.assignment_6.utility.Utility

@Composable
fun ToDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ToDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    uiState.toDo?.let { currentToDo ->
        ToDetailContent(
            toDo = currentToDo,
            modifier = modifier,
            onNavigateBack = onNavigateBack,
            onNavigateToEdit = onNavigateToEdit,
        )
    }
}

@Composable
fun ToDetailContent(
    toDo: ToDo,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        IconButton(onClick = onNavigateBack) {
            Text(text = "←", fontSize = 24.sp)
        }

        // Top section with image, number, and status
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.todo_image),
                contentDescription = "ToDo Image",
                modifier = Modifier.size(120.dp),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "#${toDo.number}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 56.sp,
                )
                Text(
                    text = toDo.statusDescription,
                    fontFamily = FontFamily.Cursive,
                    fontSize = 36.sp,
                )
            }
        }

        // Red divider
        HorizontalDivider(
            color = Color.Red,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Title and Description
        Text(
            text = toDo.title,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Text(
            text = toDo.description,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        val assignedUserText = if (toDo.assignedToUser != null) {
            "Assigned to ${Utility.getUserDisplayName(toDo.assignedToUser)}"
        } else {
            Utility.getUserDisplayName(null, "Not assigned yet")
        }
        Text(
            text = assignedUserText,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Time estimated and remaining
        Text(
            text = "Time estimated ${toDo.timeEstimated} hours",
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        val remainingFormatted = Utility.formatHours(toDo.timeRemaining)
        Text(
            text = "Time remaining $remainingFormatted hours",
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        // Boxed area for boolean flags
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SwitchRow(label = "Analysis done?", checked = toDo.analysisDone)
                SwitchRow(label = "Development done?", checked = toDo.developmentDone)
                SwitchRow(label = "Review & testing done?", checked = toDo.reviewAndTestingDone)
                SwitchRow(label = "Acceptance done?", checked = toDo.acceptanceDone)
            }
        }

        Button(
            onClick = onNavigateToEdit,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp),
        ) {
            Text("Bewerken")
        }
    }
}
