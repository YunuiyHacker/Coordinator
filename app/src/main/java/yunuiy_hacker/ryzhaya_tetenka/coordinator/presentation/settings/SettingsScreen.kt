package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Person3
import androidx.compose.material.icons.rounded.PhoneIphone
import androidx.compose.material.icons.rounded.Phonelink
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.LoadingDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.LoadingIndicator
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.MessageDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.QuestionDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.composable.NameChangeDialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getAppInfo
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navHostController: NavHostController, viewModel: SettingsViewModel) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val selectFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { fileUri ->
        if (fileUri != null) {
            viewModel.state.selectedFileUri = fileUri
            viewModel.onEvent(
                SettingsEvent.ShowQuestionDialogEvent(
                    context.getString(R.string.import_data),
                    context.getString(R.string.really_want_to_import_data)
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(SettingsEvent.LoadDataEvent)
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
                            text = stringResource(R.string.settings),
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
                                navHostController.popBackStack(
                                    Route.HomeScreen.route, inclusive = false, saveState = false
                                )
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
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    //personal data
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.personal_data).toUpperCase(Locale.ROOT),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                viewModel.onEvent(SettingsEvent.ShowUserNameChangeDialogEvent)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.your_name),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = state.userName,
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    //app data
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.app_data).toUpperCase(Locale.ROOT),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                navHostController.navigate(Route.PlacesScreen.route)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.places),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                navHostController.navigate(Route.PeoplesScreen.route)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.peoples),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    //import export data
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.import_export_data).toUpperCase(Locale.ROOT),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                selectFileLauncher.launch("*/*")
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.import_data),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                viewModel.onEvent(SettingsEvent.ExportDataOnClick)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.export_data),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    //developer info
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.additionally).toUpperCase(Locale.ROOT),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                navHostController.navigate(Route.DeveloperScreen.route)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Code,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.info_about_developer),
                                fontFamily = caros,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    //app info
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = getAppInfo(context),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center
                    )
                }
                if (state.contentState.isLoading.value) {
                    LoadingDialog(onDismissRequest = {})
                }
            }
        }

        if (state.showUserNameChangeDialog) {
            NameChangeDialog(userName = state.userName, onDismissRequest = {
                viewModel.onEvent(SettingsEvent.HideUserNameChangeDialogEvent)
            }, onSaveClick = {
                viewModel.onEvent(SettingsEvent.UserNameChangeEvent(it))
            })
        }

        BackHandler {
            navHostController.popBackStack(
                Route.HomeScreen.route, inclusive = false, saveState = false
            )
        }

        if (state.showMessageDialog) {
            MessageDialog(message = state.contentState.data.value ?: "",
                onDismissRequest = {
                    viewModel.onEvent(SettingsEvent.HideMessageDialogEvent)
                })
        }

        if (state.showQuestionDialog) {
            QuestionDialog(title = state.questionTitle,
                text = state.questionText,
                onDismissRequest = {
                    viewModel.onEvent(SettingsEvent.HideQuestionDialogEvent)
                },
                onConfirmRequest = {
                    viewModel.onEvent(SettingsEvent.ImportDataOnClick)
                })
        }
    }
}