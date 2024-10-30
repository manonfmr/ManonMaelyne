package com.example.chicoutexplore.Screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.example.chicoutexplore.R

class MapScreen : AppCompatActivity() {

    //@Composable
//fun MapScreen() {
//Column {
//Text(text = "Map")
//Button(onClick = { navController.navigate(enumScreen.Activity.name) }, colors = ButtonDefaults.buttonColors()) {
// Text(text = "Activité 1")
//}
//}
// }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurer l'utilisateur pour osmdroid
        Configuration.getInstance().userAgentValue = packageName

        setContent {
            ChicoutExploreTheme {
                MapScreenContent()
            }
        }
    }

    @Composable
    fun MapScreenContent() {
        AndroidView(factory = { context ->
            MapView(context).apply {
                setMultiTouchControls(true)
            }
        }, update = { mapView ->
            val controller = mapView.controller
            if (controller != null) {
                val startPoint = GeoPoint(48.4197, 71.0661)
                controller.setZoom(15.0)
                controller.setCenter(startPoint)

                // Ajouter un marqueur
                val marker = Marker(mapView)
                marker.position = startPoint
                marker.title = "Saguenay"
                mapView.overlays.add(marker)
            }
        })
    }


    @Preview(showBackground = true)
    @Composable
    fun MapScreenPreview() {
        ChicoutExploreTheme {
            MapScreenContent()
        }
    }
}
