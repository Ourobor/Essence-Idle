package com.ashe.essenceidle.model

import com.ashe.essenceidle.model.contact.ContactScript
import com.ashe.essenceidle.model.contact.WatchfulRaven
import com.ashe.essenceidle.model.database.ContactRecord

fun createContact(contactRecord: ContactRecord): ContactScript?{
    return when(contactRecord.contactId){
        "WR" -> {
            return WatchfulRaven(
                contactRecord.previousSteps.map { WatchfulRaven.Steps.valueOf(it) },
                WatchfulRaven.Steps.valueOf(contactRecord.currentStepId),
                contactRecord.unread)
        }
        else -> null
    }
}