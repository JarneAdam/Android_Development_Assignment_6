package be.vives.jarne.assignment_6.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.vives.jarne.assignment_6.models.ToDo
import be.vives.jarne.assignment_6.models.User

/**
 * Stateful screen component managing ToDo editing state using AddEditToDoViewModel.
 */
@Composable
fun AddEditToDoScreen(
    modifier: Modifier = Modifier,
    viewModel: AddEditToDoViewModel,
    onNavigateBack: () -> Unit = {},
    onSave: (ToDo) -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    AddEditToDoContent(
        title = uiState.title,
        onTitleChange = viewModel::onTitleChange,
        description = uiState.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        selectedUser = uiState.selectedUser,
        onUserSelected = viewModel::onUserSelected,
        users = uiState.users,
        analysisDone = uiState.analysisDone,
        onAnalysisDoneChange = viewModel::onAnalysisDoneChange,
        developmentDone = uiState.developmentDone,
        onDevelopmentDoneChange = viewModel::onDevelopmentDoneChange,
        reviewAndTestingDone = uiState.reviewAndTestingDone,
        onReviewAndTestingDoneChange = viewModel::onReviewAndTestingDoneChange,
        acceptanceDone = uiState.acceptanceDone,
        onAcceptanceDoneChange = viewModel::onAcceptanceDoneChange,
        onSave = {
            onSave(viewModel.getUpdatedToDo())
        },
        onCancel = onCancel,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

/**
 * Stateless content component displaying the form UI.
 */
@Composable
fun AddEditToDoContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedUser: User?,
    onUserSelected: (User?) -> Unit,
    users: List<User>,
    analysisDone: Boolean,
    onAnalysisDoneChange: (Boolean) -> Unit,
    developmentDone: Boolean,
    onDevelopmentDoneChange: (Boolean) -> Unit,
    reviewAndTestingDone: Boolean,
    onReviewAndTestingDoneChange: (Boolean) -> Unit,
    acceptanceDone: Boolean,
    onAcceptanceDoneChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        IconButton(onClick = onNavigateBack) {
            Text(text = "←", fontSize = 24.sp)
        }

        // Title text
        Text(
            text = "Add/Edit ToDo",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        // Title OutlinedTextField
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
        )

        // Description OutlinedTextField
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
        )

        // Assign To Dropdown
        UserDropdown(
            selectedUser = selectedUser,
            users = users,
            onUserSelected = onUserSelected,
        )

        // Progress header
        Text(
            text = "Progress:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp),
        )

        // Switches
        SwitchRow(
            label = "Analysis Done",
            checked = analysisDone,
            onCheckedChange = onAnalysisDoneChange,
        )
        SwitchRow(
            label = "Development Done",
            checked = developmentDone,
            onCheckedChange = onDevelopmentDoneChange,
        )
        SwitchRow(
            label = "Review & Testing Done",
            checked = reviewAndTestingDone,
            onCheckedChange = onReviewAndTestingDoneChange,
        )
        SwitchRow(
            label = "Acceptance Done",
            checked = acceptanceDone,
            onCheckedChange = onAcceptanceDoneChange,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save & Cancel Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.size(12.dp))
            Button(onClick = onSave) {
                Text("Save")
            }
        }
    }
}
