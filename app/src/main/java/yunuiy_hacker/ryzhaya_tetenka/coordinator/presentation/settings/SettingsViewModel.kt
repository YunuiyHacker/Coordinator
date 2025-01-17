package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

import android.app.Application
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.ExportDataUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.ImportDataUseCase
import java.io.BufferedWriter
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val exportDataUseCase: ExportDataUseCase,
    private val importDataUseCase: ImportDataUseCase,
    private val application: Application
) : ViewModel() {
    val state by mutableStateOf(SettingsState())

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LoadDataEvent -> loadData()

            is SettingsEvent.ShowUserNameChangeDialogEvent -> state.showUserNameChangeDialog = true
            is SettingsEvent.HideUserNameChangeDialogEvent -> state.showUserNameChangeDialog = false
            is SettingsEvent.UserNameChangeEvent -> {
                sharedPrefsHelper.name = event.userName
                state.userName = event.userName

                state.showUserNameChangeDialog = false
            }

            is SettingsEvent.ShowMessageDialogEvent -> state.showMessageDialog = true
            is SettingsEvent.HideMessageDialogEvent -> state.showMessageDialog = false

            is SettingsEvent.ShowQuestionDialogEvent -> {
                state.questionTitle = event.title
                state.questionText = event.text
                state.showQuestionDialog = true
            }

            is SettingsEvent.HideQuestionDialogEvent -> state.showQuestionDialog = false

            is SettingsEvent.ExportDataOnClick -> exportData()
            is SettingsEvent.ImportDataOnClick -> importData()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.userName = sharedPrefsHelper.name ?: ""

                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun exportData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                val exportedString = exportDataUseCase.invoke()
                val fileName: String =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/coordinator_export" + Date().time + ".json"
                Files.createFile(Paths.get(fileName))
                val bw = BufferedWriter(FileWriter(fileName))
                bw.write(exportedString)
                bw.flush()
                bw.close()

                state.contentState.data.value = application.getString(R.string.success_export)
                state.showMessageDialog = true
                state.contentState.isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun importData() {
        state.showQuestionDialog = false
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
//                try {
                    importDataUseCase.invoke(state.selectedFileUri)

                    state.contentState.data.value = application.getString(R.string.success_import)
                    state.showMessageDialog = true
                    state.contentState.isLoading.value = false
//                } catch (e: Exception) {
//                    state.contentState.exception.value =
//                        Exception(application.getString(R.string.import_exception))
//                    state.contentState.isLoading.value = false
//                }
            }
        }
    }

}