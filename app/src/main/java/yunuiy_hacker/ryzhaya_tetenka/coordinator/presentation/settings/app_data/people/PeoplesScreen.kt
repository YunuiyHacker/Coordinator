package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.people

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdatePeopleColumn
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PeopleRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeoplesScreen(
    navHostController: NavHostController, viewModel: PeoplesViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.onEvent(PeoplesEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = -24.dp),
                            text = stringResource(R.string.peoples),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        Row {
                            Spacer(modifier = Modifier.width(24.dp))
                            Box(modifier = Modifier.clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                navHostController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = null
                                )
                            }
                        }
                    })
            }) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .animateContentSize()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            onClick = {
                                viewModel.onEvent(
                                    PeoplesEvent.ShowCreateUpdatePeopleBottomSheetEvent(
                                        null, false
                                    )
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAddAlt1,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .offset(x = -16.dp),
                                text = stringResource(R.string.add_new_people),
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    items(state.peoples, key = { people -> people.id }) { people ->
                        key(people.id) {
                            PeopleRow(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                                people = people,
                                onClick = {
                                    state.selectedPeople.value = people
                                    viewModel.onEvent(
                                        PeoplesEvent.ShowCreateUpdatePeopleBottomSheetEvent(
                                            people, true
                                        )
                                    )
                                },
                                onLongClick = {
                                    state.isEditMode = true
                                    viewModel.onEvent(
                                        PeoplesEvent.ShowCreateUpdatePeopleBottomSheetEvent(
                                            people, false
                                        )
                                    )
                                },
                                showDeletionButton = true,
                                onDeleteClick = {
                                    state.selectedPeople.value = it
                                    viewModel.onEvent(
                                        PeoplesEvent.ShowQuestionDialogEvent(
                                            context.getString(
                                                R.string.deletion
                                            ),
                                            context.getString(R.string.really_want_to_delete_people)
                                        )
                                    )
                                })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            if (state.showPhoto) {
                Dialog(onDismissRequest = {
                    viewModel.onEvent(PeoplesEvent.HideImageEvent)
                }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
                    ShowPeopleImageScreen(
                        imageName = state.selectedPeople.value?.avatarPath.toString(),
                        displayName = state.selectedPeople.value.let { p ->
                            displayName(
                                p?.displayName ?: "",
                                p?.surname ?: "",
                                p?.name ?: "",
                                p?.lastname ?: ""
                            )
                        },
                        onDismissRequest = {
                            viewModel.onEvent(PeoplesEvent.HideImageEvent)
                        })
                }
            }

            if (state.showCreateUpdatePeopleBottomSheet) {
                AnimatedVisibility(state.showCreateUpdatePeopleBottomSheet) {
                    ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
                        viewModel.onEvent(PeoplesEvent.HideCreateUpdatePeopleBottomSheetEvent)
                    }, containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                        CreateUpdatePeopleColumn(onDismissRequest = {
                            state.canCloseCreateUpdatePeopleButtonSheet = true
                        },
                            onSaveClick = { people, uri ->
                                viewModel.onEvent(
                                    PeoplesEvent.CreateUpdatePeopleEvent(
                                        people, uri
                                    )
                                )
                            },
                            people = state.selectedPeople.value,
                            isEditMode = state.isEditMode,
                            isReadOnly = state.isOnlyReadPeople,
                            onDeleteClick = {
                                viewModel.onEvent(
                                    PeoplesEvent.ShowQuestionDialogEvent(
                                        context.getString(R.string.deletion),
                                        context.getString(R.string.really_want_to_delete_people)
                                    )
                                )
                            },
                            onClickPhoto = {
                                viewModel.onEvent(PeoplesEvent.ShowImageEvent)
                            })
                    }
                }
            }

            LaunchedEffect(state.canCloseCreateUpdatePeopleButtonSheet) {
                if (state.canCloseCreateUpdatePeopleButtonSheet) scope.launch {
                    sheetState.hide()
                    state.canCloseCreateUpdatePeopleButtonSheet = false
                    state.showCreateUpdatePeopleBottomSheet = false
                }
            }
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = state.questionTitle,
                text = state.questionText,
                onDismissRequest = {
                    viewModel.onEvent(PeoplesEvent.HideQuestionDialogEvent)
                    state.canCloseCreateUpdatePeopleButtonSheet = true
                },
                onConfirmRequest = {
                    viewModel.onEvent(PeoplesEvent.DeletePeopleEvent)
                })
        }
    }
}