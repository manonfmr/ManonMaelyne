package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.Activity
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.fetchActivityById
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun FeedbackFormScreen(activityId: String,navController: NavHostController) {
    var activity by remember { mutableStateOf<Activity?>(null) }
    // Lance la récupération de données pour l'activité
    LaunchedEffect(activityId) {
        fetchActivityById(activityId) { fetchedActivity ->
            activity = fetchedActivity
        }
    }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Avis :  ${activity?.nom}")
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
            Button(onClick = { navController.navigate("${enumScreen.Activity.name}/${activityId}") }) {
                Text(text = "Valider")
            }
        }


    }
}

@Preview
@Composable
fun FeedbackFormScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme{
        FeedbackFormScreen("1",navController);
    }

}