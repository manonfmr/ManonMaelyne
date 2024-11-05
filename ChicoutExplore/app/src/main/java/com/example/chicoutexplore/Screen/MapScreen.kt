import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import org.osmdroid.config.Configuration
import android.content.Context

class MapScreen : AppCompatActivity() {
    private var mapView: MapView? = null

    @Composable
    fun navigation(navController: NavHostController) {
        Column {
            Text(text = "Map")
            Button(
                onClick = { navController.navigate(enumScreen.Activity.name) },
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(text = "Activité 1")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation d'OsmDroid
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmPrefs", Context.MODE_PRIVATE))

        setContentView(R.layout.activity_map)

        // Initialisation de la carte
        mapView = findViewById<MapView>(R.id.mapView)

        mapView?.let { map ->
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setBuiltInZoomControls(true)
            map.setMultiTouchControls(true)

            val mapController = map.controller
            mapController.setZoom(15.0)
            mapController.setCenter(GeoPoint(48.8583, 2.2944))
        }
    }

    public override fun onResume() {
        super.onResume()
        mapView?.onResume() // Utiliser `?.` pour éviter les accès forcés
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause() // Utiliser `?.` pour éviter les accès forcés
    }
}
