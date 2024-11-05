package com.example.chicoutexplore.Screen

import android.content.Context // Importation correcte pour Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chicoutexplore.R
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingScreen() {
    // État pour suivre la langue actuelle
    var isEnglish by remember { mutableStateOf(false) }

    // Obtenir le contexte local pour afficher le toast
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Titre dynamique en fonction de la langue
        Text(
            text = if (isEnglish) "Settings" else "Paramètres",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Options de paramètres
        SettingOption(text = if (isEnglish) "Notifications" else "Notifications") {
            showToast(context, if (isEnglish) "Notifications clicked" else "Notifications cliqué")
        }

        SettingOption(text = if (isEnglish) "Theme" else "Thème") {
            showToast(context, if (isEnglish) "Theme clicked" else "Thème cliqué")
        }

        SettingOption(text = if (isEnglish) "Language" else "Langue") {
            // Changer l'état de la langue
            isEnglish = !isEnglish
            showToast(context, if (isEnglish) "Language switched to English" else "Langue changée en Français")
        }

        SettingOption(text = if (isEnglish) "Privacy" else "Confidentialité") {
            showToast(context, if (isEnglish) "Privacy clicked" else "Confidentialité cliqué")
        }

        SettingOption(text = if (isEnglish) "About" else "À propos") {
            showToast(context, if (isEnglish) "About clicked" else "À propos cliqué")
        }
    }
}

@Composable
fun SettingOption(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp)
    )
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    ChicoutExploreTheme {
        SettingScreen()
    }
}
