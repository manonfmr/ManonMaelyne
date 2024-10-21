package com.example.chicoutexplore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.Screen.ActivityScreen
import com.example.chicoutexplore.Screen.FeedbackFormScreen
import com.example.chicoutexplore.Screen.MapScreen
import com.example.chicoutexplore.Screen.SearchResultScreen
import com.example.chicoutexplore.Screen.SettingScreen
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChicoutExploreTheme {
                ChicoutExploreApp()
            }
        }
    }
}

enum class enumScreen(){
    Map,
    Activity,
    SearchResult,
    Setting,
    feedbackForm
}


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
                MapScreen(navController)
            }
            composable(route = enumScreen.Activity.name) {
                //Appel de l'écran Activité
                ActivityScreen(navController)
            }
            composable(route = enumScreen.SearchResult.name) {
                //Appel de l'écran resultat de recherche
                SearchResultScreen()
            }
            composable(route = enumScreen.Setting.name) {
                //Appel de l'écran Paramètre
                SettingScreen()
            }
            composable(route = enumScreen.feedbackForm.name) {
                //Appel de l'écran formulaire avis
                FeedbackFormScreen(navController)
            }



        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChicoutExploreTheme {
        ChicoutExploreApp()
    }
}