package com.example.chicoutexplore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChicoutExploreApp() {
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
                .fillMaxWidth().height(120.dp)){
                Button(onClick = { /*TODO*/ }, colors =  ButtonDefaults.buttonColors(), modifier = Modifier
                    .weight(1f).fillMaxHeight(),shape = RoundedCornerShape(0.dp),) {
                    Icon(
                        imageVector = Icons.Default.Search, //Changer icon pour activité
                        contentDescription = "Activité",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                    )
                }
                Button(onClick = { /*TODO*/ }, colors =  ButtonDefaults.buttonColors(),modifier = Modifier
                    .weight(1f).fillMaxHeight(),shape = RoundedCornerShape(0.dp),) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Recherche",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                    )
                }
                Button(onClick = { /*TODO*/ }, colors =  ButtonDefaults.buttonColors(),modifier = Modifier
                    .weight(1f).fillMaxHeight(),shape = RoundedCornerShape(0.dp), ) {
                    Icon(imageVector = Icons.Default.Settings,
                        contentDescription = "Parametre",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size)))
                }
            }
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Carte") //Contenu de la page
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