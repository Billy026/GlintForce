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
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.common.roundedToNearestTens
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

/**
 * Composable to filter out list needed for line chart
 */
@Composable
fun LineChartDiagram(
    list: List<Expense>,
    onConfirm: () -> Unit,
) {
    if (list.isNotEmpty()) {
        val last = list.last().date
        val date = last.filterIndexed { index, _ -> index < 2 }.toInt()
        val month = last.filterIndexed { index, _ -> index > 1 }
        val populatedList = mutableListOf<String>()

        for (i in 0..date) {
            populatedList.add(
                if (i < 10) {
                    "0${i}${month}"
                } else {
                    "${i}${month}"
                }
            )
        }

        // List containing map from date to its total cost
        val combinedList = populatedList.map { str ->
            if (list.any { it.date.contains(other = str) }) {
                Pair(
                    str,
                    list.filter { it.date.contains(other = str) }
                        .map { it.cost }
                        .reduce { acc, cost -> acc + cost }
                )
            } else {
                Pair(str, 0.0)
            }
        }

        LineChart(
            list = combinedList,
            onConfirm = onConfirm,
        )
    }
}

/**
 * Composable to create line chart
 */
@Composable
private fun LineChart(
    // Values
    list: List<Pair<String, Double>>,

    // Actions
    onConfirm: () -> Unit,
) {
    val xData = mutableMapOf<String, Float>()
    list.forEachIndexed { index, pair ->
        xData.set(key = pair.first.filterIndexed { id, _ -> id < 6 }, value = index.toFloat())
    }
    val xToDateMapKey = ExtraStore.Key<List<String>>()

    val yData = mutableMapOf<String, Float>()
    for (i in 0..roundedToNearestTens(double = list.maxOf { it.second }) / 10) {
        yData.set(key = "S$ ${i * 10}", value = i.toFloat())
    }
    val yToPriceMapKey = ExtraStore.Key<List<String>>()

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                modelProducer.runTransaction {
                    lineSeries { series(y = list.map { it.second.toFloat() }) }
                    extras { it[xToDateMapKey] = xData.keys.toList() }
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
                rememberLineCartesianLayer(
                    spacing = 70.dp,
                    lines = listOf(
                        rememberLineSpec(
                            shader = DynamicShader.color(color = MaterialTheme.colorScheme.primary),
                        )
                    ),
                    axisValueOverrider = AxisValueOverrider.fixed(
                        minX = 1f,
                        maxY = roundedToNearestTens(double = list.maxOf { it.second }).toFloat()
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
                bottomAxis =
                rememberBottomAxis(
                    axis = rememberLineComponent(Color.Gray),
                    label = rememberTextComponent(
                        color = MaterialTheme.colorScheme.onSurface,
                        textSize = 15.sp,
                        padding = Dimensions.of(horizontal = 4.dp, vertical = 2.dp),
                        minWidth = TextComponent.MinWidth.fixed(valueDp = 5f)
                    ),
                    valueFormatter = { x, chartValues, _ ->
                        chartValues.model.extraStore[xToDateMapKey][x.toInt()]
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
            scrollState = rememberVicoScrollState(),
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