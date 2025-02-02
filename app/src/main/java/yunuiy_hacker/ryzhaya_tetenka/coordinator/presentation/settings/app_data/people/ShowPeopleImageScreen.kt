package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.app_data.people

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPeopleImageScreen(imageName: String, displayName: String, onDismissRequest: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            title = {
                Text(
                    modifier = Modifier.offset(x = -24.dp).fillMaxWidth(),
                    text = displayName,
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
                        onDismissRequest()
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

            })
    }) {
        Column(modifier = Modifier.padding(it)) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(context)
                    .data(ImageUtils.IMG_DIR + "/" + imageName).build(),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}