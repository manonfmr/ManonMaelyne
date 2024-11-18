package com.example.chicoutexplore.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.Activity
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SearchResultScreen(navController: NavHostController) {
    var query by remember { mutableStateOf(TextFieldValue("")) } // Barre de recherche
    var activities by remember { mutableStateOf<List<Activity>>(emptyList()) } // Résultats
    var isLoading by remember { mutableStateOf(false) } // Indicateur de chargement
    var errorMessage by remember { mutableStateOf<String?>(null) } // Gestion des erreurs
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barre de recherche
        SearchBar(
            query = query.text,
            onQueryChanged = {
                query = TextFieldValue(it)
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    searchActivities(
                        searchTerm = it,
                        onResult = { result ->
                            activities = result
                            isLoading = false
                        },
                        onError = { error ->
                            errorMessage = error.message
                            isLoading = false
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contenu principal : Résultats ou messages d'état
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage != null -> {
                Text(
                    text = "Erreur : ${errorMessage ?: "Impossible de charger les activités"}",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            activities.isEmpty() -> {
                Text(
                    text = "Aucune activité trouvée",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(activities) { activity ->
                        ActivityItem(activity,onClick = { clickedActivity ->
                            // Navigation vers l'écran des détails
                            navController.navigate("${enumScreen.Activity.name}/${clickedActivity.id}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(Color.LightGray, MaterialTheme.shapes.medium)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.DarkGray,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
            )
        }
    }
}

@Composable
fun ActivityItem(activity: Activity,onClick: (Activity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        .clickable { onClick(activity) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = activity.nom, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = activity.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Prix : ${activity.prix} €", style = MaterialTheme.typography.bodySmall)
        }
    }
}

suspend fun searchActivities(
    searchTerm: String,
    onResult: (List<Activity>) -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val activitiesCollection = db.collection("Activities")

    try {
        // Récupération des documents de Firestore
        val querySnapshot = activitiesCollection.get().await()
        val matchedActivities = mutableListOf<Activity>()

        // Filtrage local
        for (document in querySnapshot.documents) {
            val activity = document.toObject(Activity::class.java)
            if (activity != null) {
                if (searchTerm in activity.nom ||
                    searchTerm in activity.description ||
                    searchTerm in activity.location ||
                    searchTerm in activity.adresse ||
                    activity.avis.any { it.contains(searchTerm, ignoreCase = true) }) {
                    matchedActivities.add(activity)
                }
            }
        }
        onResult(matchedActivities)
    } catch (e: Exception) {
        onError(e)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme {
        SearchResultScreen(navController)
    }
}
