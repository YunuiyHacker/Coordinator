package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.dev.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros

@Composable
fun SupportDeveloper(modifier: Modifier = Modifier, onCopyClick: () -> Unit) {
    Column(
        modifier = modifier
            .requiredWidth(360.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.support_developer),
            fontFamily = caros,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.support_developer_text_part1))
                append(" ")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(stringResource(R.string.support_developer_text_part2))
                }
            },
            fontFamily = caros,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 0.3.dp, color = Color.DarkGray, RoundedCornerShape(10.dp)
                    )
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                    text = stringResource(R.string.developer_phone_formatted),
                    fontFamily = caros,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier,
                onClick = {
                    onCopyClick()
                },
                border = BorderStroke(width = 0.3.dp, color = Color.DarkGray),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.copy),
                    fontFamily = caros,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}