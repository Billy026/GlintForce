package com.billy.glintforce.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(25.dp),
    medium = CutCornerShape(topEnd = 16.dp, bottomStart = 16.dp)
)