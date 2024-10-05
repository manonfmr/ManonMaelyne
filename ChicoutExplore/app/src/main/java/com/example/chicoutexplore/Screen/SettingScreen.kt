package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun SettingScreen(){
    Column {
        Text(text = "Setting")
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    ChicoutExploreTheme{
        SettingScreen();
    }

}