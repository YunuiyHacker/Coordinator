package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings.dev

import android.R.attr.label
import android.R.attr.text
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import javax.inject.Inject


@HiltViewModel
class DeveloperViewModel @Inject constructor(private val application: Application) : ViewModel() {
    val state by mutableStateOf(DeveloperState())

    fun onEvent(event: DeveloperEvent) {
        when (event) {
            is DeveloperEvent.ShowDeveloperEvent -> state.showDeveloperDialog = true
            is DeveloperEvent.HideDeveloperEvent -> state.showDeveloperDialog = false

            is DeveloperEvent.CopyPhoneEvent -> {
                state.phoneCopied = true
                val clipboard =
                    application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip =
                    ClipData.newPlainText("phone", application.getString(R.string.developer_phone))
                clipboard!!.setPrimaryClip(clip)
                Toast.makeText(
                    application,
                    application.getString(R.string.copied),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is DeveloperEvent.ResetCopiedPhoneEvent -> {
                state.phoneCopied = false
            }
        }
    }
}