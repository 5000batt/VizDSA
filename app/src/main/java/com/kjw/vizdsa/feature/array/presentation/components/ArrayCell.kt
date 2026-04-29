package com.kjw.vizdsa.feature.array.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ArrayCell(
    index: Int,                     // 아래에 표시될 인덱스 번호
    value: Int?,                    // 박스 안에 들어갈 실제 값
    isHighlighted: Boolean,         // 포커스 여부
    modifier: Modifier = Modifier
) {
    val boxColor = if (isHighlighted) Color.Yellow else Color.White
    val borderColor = if (isHighlighted) Color.Red else Color.Black

    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = boxColor)
                .border(1.dp, borderColor),
            contentAlignment = Alignment.Center
        ) {
            if (value == null) Text("") else Text(text = value.toString())
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = index.toString()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArrayCellPreview() {
    Row {
        // 값이 있는 일반 칸
        ArrayCell(index = 0, value = 15, isHighlighted = false)
        // 값이 없는 일반 칸
        ArrayCell(index = 1, value = null, isHighlighted = false)
        // 값이 있으면서 하이라이트된 칸
        ArrayCell(index = 2, value = 42, isHighlighted = true)
    }
}