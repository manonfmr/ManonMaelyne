package com.example.chicoutexplore

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.Screen.ActivityScreen
import com.example.chicoutexplore.Screen.AddActivityScreen
import com.example.chicoutexplore.Screen.FeedbackFormScreen
import com.example.chicoutexplore.Screen.MapComposable
import com.example.chicoutexplore.Screen.SearchResultScreen
import com.example.chicoutexplore.Screen.SettingScreen
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import android.Manifest

class MainActivity : ComponentActivity() {
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Active les bords à bords (edge-to-edge)
        enableEdgeToEdge()

        // Initialisation du locationPermissionLauncher
        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.i("Permissions", "Permission de localisation accordée")
            } else {
                Log.w("Permissions", "Permission de localisation refusée")
            }
        }
        // Vérifiez si la permission est déjà accordée
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        setContent {
            // Obtenir le contrôleur System UI
            val systemUiController = rememberSystemUiController()
            val isDarkTheme = isSystemInDarkTheme() // Vérifie si le mode sombre est actif


            LaunchedEffect(isDarkTheme) {
                // Configurer les couleurs et les icônes en fonction du thème
                systemUiController.setStatusBarColor(
                    color = Color.Transparent, // Fond transparent
                    darkIcons = !isDarkTheme // Icônes sombres en mode clair, icônes claires en mode sombre
                )
                systemUiController.isNavigationBarVisible = false // Cache la barre de navigation
            }

            // Appliquer le thème et lancer l'application
            ChicoutExploreTheme {
                ChicoutExploreApp()
            }
        }

    }
}



//Enumération des différentes pages
enum class enumScreen(){
    Map,
    Activity,
    SearchResult,
    Setting,
    feedbackForm,
    AddActivity
}

//Function qui contient le Header et footer et réalise la navigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChicoutExploreApp(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Image(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.image_size))
                            .padding(dimensionResource(id = R.dimen.padding_small)),
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                }
            })
        },
        bottomBar = {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)){
                Button(onClick = { navController.navigate(enumScreen.Map.name)},colors = if (currentRoute == enumScreen.Map.name) ButtonDefaults.buttonColors(Color.Blue)
                else ButtonDefaults.buttonColors(), modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),shape = RoundedCornerShape(0.dp)) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Carte",
                        tint = if (currentRoute == enumScreen.Map.name) Color.White else Color.Gray,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                    )
                }
                Button(onClick = { navController.navigate(enumScreen.SearchResult.name) }, colors = if (currentRoute == enumScreen.SearchResult.name) ButtonDefaults.buttonColors(Color.Blue) else ButtonDefaults.buttonColors(),modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),shape = RoundedCornerShape(0.dp),) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Recherche",
                        tint = if (currentRoute == enumScreen.SearchResult.name) Color.White else Color.Gray,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                    )
                }
                Button(onClick = { navController.navigate(enumScreen.Setting.name)}, colors = if (currentRoute == enumScreen.Setting.name) ButtonDefaults.buttonColors(Color.Blue) else ButtonDefaults.buttonColors(),modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),shape = RoundedCornerShape(0.dp), ) {
                    Icon(imageVector = Icons.Default.Settings,
                        contentDescription = "Parametre",
                        tint = if (currentRoute == enumScreen.Setting.name) Color.White else Color.Gray,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size)))
                }
            }
        }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = enumScreen.Map.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = enumScreen.Map.name) {
                //Appel de l'écran map
                MapComposable(context = LocalContext.current,navController)
            }
            composable(route = "${enumScreen.Activity.name}/{activityId}") {
                //Appel à l'écran de l'activité en récupérant son id
                backStackEntry ->
                val activityId = backStackEntry.arguments?.getString("activityId") ?: "Unknown"
                ActivityScreen(activityId, navController)
            }
            composable(route = enumScreen.SearchResult.name) {
                //Appel de l'écran resultat de recherche
                SearchResultScreen(navController)
            }
            composable(route = enumScreen.Setting.name) {
                //Appel de l'écran Paramètre
                SettingScreen()
            }
            composable(route = "${enumScreen.feedbackForm.name}/{activityId}") {backStackEntry ->
                //Appel de l'écran formulaire avis
                val activityId = backStackEntry.arguments?.getString("activityId") ?: "Unknown"
                FeedbackFormScreen(activityId,navController)
            }
            composable(route = enumScreen.AddActivity.name) {
                // Appel à l'écran de l'ajout d'activité
                AddActivityScreen(navController)
            }



        }
    }
}

/** Recupération des donnée de la base
 * en liste
 */
fun fetchActivities(callback: (List<Activity>) -> Unit) {
    val db = Firebase.firestore
    db.collection("Activities")
        .get()
        .addOnSuccessListener { result ->
            val activities = result.documents.mapNotNull { document ->
                document.toObject(Activity::class.java)
            }
            callback(activities)
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error fetching activities", exception)
            callback(emptyList())
        }
}
/** Recupération des donnée de la base
 * en  fonction de l'id de l'activité
 */
fun fetchActivityById(activityId: String, onComplete: (Activity?) -> Unit) {
    val db = Firebase.firestore
    db.collection("Activities")
        .document(activityId) // Récupère le document avec l'ID correspondant
        .get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val activity = document.toObject(Activity::class.java)
                onComplete(activity) // Passe l'activité récupérée à la fonction onComplete
            } else {
                onComplete(null) // Aucun document trouvé
            }
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
            onComplete(null) // Erreur lors de la récupération des données
        }
}

/** modifiaction du chemin des photos**/
fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        it.moveToFirst()
        val index = it.getColumnIndex(MediaStore.Images.Media.DATA)
        it.getString(index)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChicoutExploreTheme {
        ChicoutExploreApp()
    }
}

