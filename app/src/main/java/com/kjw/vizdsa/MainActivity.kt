package com.kjw.vizdsa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kjw.vizdsa.feature.array.presentation.ArrayScreen
import com.kjw.vizdsa.feature.array.presentation.ArrayViewModel
import com.kjw.vizdsa.feature.home.DataStructureScreen
import com.kjw.vizdsa.feature.home.HomeScreen
import com.kjw.vizdsa.navigation.Screen
import com.kjw.vizdsa.ui.theme.VizDSATheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VizDSATheme {
                VizDsaApp()
            }
        }
    }
}