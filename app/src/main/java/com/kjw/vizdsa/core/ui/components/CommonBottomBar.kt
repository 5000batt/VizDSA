package com.kjw.vizdsa.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ActionButton(
    val text: String,
    val isPrimary:  Boolean,
    val weight: Float = 1f,
    val onClick: () -> Unit
)

@Composable
fun CommonBottomBar(
    buttons: List<ActionButton>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        buttons.forEach {
            if (it.isPrimary) {
                Button(
                    onClick = it.onClick,
                    modifier = Modifier
                        .weight(it.weight)
                        .height(56.dp)
                ) {
                    Text(it.text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                OutlinedButton(
                    onClick = it.onClick,
                    modifier = Modifier
                        .weight(it.weight)
                        .height(56.dp)
                ) {
                    Text(it.text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}