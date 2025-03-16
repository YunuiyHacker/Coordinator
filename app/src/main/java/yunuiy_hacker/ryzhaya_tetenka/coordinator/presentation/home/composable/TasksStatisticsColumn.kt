package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.home.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun TasksStatisticsColumn(
    modifier: Modifier = Modifier,
    completedTasks: Int,
    notCompletedTasks: Int,
    allCompletedTasks: Int,
    allNotCompletedTasks: Int
) {
    val interactionSource = remember { MutableInteractionSource() }

    var showStatisticsForAllTasks by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp)), verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(interactionSource = interactionSource, indication = null) {
                            showStatisticsForAllTasks = !showStatisticsForAllTasks
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.completed),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = if (!showStatisticsForAllTasks) completedTasks.toString() else allCompletedTasks.toString(),
                        fontFamily = caros,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(interactionSource = interactionSource, indication = null) {
                            showStatisticsForAllTasks = !showStatisticsForAllTasks
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.not_completed),
                        fontFamily = caros,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = if (!showStatisticsForAllTasks) notCompletedTasks.toString() else allNotCompletedTasks.toString(),
                        fontFamily = caros,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            AnimatedVisibility(showStatisticsForAllTasks) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.displaying_statistics_for_all_tasks),
                        fontFamily = caros,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}