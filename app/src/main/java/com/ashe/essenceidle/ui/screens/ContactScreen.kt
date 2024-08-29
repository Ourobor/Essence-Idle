package com.ashe.essenceidle.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ashe.essenceidle.model.contact.ContactScript

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactScreen(navController: NavHostController, contacts: SnapshotStateMap<String, ContactScript>) {
    LazyColumn {
        //stub for later when we build this list elsewhere

        val grouped = contacts.filter {
            it.value.showContact()
        }.toList().map { it.second }.groupBy {
            it.contactLastName[0]
        }

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
                    headlineContent = { Text(contact.contactFirstName + " " + contact.contactLastName) },
                    leadingContent = {
                        Icon(Icons.Sharp.AccountCircle, contentDescription = "Picture")
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("Messages/" + contact.id)
                    }
                    )
                HorizontalDivider()
            }
        }

    }
}