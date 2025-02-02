package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.onboarding.pages

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddLocationAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable.PlaceRow
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.Constants
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getMapUri

@Composable
fun OnboardingSecondScreen(modifier: Modifier = Modifier, isLoading: Boolean, place: Place) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )
    val context = LocalContext.current

    Column(modifier = modifier) {
        Icon(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.CenterHorizontally),
            imageVector = Icons.Rounded.AddLocationAlt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        AnimatedVisibility(!isLoading) {
            PlaceRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin.Center
                    },
                place = place,
                onClick = {
                    val mapIntent =
                        Intent(Intent.ACTION_VIEW, getMapUri(place.la, place.lt))
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
                },
                onLongClick = {},
                onDeleteClick = {},
                showDeletionButton = false
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.onboarding_screen2_title),
            fontFamily = caros,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.onboarding_screen2_content),
            fontFamily = caros,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}