package com.billy.glintforce.viewModel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.billy.glintforce.GlintForceApplication
import com.billy.glintforce.viewModel.database.CategoryViewModel
import com.billy.glintforce.viewModel.database.DatabaseViewModel
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryDetailsViewModel
import com.billy.glintforce.viewModel.database.initiateRepo.CategoryRepoViewModel
import com.billy.glintforce.viewModel.home.AddExpenseViewModel
import com.billy.glintforce.viewModel.home.DisplayGoalViewModel
import com.billy.glintforce.viewModel.limit.goalType.GoalTypeViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalDetailsViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalRepoViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeDetailsViewModel
import com.billy.glintforce.viewModel.limit.initiateRepo.GoalTypeRepoViewModel
import com.billy.glintforce.viewModel.limit.mainLimitPage.LimitViewModel
import com.billy.glintforce.viewModel.main.MainViewModel
import com.billy.glintforce.viewModel.screenTemplate.GlintForceViewModel
import com.billy.glintforce.viewModel.settings.SystemViewModel
import com.billy.glintforce.viewModel.settings.UserViewModel
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Object to provide each view model with the repository it requires.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initialise MainViewModel
        initializer {
            MainViewModel(
                //userPrefRepository = glintForceApplication().settingsContainer.userPrefRepository
            )
        }

        // Initialise GlintForceViewModel
        initializer {
            GlintForceViewModel(
                userPrefRepo = glintForceApplication().settingsContainer.userPrefRepository
            )
        }

        // Initialise DisplayGoalViewModel with Goal Repository
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            DisplayGoalViewModel(
                goalRepository = glintForceApplication().limitContainer.goalRepository,
                userId = userId
            )
        }

        // Initialise AddExpenseViewModel with Expense Repository
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            AddExpenseViewModel(
                expenseRepository = glintForceApplication().databaseContainer.expenseRepository,
                categoryRepository = glintForceApplication().databaseContainer.categoryRepository,
                userId = userId
            )
        }
/**
        // Initialise ExpenseRepoViewModel with Expense Repository
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            ExpenseRepoViewModel(
                expenseRepository = glintForceApplication().databaseContainer.expenseRepository,
                userId = userId
            )
        }
**/
        // Initialise CategoryRepoViewModel with Category Repository
        initializer {
            CategoryRepoViewModel(
                categoryRepository = glintForceApplication().databaseContainer.categoryRepository
            )
        }

        // Initialise DatabaseViewModel with Expense Repository
        initializer {
            DatabaseViewModel(
                expenseRepository = glintForceApplication().databaseContainer.expenseRepository,
                categoryRepository = glintForceApplication().databaseContainer.categoryRepository
            )
        }

        // Initialise CategoryViewModel with Category Repository
        initializer {
            CategoryViewModel(
                categoryRepository = glintForceApplication().databaseContainer.categoryRepository
            )
        }

        // Initialise CategoryDetailsViewModel with Category Repository
        initializer {
            CategoryDetailsViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                categoryRepository = glintForceApplication().databaseContainer.categoryRepository
            )
        }

        // Initialise LimitViewModel with Goal and GoalType Repositories
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            LimitViewModel(
                goalRepository = glintForceApplication().limitContainer.goalRepository,
                goalTypeRepository = glintForceApplication().limitContainer.goalTypeRepository,
                userId = userId
            )
        }

        // Initialise LimitListViewModel with Goal Repository
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            GoalRepoViewModel(
                goalRepository = glintForceApplication().limitContainer.goalRepository,
                userId = userId
            )
        }

        // Initialise GoalDetailViewModel
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            GoalDetailsViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                goalRepository = glintForceApplication().limitContainer.goalRepository,
                userId = userId
            )
        }

        // Initialise TypeViewModel with Goal Type Repository
        initializer {
            GoalTypeViewModel(
                goalTypeRepository = glintForceApplication().limitContainer.goalTypeRepository
            )
        }

        // Initialise TypeRepoViewModel with Goal Type Repository
        initializer {
            GoalTypeRepoViewModel(
                goalTypeRepository = glintForceApplication().limitContainer.goalTypeRepository
            )
        }

        // Initialise GoalTypeDetailsViewModel
        initializer {
            GoalTypeDetailsViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                goalTypeRepository = glintForceApplication().limitContainer.goalTypeRepository
            )
        }

        // Initialise SystemViewModel
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            SystemViewModel(
                expenseRepository = glintForceApplication().databaseContainer.expenseRepository,
                categoryRepository = glintForceApplication().databaseContainer.categoryRepository,
                goalRepository = glintForceApplication().limitContainer.goalRepository,
                goalTypeRepository = glintForceApplication().limitContainer.goalTypeRepository,
                userPrefRepository = glintForceApplication().settingsContainer.userPrefRepository
            )
        }

        // Initialise UserPrefRepoViewModel
        initializer {
            UserPrefRepoViewModel(
                userPrefRepository = glintForceApplication().settingsContainer.userPrefRepository
            )
        }

        // Initialise UserViewModel
        initializer {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User ID is null")
            UserViewModel(
                userRepository = glintForceApplication().userContainer.userRepository,
                userId = userId
            )
        }
    }
}

/**
 * Allows additional information to be passed into glintForceApplication
 */
fun CreationExtras.glintForceApplication(): GlintForceApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as GlintForceApplication)