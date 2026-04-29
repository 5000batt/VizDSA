package com.kjw.vizdsa.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DataStructureScreen(
    onNavigateToArray: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("자료구조 선택", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavigateToArray, modifier = Modifier.fillMaxWidth(0.6f)) {
            Text("배열 (Array)")
        }
        // 추후 스택, 큐 등 추가 예
    }
}