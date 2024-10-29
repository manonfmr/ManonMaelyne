package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.Activity
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.fetchActivities
import com.example.chicoutexplore.fetchActivityById
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun ActivityScreen(activityId: String,navController: NavHostController) {
    var activity by remember { mutableStateOf<Activity?>(null) }
    // Lance la récupération de données pour l'activité
    LaunchedEffect(activityId) {
        fetchActivityById(activityId) { fetchedActivity ->
            activity = fetchedActivity
        }
    }
    Column(Modifier.fillMaxWidth()){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${activity?.nom}")
            Button(onClick = { navController.navigate("${enumScreen.feedbackForm.name}/${activityId}") }, colors = ButtonDefaults.buttonColors()) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Avis",
                )
            }
        }
        if (activity != null) {
            Text(text = "Description de l'activité : ${activity?.description} ")
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Prix : ${activity?.prix}")
                Text(text = "Location de l'activité : ${activity?.location}")
            }
            Row {
                Text(text = "Photos :")
            }
            Row {
                Text(text = "Avis :")
            }
        } else {
            Text(text = "Chargement en cours...")
        }



    }
}

@Preview
@Composable
fun ActivityScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme{
        ActivityScreen("1",navController);
    }

}