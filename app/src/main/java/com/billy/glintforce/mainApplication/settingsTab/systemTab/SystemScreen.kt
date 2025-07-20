@file:OptIn(ExperimentalMaterial3Api::class)

package com.billy.glintforce.mainApplication.settingsTab.systemTab

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.common.TopAppBar
import com.billy.glintforce.viewModel.settings.SystemViewModel
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import kotlinx.coroutines.launch

/**
 * Composable to change settings related to the entire application
 */
@Composable
fun SystemScreen(
    // Values
    modifier: Modifier = Modifier,

    // Actions
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    changeTheme: () -> Unit,

    // ViewModels
    viewModel: SystemViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    userPrefRepoViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val userPrefUiState by userPrefRepoViewModel.userPrefRepoUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    UpdateListLang(
        coroutineScope = coroutineScope,
        uiState = uiState,
        viewModel = viewModel
    )

    val langMap = mapOf(
        stringResource(id = R.string.eng) to "en",
        stringResource(id = R.string.chi) to "zh",
        stringResource(id = R.string.malay) to "ms",
        stringResource(id = R.string.indo) to "in",
        stringResource(id = R.string.jap) to "ja",
        stringResource(id = R.string.kr) to "ko",
        stringResource(id = R.string.th) to "th",
        stringResource(id = R.string.viet) to "vi",
    )
    val rateList = listOf(
        stringResource(id = R.string.weekly),
        stringResource(id = R.string.biweekly),
        stringResource(id = R.string.monthly)
    )

    val langList = listOf("en", "zh", "ms", "in", "ja", "ko", "th", "vi")

    viewModel.updateLangList(
        list = listOf(
            stringResource(id = R.string.eng),
            stringResource(id = R.string.chi),
            stringResource(id = R.string.malay),
            stringResource(id = R.string.indo),
            stringResource(id = R.string.jap),
            stringResource(id = R.string.kr),
            stringResource(id = R.string.th),
            stringResource(id = R.string.viet),
        )
    )
    viewModel.initializeRate(
        weekly = rateList[0],
        biweekly = rateList[1],
        monthly = rateList[2]
    )
    viewModel.setTheme(theme = isSystemInDarkTheme())
    viewModel.setTheme(
        theme = if (userPrefUiState.userPrefList.isEmpty()) {
            false
        } else {
            userPrefUiState.userPrefList.first().theme
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.system),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
            )
        }
    ) { innerPadding ->

        viewModel.updateLangSelected(
            index = if (userPrefUiState.userPrefList.isEmpty()) {
                0
            } else {
                langList.indexOf(element = userPrefUiState.userPrefList.first().lang)
            }
        )
        viewModel.updateRateSelected(
            index = if (userPrefUiState.userPrefList.isEmpty()) {
                0
            } else {
                when (userPrefUiState.userPrefList.first().report) {
                    7 -> 0
                    14 -> 1
                    30 -> 2
                    else -> 0
                }
            }
        )

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                DisplayedLanguage(
                    onClick = { index, text ->
                        viewModel.updateLangSelected(index = index)
                        viewModel.toggleLangExpansion()
                        viewModel.updateLang()
                        updateLang(
                            lang = text,
                            langMap = langMap
                        )
                        coroutineScope.launch {
                            viewModel.updateLangPref(
                                lang = langMap.getOrDefault(
                                    key = text,
                                    defaultValue = "en"
                                )
                            )
                        }
                    },
                    onDismissRequest = { viewModel.toggleLangExpansion() },
                    onExpandedChange = { viewModel.toggleLangExpansion() },
                    uiState = uiState
                )

            }

            // Not working
            item {
                ReportRate(
                    onCheckedChange = {
                        viewModel.toggleRateSwitch()
                        coroutineScope.launch {
                            viewModel.setReportRate(bool = uiState.rateSwitch)
                        }
                    },
                    onClick = { index ->
                        viewModel.toggleRateExpansion()
                        viewModel.updateRate(index = index)
                        coroutineScope.launch {
                            viewModel.updateReportRate(
                                weekly = rateList[0],
                                biweekly = rateList[1],
                                monthly = rateList[2]
                            )
                        }
                    },
                    onDismissRequest = { viewModel.toggleRateExpansion() },
                    onExpandedChange = { viewModel.toggleRateExpansion() },
                    uiState = uiState
                )
            }

            // Move to themes
            item {
                ChangeTheme(
                    onCheckedChange = {
                        viewModel.toggleThemeSwitch()
                        changeTheme()
                    },
                    uiState = uiState
                )
            }
        }
    }
}

// Changes default language
fun updateLang(lang: String, langMap: Map<String, String>) {
    val language = langMap.getOrDefault(key = lang, defaultValue = "en")
    val locale = LocaleListCompat.forLanguageTags(language)
    AppCompatDelegate.setApplicationLocales(locale)
}