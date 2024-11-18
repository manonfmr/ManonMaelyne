package com.example.chicoutexplore.Screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.chicoutexplore.Activity
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.fetchActivities
import com.example.chicoutexplore.fetchActivityById
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapComposable(context: Context, navController: NavHostController, modifier: Modifier = Modifier) {

    // Initialise la configuration d'osmdroid avec le contexte
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                // Crée un MapView d'osmdroid
                val mapView = MapView(ctx)
                mapView.setMultiTouchControls(true) // Active le zoom par pincement

                // Centre la carte sur Chicoutimi
                val chicoutimi = GeoPoint(48.4275, -71.0557)
                mapView.controller.setZoom(15.0)
                mapView.controller.setCenter(chicoutimi)

                // Récupère les activités depuis Firestore et ajoute les marqueurs
                fetchActivities { activities ->
                    println(activities)
                    for (activity in activities) {
                        // Créer un marqueur pour chaque activité
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(activity.latitude, activity.longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = activity.nom

                        // Rediriger vers une autre page en cliquant sur le marqueur
                        marker.setOnMarkerClickListener { _, _ ->
                            navController.navigate("${enumScreen.Activity.name}/${activity.id}")
                            true // On indique que l'événement est géré
                        }

                        // Ajouter le marqueur sur la carte
                        mapView.overlays.add(marker)
                    }
                }


                mapView
            },
            modifier = modifier
        )

        // Bouton flottant en bas à droite de la carte
        FloatingActionButton(
            onClick = {
                navController.navigate(enumScreen.AddActivity.name)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "ajouter une activité"
                )
            }
        )
    }

}
