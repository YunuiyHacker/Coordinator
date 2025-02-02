package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.task

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdatePeopleColumn
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.LoadingIndicator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MenuItem
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PeopleRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdateSubtaskRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.SubtaskRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimeRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.roboto
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.displayName
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getMapUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navHostController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedPeoplesListExpanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.onEvent(TaskEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(
                                R.string.task
                            ),
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
                    },
                    actions = {
                        Row {
                            Box(modifier = Modifier
                                .size(24.dp)
                                .clickable(
                                    interactionSource = interactionSource, indication = null
                                ) {
                                    viewModel.onEvent(TaskEvent.ShowTaskMenuEvent)
                                }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(24.dp))
                        }

                        MaterialTheme(
                            colorScheme = MaterialTheme.colorScheme.copy(surface = MaterialTheme.colorScheme.surfaceVariant),
                            shapes = MaterialTheme.shapes.copy(extraSmall = ShapeDefaults.Medium)
                        ) {
                            DropdownMenu(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 0.3.dp,
                                        color = Color.DarkGray,
                                        shape = RoundedCornerShape(12.dp)
                                    ), expanded = state.showTaskMenu, onDismissRequest = {
                                    viewModel.onEvent(TaskEvent.HideTaskMenuEvent)
                                }, offset = DpOffset(x = -12.dp, y = 0.dp)
                            ) {
                                DropdownMenuItem(text = {
                                    MenuItem(
                                        icon = Icons.Rounded.Edit, title = R.string.edit
                                    )
                                }, onClick = {
                                    viewModel.onEvent(TaskEvent.HideTaskMenuEvent)
                                    navHostController.navigate("${Route.CreateUpdateTaskScreen.route}/${state.taskId}")
                                })
                                DropdownMenuItem(text = {
                                    MenuItem(
                                        icon = Icons.Rounded.Delete,
                                        title = R.string.delete,
                                        iconColor = MaterialTheme.colorScheme.primary,
                                        titleColor = MaterialTheme.colorScheme.primary
                                    )
                                }, onClick = {
                                    viewModel.onEvent(TaskEvent.HideTaskMenuEvent)
                                    viewModel.onEvent(
                                        TaskEvent.ShowQuestionDialogEvent(
                                            context.getString(
                                                R.string.deletion
                                            ),
                                            context.getString(R.string.really_want_to_delete_task)
                                        )
                                    )
                                })
                            }
                        }
                    })
            }, bottomBar = {

            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {
                    TimeRow(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        timeType = Constants.timeTypes.find { timeType ->
                            state.task.timeTypeId == timeType.id
                        }!!,
                        date = state.task.date,
                        hour = state.task.hour,
                        weekDate = state.weekDate,
                        minute = state.task.minute,
                        onDateClick = {

                        },
                        onTimeClick = {

                        },
                        onEndTimeClick = {},
                        withEndTime = state.task.withEndTime,
                        endHour = state.task.endHour,
                        endMinute = state.task.endMinute
                    )
                    if (!state.contentState.isLoading.value) {
                        if (state.place.id != 0) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            viewModel.onEvent(TaskEvent.OnOpenPlaceInMapModeEvent)
                                            viewModel.onEvent(
                                                TaskEvent.ShowQuestionDialogEvent(
                                                    context.getString(
                                                        R.string.open_in_maps
                                                    ),
                                                    context.getString(R.string.really_want_open_in_maps)
                                                )
                                            )
                                        }, verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            imageVector = Icons.Default.Place,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp)
                                        ) {
                                            Text(
                                                text = "${state.place.title} | ${state.place.la}, ${state.place.lt}",
                                                fontFamily = caros,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (state.peoples.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxWidth()
                                        .animateContentSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateContentSize()
                                            .padding(vertical = 6.dp, horizontal = 6.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box {
                                            state.peoples.forEachIndexed { index, people ->
                                                if (index <= 4) {
                                                    key(people.id) {
                                                        Row(modifier = Modifier.padding(start = index * 30.dp)) {
                                                            if (people.avatarPath.isNotEmpty()) {
                                                                AsyncImage(
                                                                    modifier = Modifier
                                                                        .size(60.dp)
                                                                        .clip(CircleShape),
                                                                    model = ImageRequest.Builder(
                                                                        context
                                                                    )
                                                                        .data("${ImageUtils.IMG_DIR}/${people.avatarPath}")
                                                                        .build(),
                                                                    contentDescription = null,
                                                                    contentScale = ContentScale.Crop
                                                                )
                                                            } else {
                                                                Icon(
                                                                    modifier = Modifier
                                                                        .size(60.dp)
                                                                        .clip(CircleShape)
                                                                        .background(
                                                                            color = MaterialTheme.colorScheme.primary,
                                                                            shape = CircleShape
                                                                        )
                                                                        .padding(12.dp),
                                                                    imageVector = Icons.Rounded.Person,
                                                                    contentDescription = null
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            modifier = Modifier.weight(1f),
                                            text = if (state.peoples.size > 0) displayName(
                                                state.peoples[0].displayName,
                                                state.peoples[0].surname,
                                                state.peoples[0].name,
                                                state.peoples[0].lastname
                                            ) + if (state.peoples.size == 1) "" else ", ..." else "",
                                            fontFamily = caros,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .clickable {
                                                    selectedPeoplesListExpanded =
                                                        !selectedPeoplesListExpanded
                                                },
                                            imageVector = if (selectedPeoplesListExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                AnimatedVisibility(selectedPeoplesListExpanded) {
                                    Column {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        state.peoples.forEach { people ->
                                            PeopleRow(people = people,
                                                onClick = {
                                                    viewModel.onEvent(
                                                        TaskEvent.ShowCreateUpdatePeopleBottomSheetEvent(
                                                            people
                                                        )
                                                    )
                                                },
                                                onLongClick = {},
                                                showDeletionButton = false,
                                                onDeleteClick = {})
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (state.task.title.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .animateContentSize(),
                            text = state.task.title,
                            fontFamily = caros,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            lineHeight = 28.sp
                        )
                    }
                    if (state.task.content.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .animateContentSize(),
                            text = state.task.content,
                            fontFamily = caros,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .animateContentSize()
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        if (!state.contentState.isLoading.value) {
                            state.subtasks.forEach { subtask ->
                                key(subtask.id) {
                                    SubtaskRow(
                                        subtask = subtask,
                                        onCheckedChange = {
                                            viewModel.onEvent(
                                                TaskEvent.SubtaskItemCheckboxToggleEvent(
                                                    subtask
                                                )
                                            )
                                        },
                                        onDeleteClick = {

                                        },
                                        isEditingEnabled = false
                                    )
                                }
                            }
                        } else {
                            LoadingIndicator()
                        }
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {

            }
        }

        if (state.showCreateUpdatePeopleBottomSheet) {
            AnimatedVisibility(state.showCreateUpdatePeopleBottomSheet) {
                ModalBottomSheet(sheetState = sheetState, onDismissRequest = {
                    viewModel.onEvent(TaskEvent.HideCreateUpdatePeopleBottomSheetEvent)
                }, containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    CreateUpdatePeopleColumn(
                        onDismissRequest = {
                        },
                        onSaveClick = { people, uri ->
                        },
                        people = state.selectedPeople,
                        isEditMode = false,
                        isReadOnly = true,
                        onDeleteClick = {
                        }, showButtons = false,
                        onClickPhoto = {}
                    )
                }
            }
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = state.questionTitle,
                text = state.questionText,
                onDismissRequest = {
                    viewModel.onEvent(TaskEvent.HideQuestionDialogEvent)
                },
                onConfirmRequest = {
                    if (state.openPlaceInMapMode) {
                        viewModel.onEvent(TaskEvent.OpenPlaceInMapEvent)
                        val mapIntent =
                            Intent(Intent.ACTION_VIEW, getMapUri(state.place.la, state.place.lt))
                        try {
                            mapIntent.setPackage(Constants.YANDEX_MAPS_PACKAGE)
                            startActivity(context, mapIntent, null)
                        } catch (e: ActivityNotFoundException) {
                            try {
                                mapIntent.setPackage(Constants.GOOGLE_MAPS_PACKAGE)
                                startActivity(context, mapIntent, null)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.not_supported_apps),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else viewModel.onEvent(TaskEvent.DeleteTaskEvent)
                })
        }

        LaunchedEffect(state.success) {
            if (state.success) {
                navHostController.popBackStack()
            }
        }
    }
}