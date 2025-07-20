package com.billy.glintforce.mainApplication.databaseTab.charts

import android.graphics.Typeface
import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.categoryDatabase.Category
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.common.roundedToNearestHundred
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

/**
 * Composable to filter out list needed for bar chart
 */
@Composable
fun BarChartDiagram(
    // Values
    list: List<Expense>,
    categoryList: List<Category>,

    // Actions
    onConfirm: () -> Unit
) {
    if (list.isNotEmpty()) {
        // List containing map from Category to its total cost
        val combinedList = categoryList.map { category ->
            Pair(
                category.desc,
                if (list.any { it.category == category.desc }) {
                    list.filter { it.category == category.desc }
                        .map { it.cost }
                        .reduce { acc, cost -> acc + cost }
                } else {
                    0.0
                }
            )
        }

        BarChart(list = combinedList, onConfirm = onConfirm)
    }
}

/**
 * Composable to create bar chart
 */
@Composable
private fun BarChart(
    // Values
    list: List<Pair<String, Double>>,

    // Actions
    onConfirm: () -> Unit
) {
    val xData = mutableMapOf<String, Float>()
    list.forEachIndexed { index, pair ->
        xData.set(key = pair.first, value = index.toFloat())
    }
    val xToCategoryMapKey = ExtraStore.Key<List<String>>()

    val yData = mutableMapOf<String, Float>()
    for (i in 0..roundedToNearestHundred(double = list.maxOf { it.second }) / 10) {
        yData.set(key = "S$ ${i * 10}", value = i.toFloat())
    }
    val yToPriceMapKey = ExtraStore.Key<List<String>>()

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                modelProducer.runTransaction {
                    columnSeries { series(y = list.map { it.second.toFloat() }) }
                    extras { it[xToCategoryMapKey] = xData.keys.toList() }
                    extras { it[yToPriceMapKey] = yData.keys.toList() }
                }
                delay(100)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CartesianChartHost(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            color = MaterialTheme.colorScheme.tertiary,
                            thickness = 16.dp,
                            shape = Shape.rounded(allPercent = 40)
                        )
                    ),
                    axisValueOverrider = AxisValueOverrider.fixed(
                        minX = 0f,
                        maxY = roundedToNearestHundred(double = list.maxOf { it.second }).toFloat()
                    ),
                ),
                startAxis = rememberStartAxis(
                    label = rememberTextComponent(
                        color = MaterialTheme.colorScheme.onSurface,
                        textSize = 15.sp,
                        padding = Dimensions.of(horizontal = 4.dp, vertical = 2.dp),
                    ),
                    valueFormatter = { y, chartValues, _ ->
                        chartValues.model.extraStore[yToPriceMapKey][
                            (y / 10).toString().takeWhile { it != '.' }.toInt()
                        ]
                    },
                    guideline = rememberLineComponent(
                        color = Color.Gray,
                    ),
                ),
                bottomAxis = rememberBottomAxis(
                    axis = rememberLineComponent(Color.Gray),
                    label = rememberTextComponent(
                        color = MaterialTheme.colorScheme.onSurface,
                        textSize = 15.sp,
                        padding = Dimensions.of(horizontal = 4.dp, vertical = 2.dp),
                        minWidth = TextComponent.MinWidth.fixed(valueDp = 5f)
                    ),
                    valueFormatter = { x, chartValues, _ ->
                        chartValues.model.extraStore[xToCategoryMapKey][x.toInt()]
                    },
                    guideline = null,
                ),
            ),
            modelProducer = modelProducer,
            marker = rememberDefaultCartesianMarker(
                labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
                label = rememberTextComponent(
                    color = Color.Black,
                    textSize = 16.sp,
                    typeface = Typeface.MONOSPACE,
                    textAlignment = Layout.Alignment.ALIGN_CENTER,
                    padding = Dimensions.of(8.dp, 4.dp),
                    minWidth = TextComponent.MinWidth.fixed(valueDp = 5f),
                ),
            ),
            scrollState = rememberVicoScrollState()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.confirm))
            }
        }
    }
}