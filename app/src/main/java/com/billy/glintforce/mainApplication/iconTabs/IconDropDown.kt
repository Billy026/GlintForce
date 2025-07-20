package com.billy.glintforce.mainApplication.iconTabs

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.billy.glintforce.R

/**
 * Composable containing the Icon used to access the Settings Tab
 */
@Composable
fun IconDropDown(
    dropDownItems: List<IconTabs>,
    modifier: Modifier = Modifier,
    onItemClick: (IconTabs) -> Unit
) {
    // Variable for whether the drop down is currently being shown
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    // Variable for the offsetting of composable when Icon is clicked
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    // Variable for the height of the drop down box
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Card(
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
    ) {
        Row(
            modifier = modifier
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        }
                    )
                }
                .padding(5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "Circle account icon",
                modifier = modifier
            )
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            modifier = modifier
        ) {
            dropDownItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(
                        text = stringResource(id = item.title),
                        color = if (item.isLogOut) Color.Red else Color.White
                    ) },
                    onClick = {
                        onItemClick(item)
                        isContextMenuVisible = false
                    }
                )
            }
        }
    }
}