package com.example.chicoutexplore.Screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.fetchActivities
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapComposable(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Initialise la configuration d'osmdroid avec le contexte
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    // Crée un MapView et initialise FusedLocationProviderClient pour obtenir la position de l'utilisateur

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) } // Pour stocker la position de l'utilisateur

    // Récupère la position de l'utilisateur
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLocation = GeoPoint(it.latitude, it.longitude)
                }
            }
        }
    }

    // Composable AndroidView pour afficher la carte
    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx)
            mapView.setMultiTouchControls(true)

            // Centre la carte sur Chicoutimi par défaut
            val chicoutimi = GeoPoint(48.4275, -71.0557)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(chicoutimi)

            // Si la position de l'utilisateur est récupérée, centre la carte sur sa position
            userLocation?.let {
                mapView.controller.setCenter(it)

                // Ajouter un marqueur pour la position de l'utilisateur
                val userMarker = Marker(mapView)
                userMarker.position = it
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                userMarker.title = "Votre position"
                mapView.overlays.add(userMarker)
            }

            // Récupère les activités depuis Firestore et ajoute les marqueurs
            fetchActivities { activities ->
                for (activity in activities) {
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(activity.latitude, activity.longitude)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = activity.nom

                    // Rediriger vers une autre page en cliquant sur le marqueur
                    marker.setOnMarkerClickListener { _, _ ->
                        navController.navigate("${enumScreen.Activity.name}/${activity.id}")
                        true
                    }

                    mapView.overlays.add(marker)
                }
            }
            mapView
        },
        modifier = modifier
    )
}
