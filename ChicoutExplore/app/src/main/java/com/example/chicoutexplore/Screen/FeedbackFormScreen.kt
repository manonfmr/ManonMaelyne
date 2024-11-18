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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun FeedbackFormScreen(activityId: String,navController: NavHostController) {

    var activity by remember { mutableStateOf<Activity?>(null) }
    var commentaire by remember { mutableStateOf("") } // État pour le champ de texte
    var prix by remember { mutableStateOf("") } // État pour le champ de texte du prix
    var photos by remember { mutableStateOf(listOf<String>()) } // Liste d'URLs de photos
    var errorMessage by remember { mutableStateOf("") }  // Message d'erreur pour la validation
    var isUpdating by remember { mutableStateOf(false) } // Indicateur d'état pour l'opération de mise à jour


    // Fonction pour mettre à jour l'activité dans la base de données
    fun updateActivity(activityId: String, prix: String, commentaire: String, photos: List<String>,onSuccess: () -> Unit) {
        // Référence à la base de données Firestore
        val db = FirebaseFirestore.getInstance()

        // Convertir prix en Double
        val prixDouble = prix.toDoubleOrNull()

        if (prixDouble == null) {
            errorMessage = "Veuillez entrer un prix valide."
            return
        }

        // Récupérer le document pour accéder à la liste actuelle des avis
        db.collection("Activities").document(activityId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Récupérer la liste des avis actuelle, ou créer une nouvelle liste si elle n'existe pas
                    val avisActuels = documentSnapshot.get("avis") as? List<String> ?: listOf()

                    // Créer une nouvelle liste d'avis avec le nouveau commentaire ajouté
                    val avisMisAJour = avisActuels + commentaire

                    // Préparer les données à mettre à jour
                    val activityUpdates = hashMapOf<String, Any>(
                        "prix" to prixDouble, // Mise à jour du prix
                        "avis" to avisMisAJour // Ajout du commentaire à la liste des avis
                    )

                    db.collection("Activities").document(activityId)
                        .set(activityUpdates, SetOptions.merge())
                        .addOnSuccessListener {
                            onSuccess()
                            // Afficher un message ou faire une action lorsque l'update est réussi
                            println("Activité mise à jour avec succès")
                        }
                        .addOnFailureListener { e ->
                            // Afficher un message d'erreur si l'update échoue
                            println("Erreur lors de la mise à jour de l'activité: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Erreur lors de la récupération de l'activité : ${e.message}")
            }
    }

    // Lance la récupération de données pour l'activité
    LaunchedEffect(activityId) {
        fetchActivityById(activityId) { fetchedActivity ->
            activity = fetchedActivity
            prix = activity?.prix?.toString() ?: ""
        }
    }

    // Vérification en temps réel de la saisie du prix
    fun validatePrix(input: String) {
        val prixDouble = input.toDoubleOrNull()
        errorMessage = if (prixDouble == null || prixDouble <= 0) {
            "Veuillez entrer un prix valide (supérieur à 0)."
        } else {
            ""
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
                text = "Commentaire :",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            )

            // Champ de texte pour les commentaires
            TextField(
                value = commentaire,
                onValueChange = { commentaire = it },
                placeholder = { Text("Écrivez votre commentaire ici...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
                singleLine = false, // Pour permettre des textes sur plusieurs lignes
                maxLines = 5 // Limiter à 5 lignes (modifiable selon le besoin)
            )

            // Prix
            Text(
                text = "Prix :",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_medium)) // Espacement en bas
            )

            // Champ de texte pour le prix
            TextField(
                value = prix,
                onValueChange = { prix = it
                    validatePrix(it)},
                placeholder = { Text("Entrez le prix") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
                singleLine = true
            )
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)  // Espacement sous l'erreur
                )
            }

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
                    onClick = {
                        isUpdating = true // Définir l'état de mise à jour
                        updateActivity(activityId, prix, commentaire, emptyList()) {
                            isUpdating = false // Fin de l'état de mise à jour
                            navController.navigate("${enumScreen.Activity.name}/${activityId}")
                        }
                    },
                    enabled = !isUpdating, // Désactiver le bouton pendant la mise à jour
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
