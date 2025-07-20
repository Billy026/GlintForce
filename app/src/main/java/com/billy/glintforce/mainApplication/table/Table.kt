package com.billy.glintforce.mainApplication.table

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.expenses.ExpenseViewModel
import com.billy.glintforce.data.expenses.Expenses
import com.billy.glintforce.data.expenses.deleteExpense
import com.billy.glintforce.viewModel.calendar.CalendarViewModel
import com.billy.glintforce.viewModel.database.DatabaseUiState
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

@Composable
fun Table(
    // Values
    modifier: Modifier = Modifier,
    userId: String?,
    ascending: Boolean,
    edit: ((String) -> Unit)?,

    // UiStates
    uiState: DatabaseUiState,
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory),
    calendarViewModel: CalendarViewModel = viewModel(),

    // ViewModels
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    val calendar by calendarViewModel.uiState.collectAsState()
    val expenses by expenseViewModel.expenseList.collectAsStateWithLifecycle()
    val monthlyExpenses = expenses.filter {
        it.date.substring(startIndex = 3) == calendar.fullCurrentDate.substring(startIndex = 3)
    }
    
    var darkTheme by remember { mutableStateOf(false) }
    var id: String? = null

    LaunchedEffect(key1 = Unit) {
        darkTheme = if (userUiState.userPrefList.isEmpty()) {
            false
        } else {
            userUiState.userPrefList.first().theme
        }

        if (edit != null) {
            Firebase.firestore.collection("expenses")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    for (document in snapshot.documents) {
                        Log.d("Firestore", "Document ID: ${document.id}, Data: ${document.data}")

                        id = document.id
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error fetching document", e)
                }
        }
    }

    val pairList: List<Pair<String, Dp>> = listOf(
        Pair(stringResource(id = R.string.filterDate), 120.dp),
        Pair(stringResource(id = R.string.filterTime), 60.dp),
        Pair(stringResource(id = R.string.filterCategory), 120.dp),
        Pair(stringResource(id = R.string.details), 160.dp),
        Pair(stringResource(id = R.string.amt), 120.dp)
    )

    // LazyColumn to represent table of expenses
    LazyRow(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 16.dp)
    ) {

        item {
            LazyColumn {
                item {
                    // Row for Table headers
                    Row(
                        modifier = Modifier
                            .background(Color(0xFF8db3d9))
                            .border(
                                1.dp,
                                if (darkTheme) Color.White else Color.Black
                            )
                    ) {
                        pairList.forEach {
                            TableCell(
                                text = it.first,
                                width = it.second,
                                darkTheme = darkTheme
                            )
                        }

                        if (edit != null) {
                            Column(
                                modifier = Modifier.width(96.dp)
                            ) {
                                Text(
                                    text = "Options",
                                    color = Color.Black,
                                    modifier = modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }

                items(
                    items =
                        sortList(
                            sort = uiState.currentSort,
                            asc = ascending,
                            list = if (uiState.currentFilter == 0) { monthlyExpenses }
                            else {
                                filterList(
                                    filter = uiState.currentFilter, list = monthlyExpenses,
                                    date = uiState.date, category = uiState.category,
                                    costs = Pair(uiState.minCost, uiState.maxCost)
                                )
                            }
                        )
                ) { expense ->
                    TableRow(
                        modifier = Modifier.fillMaxHeight(),
                        expense = expense,
                        widthList = pairList,
                        editDelete = if (edit != null && id != null) {
                            var docId = ""

                            Firebase.firestore.collection("expenses").document(id!!)
                                .collection("expense")
                                .whereEqualTo("category", expense.category)
                                .whereEqualTo("cost", expense.cost)
                                .whereEqualTo("date", expense.date)
                                .whereEqualTo("desc", expense.desc)
                                .whereEqualTo("month", expense.month)
                                .whereEqualTo("time", expense.time)
                                .whereEqualTo("userId", expense.userId)
                                .get()
                                .addOnSuccessListener { ref ->
                                    if (!ref.isEmpty) {
                                        for (document in ref.documents) {
                                            docId = document.id
                                            Log.d("Firestore - Expenses", "Expense added with ID:  $docId")
                                        }
                                    }
                                }.addOnFailureListener { e ->
                                    Log.w("Firestore - Expenses", "Error adding expense", e)
                                }

                            if (docId != "") {
                                Pair({edit(docId)}, { deleteExpense(userId = userId!!, expenseId = docId) })
                            } else {
                                null
                            }
                        } else { null },
                        darkTheme = darkTheme
                    )
                }
            }
        }
    }
}

@Composable
private fun TableRow(
    modifier: Modifier = Modifier,
    expense: Expenses,
    widthList: List<Pair<String, Dp>>,
    editDelete: Pair<() -> Unit, () -> Unit>?,
    darkTheme: Boolean
) {
    Row(
        modifier = Modifier.border(
            1.dp,
            if (darkTheme) Color.White else Color.Black
        )
    ) {
        TableCell(
            modifier = modifier,
            text = expense.date,
            width = widthList[0].second,
            darkTheme = darkTheme
        )
        TableCell(
            modifier = modifier,
            text = expense.time,
            width = widthList[1].second,
            darkTheme = darkTheme
        )
        TableCell(
            modifier = modifier,
            text = expense.category,
            width = widthList[2].second,
            darkTheme = darkTheme
        )
        TableCell(
            modifier = modifier,
            text = expense.desc,
            width = widthList[3].second,
            darkTheme = darkTheme
        )
        TableCell(
            modifier = modifier,
            text = "S$ ${String.format(Locale.getDefault(), "%.2f", expense.cost)}",
            width = widthList[4].second,
            darkTheme = darkTheme
        )
        if (editDelete != null) {
            Row(
                modifier = modifier
            ) {
                IconButton(onClick = { editDelete.first() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "edit expense")
                }
                IconButton(onClick = { editDelete.second() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete expense")
                }
            }
        }
    }
}

@Composable
private fun TableCell(
    modifier: Modifier = Modifier,
    text: String,
    width: Dp,
    textColor: Color? = null,
    darkTheme: Boolean
) {
    Text(
        text = text,
        color = textColor ?: if (darkTheme) Color.White else Color.Black,
        modifier = modifier
            .width(width)
            .padding(8.dp)
    )
}

private fun filterList(
    filter: Int, list: List<Expenses>, date: String,
    category: String, costs: Pair<String, String>
) : List<Expenses> {
    var filteredList = list

    when (filter) {
        1 -> filteredList = filteredList.filter { it.date == date }
        2 -> filteredList = filteredList.filter { it.category == category }
        3 -> // Cost greater than min cost or min cost unspecified
            filteredList = filteredList.filter {
                (if (costs.first != "") {
                    it.cost.compareTo(costs.first.toDouble()) >= 0
                } else {
                    true
                })  &&
                // Cost less than max cost or max cost unspecified
                (if (costs.second != "") {
                     it.cost.compareTo(costs.second.toDouble()) <= 0
                } else {
                     true
                })
            }
    }

    return filteredList
}

private fun sortList(
    sort: Int, asc: Boolean, list: List<Expenses>
) : List<Expenses> {
    var sortedList = list

    sortedList = when (sort) {
        0 -> if (asc) sortedList.sortedBy { "${it.date} ${it.time}" }
             else sortedList.sortedByDescending { "${it.date} ${it.time}" }
        1 -> if (asc) sortedList.sortedBy { it.category }
             else sortedList.sortedByDescending { it.category }
        else -> if (asc) sortedList.sortedBy { it.cost }
                else sortedList.sortedByDescending { it.cost }
    }

    return sortedList
}