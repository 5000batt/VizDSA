package com.kjw.vizdsa

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kjw.vizdsa.feature.array.presentation.ArrayScreen
import com.kjw.vizdsa.feature.array.presentation.ArrayViewModel
import com.kjw.vizdsa.feature.home.DataStructureScreen
import com.kjw.vizdsa.feature.home.HomeScreen
import com.kjw.vizdsa.navigation.Screen

@Composable
fun VizDsaApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 홈 화면
            composable(route = Screen.Home.route) {
                HomeScreen(
                    onNavigateToDataStructure = { navController.navigate(Screen.DataStructure.route) },
                    onNavigateToAlgorithm = { /* 추후 구현 */ }
                )
            }

            // 자료구조 선택 화면
            composable(route = Screen.DataStructure.route) {
                DataStructureScreen(
                    onNavigateToArray = { navController.navigate(Screen.Array.route) }
                )
            }

            // 배열 시각화 화면
            composable(route = Screen.Array.route) {
                val viewModel: ArrayViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                ArrayScreen(
                    uiState = uiState,
                    onSizeChange = viewModel::updateSizeInput,
                    onValueChange = viewModel::updateValueInput,
                    onOperationChange = viewModel::updateOperation,
                    onExecute = viewModel::executeOperation
                )
            }
        }
    }
}