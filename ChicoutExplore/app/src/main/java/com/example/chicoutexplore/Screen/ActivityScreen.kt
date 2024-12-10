package com.example.chicoutexplore.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.chicoutexplore.Activity
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.fetchActivityById
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun ActivityScreen(activityId: String, navController: NavHostController) {
    var activity by remember { mutableStateOf<Activity?>(null) }
    var showDialog by remember { mutableStateOf(false) } // État pour contrôler la fenêtre modale
    var selectedImageUrl by remember { mutableStateOf("") } // URL de l'image sélectionnée

    // Détecte les changements dans le NavBackStackEntry pour recharger les données lorsque l'utilisateur revient sur la page
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Charger les données dès que l'on arrive sur la page ou si le retour est déclenché
    LaunchedEffect(navBackStackEntry) {
        fetchActivityById(activityId) { fetchedActivity ->
            activity = fetchedActivity
        }
    }

    if (activity != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            // Contenu de l'activité
            Column(modifier = Modifier.fillMaxSize()) {
                // Titre et bouton de retour dans un Row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Titre de l'activité
                    Text(
                        text = "${activity?.nom}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f) // Prendre tout l'espace à gauche
                    )

                    // Bouton flottant de retour à droite
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp) // Espacement du bouton
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Bouton de retour"
                        )
                    }
                }

                // Bouton pour naviguer vers le formulaire de feedback
                Button(
                    onClick = { navController.navigate("${enumScreen.feedbackForm.name}/${activityId}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_icon_description)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Compléter l'activité")
                }

                // Description de l'activité
                Text(
                    text = "Description de l'activité : ${activity?.description} ",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium))
                )

                // Informations sur le prix
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Prix : ${activity?.prix} $ CAD", style = MaterialTheme.typography.bodyMedium)
                }

                // Espacement entre le prix et la location
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

                // Informations sur la location de l'activité
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Location de l'activité : ${activity?.location} \n ${activity?.adresse}", style = MaterialTheme.typography.bodyMedium)
                }

                // Espacement entre les lignes
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_height)))

                // Photos de l'activité
                Text(
                    text = "Photos :",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
                )

                if (activity?.urlsPhoto.isNullOrEmpty()) {
                    Text(
                        text = "Aucune photo disponible.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
                    ) {
                        items(activity!!.urlsPhoto.size) { index ->
                            ImageCard(imageUrl = activity!!.urlsPhoto[index], onImageClick = {
                                selectedImageUrl = activity!!.urlsPhoto[index] // Met à jour l'URL de l'image sélectionnée
                                showDialog = true // Afficher la boîte de dialogue
                            })
                        }
                    }
                }

                // Avis sur l'activité
                Text(
                    text = "Avis :",
                    style = MaterialTheme.typography.bodyMedium
                )

                Column {
                    if (!activity?.avis.isNullOrEmpty()) {
                        for (avis in activity!!.avis) {
                            Text(text = avis)
                        }
                    }else {
                        Text(text = "Aucun avis disponible.", color = Color.Gray)
                    }
                }
            }
        }
        // Dialog pour afficher l'image en grand
        if (showDialog) {
            ImageDialog(imageUrl = selectedImageUrl, onDismiss = { showDialog = false })        }
    } else {
        Text(text = "Chargement en cours...")
    }
}

@Composable
fun ImageCard(imageUrl: String, onImageClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(150.dp) // Taille ajustable des images
            .clickable { onImageClick() } // Action lors du clic
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Boîte de dialogue modale pour afficher l'image en grand
@Composable
fun ImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() } // Fermer la boîte de dialogue en cliquant sur l'arrière-plan
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Fit, // Adapter l'image à l'écran
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme {
        ActivityScreen("1", navController)
    }
}
