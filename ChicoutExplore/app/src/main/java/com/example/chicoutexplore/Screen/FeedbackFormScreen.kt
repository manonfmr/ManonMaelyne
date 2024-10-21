package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun FeedbackFormScreen(navController: NavHostController) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Avis : " + stringResource(R.string.activity_screen_name))
        }
        Text(text = "Commentaire / Description :")
        Text(text = "Prix : ")
        Row {
            Text(text = "Photos : ")
            Button(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add photos",
                )
            }
        }
        Row(horizontalArrangement = Arrangement.End){
            Button(onClick = { navController.navigate(enumScreen.Activity.name) }) {
                Text(text = "Valider")
            }
        }


    }
}

@Preview
@Composable
fun FeedbackFormScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme{
        FeedbackFormScreen(navController);
    }

}