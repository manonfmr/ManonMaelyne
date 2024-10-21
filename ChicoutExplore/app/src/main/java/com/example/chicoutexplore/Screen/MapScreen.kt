package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun MapScreen(navController: NavHostController) {
    Column {
        Text(text = "Map")
        Button(onClick = { navController.navigate(enumScreen.Activity.name) }, colors = ButtonDefaults.buttonColors()) {
            Text(text = "Activité 1")
        }
    }
}

@Preview
@Composable
fun MapScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme{
        MapScreen(navController);
    }

}