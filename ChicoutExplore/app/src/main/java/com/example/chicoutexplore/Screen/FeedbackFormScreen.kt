package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun FeedbackFormScreen(){
    Column {
        Text(text = "Feedback Form")
    }
}

@Preview
@Composable
fun FeedbackFormScreenPreview() {
    ChicoutExploreTheme{
        FeedbackFormScreen();
    }

}