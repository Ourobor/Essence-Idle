package com.ashe.essenceidle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.model.contact.ChatMessage
import com.ashe.essenceidle.model.contact.ContactScript
import com.ashe.essenceidle.model.database.CharacterState

@Composable
fun MessageScreen(contactId: String, contacts: SnapshotStateMap<String, ContactScript>, characterState: CharacterState){
    var contactScript = contacts[contactId]
    if(contactScript == null){
        Text("User Not Found:$contactId")
    }
    else {
        //if the current script is unread
        if(contactScript.unread){
            //create a new copy of the script that is read
            contactScript = contactScript.newCopy(unread = false)
            //update the contacts array
            contacts[contactId] = contactScript
        }
        LazyColumn(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            reverseLayout = false
        ) {

            items(contactScript.previousSteps.flatMap { it.getMessages() }){
                ChatItem(chatMessage = it)
            }

            items(contactScript.currentStep.getMessages()){chatMessage ->
                ChatItem(chatMessage)
            }
            //A minor note, the if statement checking for readyness must be inside the item lambdas
            //because I *think* this isn't technically a composable function, so the checks won't
            //be recalculated because they aren't being recomposted
            item {
                if (contactScript.currentStep.readyForProgression(characterState)) {
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                }
            }

            items(contactScript.currentStep.chatOptions()) { chatMessage ->
                if(contactScript.currentStep.readyForProgression(characterState)) {
                    ChatItem(chatMessage.first) {
                        val newScript = contactScript.newStep(chatMessage.second, false)
                        contacts[contactScript.id] = newScript
                    }
                }
            }
        }
    }
}
//NEXT UP: Make clicking on the options do things!

@Composable
fun ChatItem(chatMessage: ChatMessage, onClick: (() -> Unit)? = null){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(align = if (chatMessage.received) Alignment.Start else Alignment.End)
            .padding(
                start = if (chatMessage.received) 0.dp else 32.dp,
                end = if (chatMessage.received) 32.dp else 0.dp
            )
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (chatMessage.received) 0f else 48f,
                        bottomEnd = if (chatMessage.received) 48f else 0f
                    )
                )
                .background(
                    if (chatMessage.received) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primaryContainer
                )
                .let {
                    if(onClick != null) it.clickable { onClick() } else it
                }
                .padding(12.dp)
        ) {
            Text(
                text = chatMessage.text,
                color = if (chatMessage.received) MaterialTheme.colorScheme.onSecondaryContainer
                else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}