package com.billy.glintforce.mainApplication.databaseTab.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billy.glintforce.R
import com.billy.glintforce.data.toRemove.expenseDatabase.Expense
import com.billy.glintforce.viewModel.settings.initiateRepo.UserPrefRepoViewModel
import java.util.Locale
import kotlin.math.PI
import kotlin.math.atan2

data class PieChartInput(
    val color: Color,
    val value: Double,
    val description: String,
    val isTapped: Boolean = false
)

/**
 * Composable to filter out list needed for pie chart
 */
@Composable
fun PieChartDiagram(
    // Values
    list: List<Expense>,

    // Actions
    onConfirm: () -> Unit
) {
    val filteredList = list.map { it.category }.distinct()
    // List containing map from Category to total cost
    val combinedList = filteredList.map { category ->
        Pair(
            category,
            list.filter { it.category == category }
                .map { it.cost }
                .reduce { acc, cost -> acc + cost }
        )
    }

    PieChartTemplate(list = combinedList, onConfirm = onConfirm)
}

/**
 * Composable to design pop-up dialog for pie chart
 */
@Composable
private fun PieChartTemplate(
    // Values
    list: List<Pair<String, Double>>,

    // Actions
    onConfirm: () -> Unit
) {
    // Colours to be used for each Category
    val colourList = listOf(
        Color(255, 26, 26),
        Color(51, 51, 255),
        Color(0, 153, 0),
        Color(255, 204, 0),
        Color(204, 0, 153)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        PieChart(
            modifier = Modifier.size(350.dp),
            radius = 350f,
            innerRadius = 175f,
            input = list.mapIndexed { index, pair ->
                PieChartInput(
                    color = colourList[index % 5],
                    value = pair.second,
                    description = pair.first,
                )
            },
            centerText = stringResource(id = R.string.centerText),
            separatorColor = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(50.dp))
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

/**
 * Composable to create pie chart
 */
@Composable
private fun PieChart(
    // Values
    modifier: Modifier = Modifier,
    radius: Float = 500f,
    innerRadius: Float = 250f,
    transparentWidth: Float = 70f,
    input: List<PieChartInput>,
    centerText: String = "",
    separatorColor: Color,

    // ViewModels
    userPrefViewModel: UserPrefRepoViewModel = viewModel(factory = com.billy.glintforce.viewModel.AppViewModelProvider.Factory)
) {
    val userUiState by userPrefViewModel.userPrefRepoUiState.collectAsState()

    var darkTheme by remember { mutableStateOf(false) }

    darkTheme = if (userUiState.userPrefList.isEmpty()) {
        false
    } else {
        userUiState.userPrefList.first().theme
    }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var inputList by remember { mutableStateOf(input) }
    var isCenterTapped by remember { mutableStateOf(false) }

    val theme = darkTheme

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { offset ->
                            val tapAngleInDegrees = (-atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat() - 90f).mod(360f)
                            val centerClicked = if (tapAngleInDegrees < 90) {
                                offset.x < circleCenter.x + innerRadius && offset.y < circleCenter.y + innerRadius
                            } else if (tapAngleInDegrees < 180) {
                                offset.x > circleCenter.x - innerRadius && offset.y < circleCenter.y + innerRadius
                            } else if (tapAngleInDegrees < 270) {
                                offset.x > circleCenter.x - innerRadius && offset.y > circleCenter.y - innerRadius
                            } else {
                                offset.x < circleCenter.x + innerRadius && offset.y > circleCenter.y - innerRadius
                            }

                            if (centerClicked) {
                                inputList = inputList.map {
                                    it.copy(isTapped = !isCenterTapped)
                                }
                                isCenterTapped = !isCenterTapped
                            } else {
                                val anglePerValue = 360f / input
                                    .sumOf {
                                        it.value
                                    }
                                    .toFloat()
                                var currAngle = 0f
                                inputList.forEach { pieChartInput ->

                                    currAngle += pieChartInput.value.toFloat() * anglePerValue
                                    if (tapAngleInDegrees < currAngle) {
                                        val desc = pieChartInput.description
                                        inputList = inputList.map {
                                            if (desc == it.description) {
                                                it.copy(isTapped = !it.isTapped)
                                            } else {
                                                it.copy(isTapped = false)
                                            }
                                        }
                                        return@detectTapGestures
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            val totalValue = input.sumOf {
                it.value
            }
            val anglePerValue = 360f / totalValue
            var currentStartAngle = 0f

            inputList.forEach { pieChartInput ->
                val scale = if (pieChartInput.isTapped) 1.1f else 1.0f
                val angleToDraw = (pieChartInput.value * anglePerValue).toFloat()
                scale(scale) {
                    drawArc(
                        color = pieChartInput.color,
                        startAngle = currentStartAngle,
                        sweepAngle = angleToDraw,
                        useCenter = true,
                        size = Size(
                            width = radius * 2f,
                            height = radius * 2f
                        ),
                        topLeft = Offset(
                            x = (width - radius * 2f) / 2f,
                            y = (height - radius * 2f) / 2f
                        )
                    )
                    currentStartAngle += angleToDraw
                }
                var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f
                var factor = 1f
                if (rotateAngle > 90f) {
                    rotateAngle = (rotateAngle + 180).mod(360f)
                    factor = -0.92f
                }

                val percentage = (pieChartInput.value / totalValue.toFloat() * 100).toInt()
                drawContext.canvas.nativeCanvas.apply {
                    if (percentage > 3) {
                        rotate(degrees = rotateAngle) {
                            drawText(
                                "$percentage %",
                                circleCenter.x,
                                circleCenter.y + (radius - (radius - innerRadius) / 2f) * factor,
                                Paint().apply {
                                    textSize =
                                        if (pieChartInput.isTapped) 18.sp.toPx() else 13.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = Color.White.toArgb()
                                }
                            )
                        }
                    }
                }
                if (pieChartInput.isTapped) {
                    val tabRotation = currentStartAngle - angleToDraw - 90f
                    rotate(tabRotation) {
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(12f, radius * 1.1f),
                            color = separatorColor,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }
                    rotate(tabRotation + angleToDraw) {
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(12f, radius * 1.1f),
                            color = separatorColor,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }
                    rotate(rotateAngle) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "${pieChartInput.description}: S$ ${
                                    String.format(Locale.getDefault(), "%.2f", pieChartInput.value)
                                }",
                                circleCenter.x,
                                circleCenter.y + radius * 1.3f * factor,
                                Paint().apply {
                                    textSize = 12.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = if (theme) Color.Black.toArgb() else Color.White.toArgb()
                                    isFakeBoldText = true
                                }
                            )
                        }
                    }
                }
            }

            if (inputList.first().isTapped) {
                rotate(270f) {
                    drawRoundRect(
                        topLeft = circleCenter,
                        size = Size(12f, radius * 1.1f),
                        color = separatorColor,
                        cornerRadius = CornerRadius(15f, 15f)
                    )
                }
            }
            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    circleCenter.x,
                    circleCenter.y,
                    innerRadius,
                    Paint().apply {
                        color = Color.White.copy(alpha = 0.6f).toArgb()
                        setShadowLayer(10f, 0f, 0f, Color.Gray.toArgb())
                    }
                )
            }

            drawCircle(
                color = Color.White.copy(0.2f),
                radius = innerRadius + transparentWidth / 2f
            )
        }

        Text(
            centerText,
            modifier = Modifier
                .width(Dp(innerRadius / 1.5f))
                .padding(10.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}