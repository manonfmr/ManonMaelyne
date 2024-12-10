package com.example.chicoutexplore.Screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chicoutexplore.Activity
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.google.firebase.firestore.QuerySnapshot

@Serializable
data class NominatimResponse(
    val lat: String,
    val lon: String
)

// Fonction pour récupérer le plus grand ID parmi les documents de la collection "Activities"
fun getMaxDocumentId(onResult: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    // Requête pour récupérer tous les documents de la collection "Activities"
    db.collection("Activities")
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents: QuerySnapshot? = task.result
                var maxIdInt = Int.MIN_VALUE

                // Parcourir les documents et chercher le plus grand ID
                for (document in documents!!) {
                    val documentId = document.id // ID du document (en tant que chaîne)

                    try {
                        // Convertir l'ID en entier
                        val idNumber = documentId.toInt()

                        // Mettre à jour le maxId si l'ID trouvé est plus grand
                        if (idNumber > maxIdInt) {
                            maxIdInt = idNumber
                        }
                    } catch (e: NumberFormatException) {
                        // Si l'ID n'est pas un nombre, on l'ignore
                        e.printStackTrace()
                    }
                }

                // Retourner le plus grand ID trouvé ou un message d'erreur
                val result = if (maxIdInt != Int.MIN_VALUE) {
                    (maxIdInt+ 1).toString() // Convertir en chaîne
                } else {
                    "0" // Si aucun ID valide trouvé, on commence à partir de "0"
                }

                // Appeler la fonction onResult pour retourner le résultat
                onResult(result)
            } else {
                // Si une erreur se produit lors de la récupération des documents
                onResult("Erreur lors de la récupération des documents.")
            }
        }
}

// Page AddActivityScreen
@Composable
fun AddActivityScreen(navController: NavHostController) {
    var nom by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var prix by remember { mutableStateOf("") }
    var adresse by remember { mutableStateOf("") }
    var avis by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }  // Indicateur de chargement
    var errorMessage by remember { mutableStateOf<String?>(null) } // Message d'erreur
    var maxId by remember { mutableStateOf("0") } // Variable pour stocker le max ID

    val coroutineScope = rememberCoroutineScope()

    // Fonction pour récupérer les coordonnées
    suspend fun getCoordinates(address: String): Pair<Double, Double>? {
        val client = HttpClient(CIO)
        return try {
            val response: String = client.get("https://nominatim.openstreetmap.org/search") {
                parameter("q", "$address,Canada") //Bloquage des adresses au Canada
                parameter("format", "json")
            }.body()

            // Désérialisation en liste et gestion des erreurs
            val json = Json { ignoreUnknownKeys = true }
            val results: List<NominatimResponse> = json.decodeFromString(response)

            val firstResult = results.firstOrNull()
            if (firstResult != null) {
                firstResult.lat.toDouble() to firstResult.lon.toDouble()
            } else {
                null  // Aucun résultat trouvé pour l'adresse
            }
        } catch (e: Exception) {
            println("Erreur lors de la récupération des coordonnées : ${e.message}")
            null
        } finally {
            client.close()
        }
    }

    // Fonction pour créer l'activité dans Firebase
    fun createActivity(
        nom: String,
        description: String,
        location: String,
        prix: String,
        adresse: String,
        avis: List<String>,
        latitude: Double?,
        longitude: Double?,
        id: String // Ajouter le champ id
    ) {
        val prixDouble = prix.toDoubleOrNull()
        if (prixDouble == null || latitude == null || longitude == null) {
            errorMessage = "Le prix doit être un nombre valide et les coordonnées doivent être disponibles."
            return
        }

        val db = FirebaseFirestore.getInstance()

        // Créer un objet Activity avec tous les détails
        val newActivity = Activity(
            nom = nom,
            description = description,
            location = location,
            prix = prixDouble,
            adresse = adresse,
            avis = avis,
            latitude = latitude,
            longitude = longitude,
            id=id
        )

        // Utiliser maxId comme ID du document
        db.collection("Activities")
            .document(id)
            .set(newActivity)
            .addOnSuccessListener {
                isSubmitting = false
                navController.popBackStack()
            }
            .addOnFailureListener { e ->
                isSubmitting = false
                errorMessage = "Erreur lors de la création de l'activité : ${e.message}"
            }
    }

    // Fonction pour valider les champs obligatoires
    fun validateFields(): Boolean {
        return when {
            nom.isBlank() -> {
                errorMessage = "Le nom de l'activité est obligatoire."
                false
            }
            description.isBlank() -> {
                errorMessage = "La description est obligatoire."
                false
            }
            prix.isBlank() -> {
                errorMessage = "Le prix est obligatoire."
                false
            }
            adresse.isBlank() -> {
                errorMessage = "L'adresse est obligatoire."
                false
            }
            else -> true
        }
    }

    // Appel de la fonction pour récupérer le max ID avant de créer une activité
    LaunchedEffect(Unit) {
        getMaxDocumentId { result ->
            maxId = result // Mettre à jour maxId avec le résultat
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Créer une Nouvelle Activité", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom de l'activité * ") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Nom du lieu") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = prix,
            onValueChange = { prix = it },
            label = { Text("Prix *") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Exemple : 25.0") }
        )

        OutlinedTextField(
            value = adresse,
            onValueChange = { adresse = it },
            label = { Text("Adresse (avec la ville) *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = avis,
            onValueChange = { avis = it },
            label = { Text("Avis") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ajoutez un avis (optionnel)") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Latitude : ${latitude ?: "Recherche..."}")
        Text(text = "Longitude : ${longitude ?: "Recherche..."}")

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null  // Réinitialiser l'erreur

                    // Valider les champs obligatoires
                    if (validateFields()) {
                        val coordinates = getCoordinates(adresse)
                        if (coordinates != null) {
                            val (lat, lon) = coordinates
                            latitude = lat
                            longitude = lon

                            createActivity(
                                nom,
                                description,
                                location,
                                prix,
                                adresse,
                                avis.split(", "),
                                latitude,
                                longitude,
                                maxId
                            )
                        } else {
                            errorMessage = "Impossible de récupérer les coordonnées pour l'adresse : $adresse"
                        }
                    }


                    isLoading = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(text = "Créer")
            }
        }
    }
}



