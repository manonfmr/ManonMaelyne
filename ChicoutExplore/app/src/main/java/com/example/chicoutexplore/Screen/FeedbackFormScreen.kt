package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.Activity
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.fetchActivityById
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun FeedbackFormScreen(activityId: String,navController: NavHostController) {
    println(activityId)
    var activity by remember { mutableStateOf<Activity?>(null) }
    // Lance la récupération de données pour l'activité
    LaunchedEffect(activityId) {
        fetchActivityById(activityId) { fetchedActivity ->
            activity = fetchedActivity
        }
    }

    if (activity != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.Top
        ) {
            // Titre de l'écran de feedback
            Text(
                text = "Avis : ${activity?.nom}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            )

            // Commentaire / Description
            Text(
                text = "Commentaire / Description :",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            )

            // Prix
            Text(
                text = "Prix :",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            )

            // Photos
            Text(
                text = "Photos :",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            )

            // Bouton pour ajouter des photos (placé sous le texte "Photos :")
            Button(
                onClick = { /*TODO: Ajouter une fonction pour ajouter des photos*/ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small)),
                modifier = Modifier
                    .fillMaxWidth() // Remplir la largeur disponible
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_photos_description)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte (optionnel)
                Text(text = "Ajouter Photo") // Ajout d'un texte au bouton pour plus de clarté
            }

            // Bouton de validation
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_medium)) // Espacement en haut
            ) {
                Button(
                    onClick = { navController.navigate("${enumScreen.Activity.name}/${activityId}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small))
                ) {
                    Text(text = "Valider")
                }
            }
        }
    }else {
        Text(text = "Chargement en cours...")
    }

}

@Preview(showBackground = true)
@Composable
fun FeedbackFormScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme{
        FeedbackFormScreen("1",navController);
    }
}
