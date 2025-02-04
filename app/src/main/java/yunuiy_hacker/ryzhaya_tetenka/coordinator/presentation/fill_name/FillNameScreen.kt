package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.fill_name

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.nav_graph.Route
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun FillNameScreen(
    navHostController: NavHostController, viewModel: FillNameViewModel = hiltViewModel()
) {
    val paddingValues = WindowInsets.navigationBars.asPaddingValues()

    viewModel.state.let { state ->
        Scaffold(bottomBar = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        viewModel.onEvent(FillNameEvent.OnClickButton)
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = state.name.length >= 2
                ) {
                    Text(
                        text = stringResource(R.string.get_start),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(64.dp))
                Text(
                    text = stringResource(R.string.what_is_your_name_title),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = caros,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.what_is_your_name_description),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = caros,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(64.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = state.name,
                    onValueChange = {
                        viewModel.onEvent(FillNameEvent.ChangeNameEvent(it.take(23)))
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.your_name),
                            style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    singleLine = true
                )
            }
        }

        LaunchedEffect(state.succes) {
            if (state.succes) {
                navHostController.navigate(Route.HomeScreen.route)
            }
        }

        BackHandler {

        }
    }
}