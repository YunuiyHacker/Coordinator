package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.common.composable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.common.model.People
import yunuiy_hacker.ryzhaya_tetenka.coordinator.ui.theme.caros
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.util.toStringFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUpdatePeopleColumn(
    modifier: Modifier = Modifier,
    people: People? = null,
    onDismissRequest: () -> Unit,
    onSaveClick: (people: People, uriOfImage: Uri) -> Unit,
    isEditMode: Boolean,
    isReadOnly: Boolean = false,
    onDeleteClick: (people: People) -> Unit,
    showButtons: Boolean = true,
    onClickPhoto: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val notIndicationInteractionSource = remember { MutableInteractionSource() }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedImageUri by remember {
        mutableStateOf(Uri.parse(""))
    }
    val imagePicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            if (it != null) selectedImageUri = it
        }
    var isEditMode by remember {
        mutableStateOf(isEditMode)
    }
    var isReadOnly by remember {
        mutableStateOf(isReadOnly)
    }

    var avatarPath by remember { mutableStateOf(people?.avatarPath) }
    var surname by remember { mutableStateOf(people?.surname ?: "") }
    var name by remember { mutableStateOf(people?.name ?: "") }
    var lastname by remember { mutableStateOf(people?.lastname ?: "") }
    var sex by remember { mutableStateOf(people?.sex ?: true) }
    var dateOfBirthInMilliseconds by remember { mutableStateOf(people?.dateOfBirthInMilliseconds) }
    var displayName by remember { mutableStateOf(people?.displayName ?: "") }
    var address by remember { mutableStateOf(people?.address ?: "") }

    val outlinedTextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledTextColor = MaterialTheme.colorScheme.onSurface
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Column(
            Modifier
                .padding(24.dp)
                .animateContentSize()
        ) {
            AnimatedVisibility(!isReadOnly) {
                Column {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (people == null) stringResource(R.string.people_adding) else stringResource(
                            R.string.people_editing
                        ),
                        textAlign = TextAlign.Center,
                        fontFamily = caros,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            if (selectedImageUri.toString()
                    .isEmpty() && (people == null || avatarPath?.isEmpty()!!)
            ) {
                Icon(modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape
                    )
                    .padding(24.dp)
                    .clickable(
                        interactionSource = notIndicationInteractionSource, indication = null
                    ) {
                        if (!isReadOnly) imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
                    imageVector = Icons.Rounded.CameraAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface)
            } else {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    AsyncImage(modifier = Modifier
                        .size(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = notIndicationInteractionSource,
                            indication = null
                        ) {
                            if (!isReadOnly) imagePicker.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                            else onClickPhoto()
                        }
                        .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (selectedImageUri.path?.isNotEmpty()!!) selectedImageUri else ImageUtils.IMG_DIR + "/" + avatarPath)
                            .crossfade(enable = true).build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop)
                }
                if (!isReadOnly || selectedImageUri.path?.isNotEmpty()!!) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally)
                            .offset(x = 40.dp, y = -30.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .clickable {
                                selectedImageUri = Uri.parse("")
                                avatarPath = ""
                            }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.Center),
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                onValueChange = {
                    surname = it.filter { letter -> letter.isLetter() }
                },
                colors = outlinedTextFieldColors,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.surname),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.surname),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it.filter { letter -> letter.isLetter() }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.name),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.name),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = lastname,
                onValueChange = {
                    lastname = it.filter { letter -> letter.isLetter() }
                },
                colors = outlinedTextFieldColors,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.lastname),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.lastname),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.sex), style = TextStyle(
                        fontFamily = caros, fontSize = 16.sp, fontStyle = FontStyle.Normal
                    ), color = MaterialTheme.colorScheme.onSurface
                )
                Row(modifier = Modifier.weight(1f)) { }
                OutlinedIconToggleButton(
                    modifier = Modifier.weight(1f), checked = sex, onCheckedChange = {
                        if (!isReadOnly) sex = !sex
                    }, colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = MaterialTheme.colorScheme.primary,
                    ), shape = RoundedCornerShape(16.dp),
                    border = if (!sex) BorderStroke(
                        width = 0.7.dp,
                        color = if (!isReadOnly) MaterialTheme.colorScheme.onSurface else outlinedTextFieldColors.disabledIndicatorColor
                    ) else null
                ) {
                    Text(
                        text = stringResource(R.string.male),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedIconToggleButton(
                    modifier = Modifier.weight(1f), checked = !sex, onCheckedChange = {
                        if (!isReadOnly) sex = !sex
                    }, colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        containerColor = Color.Transparent,
                        checkedContainerColor = MaterialTheme.colorScheme.primary
                    ), shape = RoundedCornerShape(16.dp),
                    border = if (sex) BorderStroke(
                        width = 0.7.dp,
                        color = if (!isReadOnly) MaterialTheme.colorScheme.onSurface else outlinedTextFieldColors.disabledIndicatorColor
                    ) else null
                ) {
                    Text(
                        text = stringResource(R.string.female),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                value = if (dateOfBirthInMilliseconds != null) Date(
                    dateOfBirthInMilliseconds!!
                ).toStringFormat()
                else stringResource(R.string.not_selected).toLowerCase(
                    Locale.ROOT
                ),
                onValueChange = {},
                colors = outlinedTextFieldColors,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.date_of_birth),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.date_of_birth),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = displayName,
                onValueChange = {
                    displayName = it
                },
                colors = outlinedTextFieldColors,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.display_name),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.display_name),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = address,
                onValueChange = {
                    address = it
                },
                colors = outlinedTextFieldColors,
                shape = RoundedCornerShape(16.dp),
                label = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.address),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.address),
                        style = TextStyle(fontFamily = caros, fontStyle = FontStyle.Normal),
                        textAlign = TextAlign.Left
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Left),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                ),
                readOnly = isReadOnly,
                enabled = !isReadOnly
            )
            if (showButtons) {
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(!isReadOnly) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            modifier = Modifier.weight(1f), onClick = {
                                onDismissRequest()
                            }, colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = Color.DarkGray
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
                                onSaveClick(
                                    if (!isEditMode) People(
                                        avatarPath = avatarPath ?: "",
                                        surname = surname,
                                        name = name,
                                        lastname = lastname,
                                        sex = sex,
                                        dateOfBirthInMilliseconds = dateOfBirthInMilliseconds,
                                        displayName = displayName,
                                        address = address
                                    )
                                    else people?.copy(
                                        avatarPath = avatarPath ?: "",
                                        surname = surname,
                                        name = name,
                                        lastname = lastname,
                                        sex = sex,
                                        dateOfBirthInMilliseconds = dateOfBirthInMilliseconds,
                                        displayName = displayName,
                                        address = address
                                    ) ?: People(), selectedImageUri
                                )
                            },
                            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                            shape = RoundedCornerShape(10.dp),
                            enabled = surname.isNotEmpty() || name.isNotEmpty() || lastname.isNotEmpty()
                        ) {
                            Text(
                                text = if (people != null) stringResource(R.string.save) else stringResource(
                                    R.string.add
                                ), fontFamily = caros
                            )
                        }
                    }
                }
                AnimatedVisibility(isReadOnly) {
                    Column {
                        Button(
                            modifier = Modifier.fillMaxWidth(), onClick = {
                                isReadOnly = false
                                isEditMode = true
                            }, colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = MaterialTheme.colorScheme.primary
                            ), shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.edit), fontFamily = caros
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onDeleteClick(people!!)
                            },
                            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.delete), fontFamily = caros
                            )
                        }
                    }
                }
            }
        }

        if (interactionSource.collectIsPressedAsState().value) if (!isReadOnly) showDatePickerDialog =
            true

        if (showDatePickerDialog) {
            DatePickerDialog(
                modifier = Modifier.heightIn(max = 700.dp),
                onDismissRequest = {
                    showDatePickerDialog = false
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
                            dateOfBirthInMilliseconds =
                                if (datePickerState.selectedDateMillis != null)
                                    datePickerState.selectedDateMillis!! else null
                            showDatePickerDialog = false
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
    }
}