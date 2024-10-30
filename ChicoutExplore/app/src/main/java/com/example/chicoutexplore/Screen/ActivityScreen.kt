package com.example.chicoutexplore.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chicoutexplore.R
import com.example.chicoutexplore.enumScreen
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun ActivityScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .background(Color(0xFFF3F4F6)),
        verticalArrangement = Arrangement.Top
    ) {
        // Titre de l'activité
        Text(
            text = stringResource(R.string.activity_screen_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small)) // Espacement en bas
        )

        // Bouton pour naviguer vers le formulaire de feedback
        Button(
            onClick = { navController.navigate(enumScreen.feedbackForm.name) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small)),
            modifier = Modifier
                .fillMaxWidth() // Remplir la largeur disponible
                .padding(bottom = dimensionResource(id = R.dimen.padding_small)) // Espacement en bas
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_icon_description)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'icône et le texte (optionnel)
            Text(text = "Modifier") // Ajout d'un texte au bouton pour plus de clarté
        }

        // Description de l'activité
        Text(
            text = "Description de l'activité : ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )

        // Informations sur le prix
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Prix :", style = MaterialTheme.typography.bodyMedium)
        }

        // Espacement entre le prix et la location
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        // Informations sur la location de l'activité
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Location de l'activité :", style = MaterialTheme.typography.bodyMedium)
        }

        // Espacement entre les lignes
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_height)))

        // Photos de l'activité
        Text(
            text = "Photos :",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )

        // Avis sur l'activité
        Text(
            text = "Avis :",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview(navController: NavHostController = rememberNavController()) {
    ChicoutExploreTheme {
        ActivityScreen(navController)
    }
}
