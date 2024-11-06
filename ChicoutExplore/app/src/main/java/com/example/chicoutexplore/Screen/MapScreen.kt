package com.example.chicoutexplore.Screen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.chicoutexplore.enumScreen
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapComposable(context: Context, navController: NavHostController, modifier: Modifier = Modifier) {
    // Initialise la configuration d'osmdroid avec le contexte
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    AndroidView(
        factory = { ctx ->
            // Crée un MapView d'osmdroid
            val mapView = MapView(ctx)
            mapView.setMultiTouchControls(true) // Active le zoom par pincement

            // Centre la carte sur Chicoutimi
            val chicoutimi = GeoPoint(48.4275, -71.0557)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(chicoutimi)

            // Ajouter un marqueur exemple sur Chicoutimi
            val marker = Marker(mapView)
            marker.position = GeoPoint(48.4015,-71.0456)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Parc de la Riviere"

            marker.setOnMarkerClickListener { _, _ ->
                // Naviguer vers une autre page
                navController.navigate("${enumScreen.Activity.name}/1")
                true  // Renvoie true pour indiquer que l'événement est géré
            }

            mapView.overlays.add(marker)

            mapView
        },
        modifier = modifier
    )
}
