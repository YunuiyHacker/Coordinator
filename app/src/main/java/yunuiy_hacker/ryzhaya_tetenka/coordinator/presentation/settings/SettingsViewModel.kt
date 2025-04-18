package yunuiy_hacker.ryzhaya_tetenka.coordinator.presentation.settings

import android.app.Application
import android.content.Context
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import yunuiy_hacker.ryzhaya_tetenka.coordinator.R
import yunuiy_hacker.ryzhaya_tetenka.coordinator.data.local.shared_prefs.SharedPrefsHelper
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.ExportDataUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.domain.settings.use_case.ImportDataUseCase
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.ImageUtils
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getFileName
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLanguages
import yunuiy_hacker.ryzhaya_tetenka.coordinator.utils.getLocaleStringResource
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.nio.file.FileSystemNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
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

            is SettingsEvent.ChangeThemeToLightEvent -> {
                state.isDarkTheme = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            is SettingsEvent.ToggleThemeEvent -> {
                state.isDarkTheme = !state.isDarkTheme
                AppCompatDelegate.setDefaultNightMode(if (state.isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            }

            is SettingsEvent.ChangeThemeToDarkEvent -> {
                state.isDarkTheme = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        state.contentState.isLoading.value = true

        GlobalScope.launch(Dispatchers.IO) {
            runBlocking {
                state.userName = sharedPrefsHelper.name ?: ""
                state.isDarkTheme = sharedPrefsHelper.isDarkTheme

                val savedLanguage = sharedPrefsHelper.language
                state.language =
                    getLanguages(application).find {
                        it.ISOCode.toLowerCase(Locale.ROOT)
                            .equals(if (!savedLanguage.isNullOrEmpty()) savedLanguage else "ru")
                    }!!

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
                val downloadDirectory: String =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                if (Files.notExists(Paths.get(downloadDirectory))) Files.createDirectory(
                    Paths.get(
                        downloadDirectory
                    )
                )
                val date = Date().time
                val jsonFileName: String =
                    downloadDirectory + "/coordinator_export" + date + ".json"
                val imgDir = File(ImageUtils.IMG_DIR)

                Files.createFile(Paths.get(jsonFileName))
                val bw = BufferedWriter(FileWriter(jsonFileName))
                bw.write(exportedString)
                bw.flush()
                bw.close()

                val files = imgDir.listFiles()?.filter { it.isFile }?.map { it.absolutePath }
                    ?.plus(jsonFileName)
                withContext(Dispatchers.IO) {
                    ZipOutputStream(BufferedOutputStream(FileOutputStream(downloadDirectory + "/coordinator_export" + date + ".crd"))).use { out ->
                        if (files != null) {
                            for (file in files) {
                                FileInputStream(file).use { fi ->
                                    BufferedInputStream(fi).use { origin ->
                                        val entry = ZipEntry(file.substring(file.lastIndexOf("/")))
                                        out.putNextEntry(entry)
                                        origin.copyTo(out, 1024)
                                    }
                                }
                            }
                        }
                    }
                }
                Files.delete(Paths.get(jsonFileName))

                state.contentState.data.value = getLocaleStringResource(
                    application.resources.configuration.locale,
                    R.string.success_export,
                    application as Context
                )
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
                try {
                    if (getFileName(
                            application, state.selectedFileUri
                        ).endsWith(".crd")
                    ) {
                        importDataUseCase.invoke(state.selectedFileUri)
                        state.contentState.data.value = getLocaleStringResource(
                            application.resources.configuration.locale,
                            R.string.success_import,
                            application as Context
                        )
                    } else {
                        throw FileSystemNotFoundException()
                    }
                } catch (e: FileSystemNotFoundException) {
                    state.contentState.data.value =
                        getLocaleStringResource(
                            application.resources.configuration.locale,
                            R.string.incorrect_file_format,
                            application as Context
                        )
                } catch (e: FileNotFoundException) {
                    state.contentState.data.value = getLocaleStringResource(
                        application.resources.configuration.locale,
                        R.string.access_denied,
                        application as Context
                    )
                } catch (e: Exception) {
                    state.contentState.data.value = getLocaleStringResource(
                        application.resources.configuration.locale,
                        R.string.undefined_error,
                        application as Context
                    )
                }

                state.showMessageDialog = true
                state.contentState.isLoading.value = false
            }
        }
    }
}