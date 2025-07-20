package com.billy.glintforce.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.billy.glintforce.R

// Font Families to be used
val RobotoCondensedMedium = FontFamily(
    Font(R.font.robotocondensed_medium)
)
val OpenSansRegular = FontFamily(
    Font(R.font.opensans_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = RobotoCondensedMedium,
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = OpenSansRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OpenSansRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = OpenSansRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 4.sp,
        letterSpacing = 0.sp
    )
)

/** Common **/

val Typography.textFieldContent: TextStyle
    get() = TextStyle(

    )

val button: TextStyle
    get() = TextStyle(

    )

/** Login Pages **/

val loginHeading: TextStyle
    get() = TextStyle(
        fontFamily = OpenSansRegular,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp
    )

val Typography.textButtons: TextStyle
    get() = TextStyle(

    )