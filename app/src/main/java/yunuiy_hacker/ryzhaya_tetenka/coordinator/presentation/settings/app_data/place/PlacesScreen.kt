package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.place

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.CreateUpdatePlaceDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PlaceRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getMapUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    navHostController: NavHostController, viewModel: PlacesViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(PlacesEvent.LoadDataEvent)
    }

    viewModel.state.let { state ->
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = -24.dp),
                        text = stringResource(R.string.places),
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
                            viewModel.onEvent(PlacesEvent.ShowPlaceCreateUpdateDialogEvent)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddLocationAlt,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .offset(x = -16.dp),
                            text = stringResource(R.string.add_new_place),
                            fontFamily = caros,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(state.places, key = { place -> place.id }) { place ->
                    key(place.id) {
                        PlaceRow(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                            place = place,
                            onClick = {
                                viewModel.onEvent(
                                    PlacesEvent.PreExecutionOperationWithPlaceEvent(
                                        place,
                                        context.getString(R.string.open_in_maps),
                                        context.getString(R.string.really_want_open_in_maps),
                                        PlaceOperationsEnum.OPEN_IN_MAP
                                    )
                                )
                            },
                            onLongClick = {
                                viewModel.onEvent(
                                    PlacesEvent.PreExecutionOperationWithPlaceEvent(
                                        place,
                                        context.getString(R.string.editing),
                                        context.getString(R.string.really_want_to_edit_place),
                                        PlaceOperationsEnum.EDIT
                                    )
                                )
                            },
                            showDeletionButton = true,
                            onDeleteClick = {
                                viewModel.onEvent(
                                    PlacesEvent.PreExecutionOperationWithPlaceEvent(
                                        place,
                                        context.getString(R.string.deletion),
                                        context.getString(R.string.really_want_to_delete_place),
                                        PlaceOperationsEnum.DELETE
                                    )
                                )
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        if (state.showCreateUpdatePlaceDialog) {
            CreateUpdatePlaceDialog(place = state.selectedPlace.value, onDismissRequest = {
                viewModel.onEvent(PlacesEvent.HidePlaceCreateUpdateDialogEvent)
            }, onAddClick = {
                viewModel.onEvent(PlacesEvent.CreateOrUpdatePlaceEvent(it))
            }, isEditMode = state.selectedOperation == PlaceOperationsEnum.EDIT)
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = state.questionTitle,
                text = state.questionText,
                onDismissRequest = {
                    viewModel.onEvent(PlacesEvent.HideQuestionDialog)
                },
                onConfirmRequest = {
                    when (state.selectedOperation) {
                        PlaceOperationsEnum.DELETE -> viewModel.onEvent(PlacesEvent.DeletePlaceEvent)
                        PlaceOperationsEnum.EDIT -> viewModel.onEvent(PlacesEvent.ShowPlaceCreateUpdateDialogEvent)
                        PlaceOperationsEnum.OPEN_IN_MAP -> {
                            val mapIntent =
                                Intent(
                                    Intent.ACTION_VIEW,
                                    getMapUri(
                                        state.selectedPlace.value!!.la,
                                        state.selectedPlace.value!!.lt
                                    )
                                )
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
                            viewModel.onEvent(PlacesEvent.OpenInMapPlaceEvent)
                        }
                    }
                })
        }
    }
}
