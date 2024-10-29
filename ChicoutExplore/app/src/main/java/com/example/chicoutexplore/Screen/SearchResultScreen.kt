package com.example.chicoutexplore.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chicoutexplore.ui.theme.ChicoutExploreTheme

@Composable
fun SearchResultScreen(){
    var searchQuery by remember { mutableStateOf("") }
    Column {
        Text(text = "Search Result")
        SearchBar(query = searchQuery, onQueryChanged = {searchQuery =it}) {
            
        }

    }
}
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text("Search") },
        leadingIcon = {
            IconButton(onClick = onSearchClicked) {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview
@Composable
fun SearchResultScreenPreview() {
    ChicoutExploreTheme{
        SearchResultScreen();
    }

}