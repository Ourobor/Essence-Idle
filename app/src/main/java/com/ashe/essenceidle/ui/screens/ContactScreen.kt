package com.ashe.essenceidle.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactScreen(){
    LazyColumn {
        val grouped = listOf("Alien Wolf", "Yawning Dynamo", "Dual Liberty", "Mother Falcon",
            "Poor Gambit", "Hefty Patriot", "Colossal Titan", "Salty Jumper", "Gifted Phantom",
            "Heavy Demon", "Tired Alpha", "Blushing Avalange", "Colossal Witness",
            "Hoarse Centurion", "Junior Prodigy", "Infinite Angel", "Flawless Nighthawk",
            "Gentle Apollo", "Heavy Traveler", "Purple Lyric").groupBy { it[0] }

        grouped.toSortedMap().forEach { (initial, contactsForInitial) ->
            stickyHeader {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        fontSize = 20.sp,
                        text = "$initial:")
                }
            }

            items(contactsForInitial) { contact ->
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    headlineContent = { Text(contact) },
                    leadingContent = {
                        Icon(Icons.Sharp.AccountCircle, contentDescription = "Picture")
                    })
                HorizontalDivider()
            }
        }

    }
}