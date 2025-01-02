package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.create_task

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimePickerDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.TimeRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    navHostController: NavHostController,
    viewModel: CreateTaskViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateTaskEvent.InitEvent)
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
                            text = stringResource(
                                R.string.create_task,
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
                    })
            }) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(bottom = 64.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.type_of_time),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = caros
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                viewModel.onEvent(CreateTaskEvent.ShowTimeTypePickerMenuEvent)
                            }
                            .animateContentSize(),
                            text = stringResource(Constants.timeTypes.find { it.id == state.timeType.id }!!.resId).lowercase(),
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium
                        )
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
                                    ), expanded = state.showTimeTypePickerMenu, onDismissRequest = {
                                    viewModel.onEvent(CreateTaskEvent.HideTimeTypePickerMenuEvent)
                                }, offset = DpOffset(x = 90.dp, y = 0.dp)
                            ) {
                                Constants.timeTypes.forEach { timeType ->
                                    DropdownMenuItem(modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(
                                            width = 0.dp,
                                            color = Color.Unspecified,
                                            shape = RoundedCornerShape(10.dp)
                                        ), text = {
                                        Text(
                                            text = stringResource(timeType.resId).lowercase(),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = caros,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }, onClick = {
                                        viewModel.onEvent(
                                            CreateTaskEvent.SelectTimeTypePickerMenuEvent(
                                                timeType
                                            )
                                        )
                                    })
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TimeRow(modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                        timeType = state.timeType,
                        date = state.date,
                        hour = state.selectedHour,
                        weekDate = state.weekDate,
                        minute = state.selectedMinute,
                        onDateClick = {
                            viewModel.onEvent(CreateTaskEvent.ShowDatePickerDialogEvent)
                        },
                        onTimeClick = {
                            viewModel.onEvent(CreateTaskEvent.ShowTimePickerDialogEvent)
                        })
                    TextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)
                        .offset(x = -16.dp)
                        .animateContentSize(),
                        value = state.heading,
                        onValueChange = {
                            viewModel.onEvent(CreateTaskEvent.HeadingChangeEvent(it))
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = MaterialTheme.colorScheme.background
                        ),
                        textStyle = TextStyle(
                            fontFamily = caros, fontWeight = FontWeight.Bold, fontSize = 20.sp
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.heading),
                                fontFamily = caros,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            )
                        })
                    TextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)
                        .offset(x = -16.dp, y = -16.dp)
                        .animateContentSize(),
                        value = state.content,
                        onValueChange = {
                            viewModel.onEvent(CreateTaskEvent.ContentChangeEvent(it))
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = MaterialTheme.colorScheme.background
                        ),
                        textStyle = TextStyle(
                            fontFamily = caros,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Justify
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.content),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        })
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(
                                interactionSource = interactionSource, indication = null
                            ) {
                                viewModel.onEvent(CreateTaskEvent.OnClickButtonEvent)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.create),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        LaunchedEffect(state.success) {
            if (state.success) {
                navHostController.popBackStack()
            }
        }

        if (state.showDatePickerDialog) {
            yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.DatePickerDialog(
                modifier = Modifier.heightIn(max = 700.dp), onDismissRequest = {
                    viewModel.onEvent(CreateTaskEvent.HideDatePickerDialogEvent)
                },
                confirmButton = {},
                colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    DatePicker(state = datePickerState)
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp), onClick = {
                            viewModel.onEvent(
                                CreateTaskEvent.SelectDatePickerDialogEvent(
                                    datePickerState.selectedDateMillis ?: 0
                                )
                            )
                        }, shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.select),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        if (state.showTimePickerDialog) {
            TimePickerDialog(onDismissRequest = {
                viewModel.onEvent(CreateTaskEvent.HideTimePickerDialogEvent)
            }, onSelectButtonClick = { hour, minute ->
                viewModel.onEvent(
                    CreateTaskEvent.SelectTimePickerDialogEvent(
                        hour = hour, minute = minute
                    )
                )
            })
        }
    }
}