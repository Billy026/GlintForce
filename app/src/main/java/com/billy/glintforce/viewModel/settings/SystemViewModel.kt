package com.billy.glintforce.viewModel.settings

import androidx.lifecycle.ViewModel
import com.billy.glintforce.data.toRemove.categoryDatabase.CategoryRepository
import com.billy.glintforce.data.toRemove.expenseDatabase.ExpenseRepository
import com.billy.glintforce.data.toRemove.goalDatabase.GoalRepository
import com.billy.glintforce.data.goalTypeTypeDatabase.GoalTypeRepository
import com.billy.glintforce.data.toRemove.userPreferenceDatabase.UserPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Class containing the SystemUiState and required functions to update and utilise it
 */
class SystemViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val goalRepository: GoalRepository,
    private val goalTypeRepository: GoalTypeRepository,
    private val userPrefRepository: UserPreferenceRepository,
) : ViewModel() {
    // Most recent SystemUiState
    private val _uiState = MutableStateFlow(SystemUiState())
    val uiState: StateFlow<SystemUiState> = _uiState.asStateFlow()

    // Initiates lists on start up
    fun updateLists(
        categoryList: List<String>,
        labelList: List<String>,
        goalTypeList: List<String>
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                categoryList = categoryList,
                labelList = labelList,
                goalTypeList = goalTypeList
            )
        }
    }

    // Initializes RateList
    fun initializeRate(
        weekly: String,
        biweekly: String,
        monthly: String
    ) {
        _uiState.update { currentState ->
            currentState.copy(rateList = listOf(weekly, biweekly, monthly))
        }
    }

    // Updates translated langList
    fun updateLangList(list: List<String>) {
        _uiState.update { currentState ->
            currentState.copy(langList = list)
        }
    }

    // Toggles DisplayedLanguage drop-down menu
    fun toggleLangExpansion() {
        _uiState.update { currentState ->
            currentState.copy(langIsExpanded = !currentState.langIsExpanded)
        }
    }

    // Toggles ReportRate drop-down menu
    fun toggleRateExpansion() {
        _uiState.update { currentState ->
            currentState.copy(rateIsExpanded = !currentState.rateIsExpanded)
        }
    }

    // Toggles switch state of ReportRate
    fun toggleRateSwitch() {
        _uiState.update { currentState ->
            currentState.copy(rateSwitch = !currentState.rateSwitch)
        }
    }

    // Toggles switch state of ChangeTheme
    fun toggleThemeSwitch() {
        _uiState.update { currentState ->
            currentState.copy(themeSwitch = !currentState.themeSwitch)
        }
    }

    // Sets switch state of ChangTheme
    fun setTheme(theme: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(themeSwitch = theme)
        }
    }

    // Saves user input from DisplayedLanguage drop-down menu
    fun updateLangSelected(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(langSelectedText = currentState.langList[index])
        }
    }

    // Saves user input from ReportRate drop-down menu
    fun updateRateSelected(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(rateSelectedText = currentState.rateList[index])
        }
    }

    // Saves selected user input from DisplayedLanguage drop-down menu
    fun updateLang() {
        _uiState.update { currentState ->
            currentState.copy(lang = currentState.langSelectedText)
        }
    }

    // Saves selected user input from ReportRate drop-down menu
    fun updateRate(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(rate = currentState.rateList[index])
        }
    }

    // Translates default Expense entities in ExpenseRepository
    suspend fun updateExpenseRepo(id: Int, category: String) {
        expenseRepository.updateTranslation(id = id, category = category)
    }

    // Translates default Category entities in CategoryRepository
    suspend fun updateCategoryRepo(id: Int, category: String) {
        categoryRepository.updateCategory(id = id, desc = category)
    }

    // Translates default Goal entities in GoalRepository
    suspend fun updateGoalRepo(id: Int, label: String, goalType: String) {
        goalRepository.updateTranslation(id = id, gORr = label, type = goalType)
    }

    // Translates default GoalType entities in GoalTypeRepository
    suspend fun updateGoalTypeRepo(id: Int, label: String, goalType: String) {
        goalTypeRepository.updateTranslation(id = id, gORr = label, type = goalType)
    }

    // Updates language preference property
    suspend fun updateLangPref(lang: String) {
        userPrefRepository.updateLang(id = 0, lang = lang)
    }

    // Updates report rate preference property
    suspend fun updateReportRate(
        weekly: String,
        biweekly: String,
        monthly: String
    ) {
        userPrefRepository.updateReportRate(
            id = 0,
            rate = when (uiState.value.rate) {
                weekly -> 7
                biweekly -> 14
                monthly -> 30
                else -> 30
            }
        )
    }

    // Turns on/off report rate property
    suspend fun setReportRate(bool: Boolean) {
        if (bool) {
            userPrefRepository.updateReportRate(id = 0, rate = 7)
        } else {
            userPrefRepository.updateReportRate(id = 0, rate = -1)
        }
    }
}