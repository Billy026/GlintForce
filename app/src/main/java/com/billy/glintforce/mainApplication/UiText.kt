package com.billy.glintforce.mainApplication

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value : String): com.billy.glintforce.mainApplication.UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): com.billy.glintforce.mainApplication.UiText()

    @Composable
    fun asString(): String {
        return when(this) {
            is com.billy.glintforce.mainApplication.UiText.DynamicString -> value
            is com.billy.glintforce.mainApplication.UiText.StringResource -> stringResource(id = resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is com.billy.glintforce.mainApplication.UiText.DynamicString -> value
            is com.billy.glintforce.mainApplication.UiText.StringResource -> context.getString(resId, *args)
        }
    }
}