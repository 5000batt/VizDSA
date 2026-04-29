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
fun HomeScreen(
    onNavigateToDataStructure: () -> Unit,
    onNavigateToAlgorithm: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("VizDSA", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavigateToDataStructure, modifier = Modifier.fillMaxWidth(0.6f)) {
            Text("자료구조 (Data Structure)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToAlgorithm, modifier = Modifier.fillMaxWidth(0.6f)) {
            Text("알고리즘 (Algorithm)")
        }
    }
}