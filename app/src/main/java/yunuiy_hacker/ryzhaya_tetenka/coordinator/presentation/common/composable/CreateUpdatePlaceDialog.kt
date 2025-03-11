package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.Place
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.getLaAndLtFromString

@Composable
fun CreateUpdatePlaceDialog(
    onDismissRequest: () -> Unit,
    onAddClick: (Place) -> Unit,
    place: Place? = null,
    isEditMode: Boolean
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var localPlace = remember { place }
    var title by remember { mutableStateOf(localPlace?.title ?: "") }
    var la by remember { mutableDoubleStateOf(localPlace?.la ?: 0.0) }
    var lt by remember { mutableDoubleStateOf(localPlace?.lt ?: 0.0) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .requiredWidth(360.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isEditMode) stringResource(R.string.place_editing) else stringResource(
                    R.string.place_adding
                ),
                fontSize = 16.sp,
                fontFamily = caros,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = {
                    title = it
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.title),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = la.toString(),
                    onValueChange = {
                        if (it.toFloatOrNull() != null) if (it.toFloat() >= -90 && it.toFloat() <= 90) la =
                            String.format("%.6f", it.toDouble()).replace(",", ".").toDouble()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    label = {
                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.latitude),
                            style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                            textAlign = TextAlign.Left
                        )
                    },
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.latitude),
                            style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                            textAlign = TextAlign.Left
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Left),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = lt.toString(),
                    onValueChange = {
                        if (it.toFloatOrNull() != null) if (it.toFloat() >= -180 && it.toFloat() <= 180) lt =
                            String.format("%.6f", it.toDouble()).replace(",", ".").toDouble()
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(16.dp),
                    label = {
                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.longitude),
                            style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                            textAlign = TextAlign.Left
                        )
                    },
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.longitude),
                            style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                            textAlign = TextAlign.Left
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Left),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    val laAndLt = getLaAndLtFromString(clipboardManager.getText()?.text.toString())
                    if (laAndLt != null) {
                        la = laAndLt[0]
                        lt = laAndLt[1]
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.no_matching_data_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ), shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.paste_coordinates_from_google_or_yandex_maps
                    ), fontFamily = caros, textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f), onClick = {
                        onDismissRequest()
                    }, colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ), shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.cancel
                        ), fontFamily = caros
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAddClick(
                            if (!isEditMode) Place(
                                title = title,
                                la = la.toString().toDouble(),
                                lt = lt.toString().toDouble()
                            )
                            else localPlace!!.copy(
                                id = place?.id ?: 0,
                                title = title,
                                la = la.toString().toDouble(),
                                lt = lt.toString().toDouble()
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    enabled = title.isNotEmpty() && la != 0.0 && lt != 0.0
                ) {
                    Text(
                        text = if (isEditMode) stringResource(R.string.edit) else stringResource(
                            R.string.add
                        ), fontFamily = caros
                    )
                }
            }
        }
    }
}