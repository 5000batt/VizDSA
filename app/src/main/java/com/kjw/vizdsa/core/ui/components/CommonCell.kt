package com.kjw.vizdsa.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonCell(
    index: Int,                     // 아래에 표시될 인덱스 번호
    value: Int?,                    // 박스 안에 들어갈 실제 값
    isHighlighted: Boolean,         // 포커스 여부
    modifier: Modifier = Modifier,
    boxRatio: Float? = 1f           // 셀의 가로 세로 비율
) {
    val boxColor = when {
        isHighlighted -> Color(0xFFFFA726)
        value == null -> Color(0xFFE0E0E0)
        else -> MaterialTheme.colorScheme.primary
    }
    val textColor = if (value == null) Color.Transparent else Color.White

    Column(
        modifier = modifier.padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (boxRatio != null) Modifier.aspectRatio(boxRatio) else Modifier)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(color = boxColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value?.toString() ?: "",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = index.toString(),
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, widthDp = 250)
@Composable
fun ArrayCellPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 60.dp),
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // 값이 있는 일반 칸
                CommonCell(index = 0, value = 15, isHighlighted = false)
            }
            item {
                // 값이 있으면서 하이라이트된 칸
                CommonCell(index = 1, value = 42, isHighlighted = true)
            }
            item {
                // 값이 있는 일반 칸
                CommonCell(index = 2, value = 35, isHighlighted = false)
            }
            item {
                // 값이 있는 일반 칸
                CommonCell(index = 3, value = 65, isHighlighted = false)
            }
            item {
                // 값이 없는 일반 칸
                CommonCell(index = 4, value = null, isHighlighted = false)
            }
        }
    }
}