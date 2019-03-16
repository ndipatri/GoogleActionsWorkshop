package com.example

import com.google.actions.api.*
import com.google.actions.api.response.helperintent.Permission
import com.google.api.services.actions_fulfillment.v2.model.SpeechResponse
import org.slf4j.LoggerFactory


class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {

        return getResponseBuilder(request).apply {
            LOGGER.info("Welcome intent started.")

            var userName = request.userStorage["userName"]

            if (userName != null) {
                // Using Speech Synthesis Markup Language (SSML) for effect:

                add("<speak><p><prosody rate=\"x-slow\" pitch=\"-3st\">Arg</prosody><break time=\"200ms\"/></p></speak>")

                add(" Ahoy $userName! Would you be loading your catapult now, sir?")
                addSuggestions(arrayOf("Yes", "No"))
            } else {
                add("ignore this text") // this has to be here or there will be JSON error w/ Dialog Flow
                add(Permission().apply {
                    setPermissions(arrayOf(PERMISSION_NAME))
                    setContext("Beggin yer pardon, sir! So I don't have to call you sir anymore")
                })
            }
        }.build().also {
            LOGGER.info("Welcome intent ended.")
        }
    }

    @ForIntent("actions_intent_PERMISSION")
    fun permissions(request: ActionRequest): ActionResponse {
        LOGGER.info("Permission intent started.")

        return getResponseBuilder(request).apply {
            if (request.isPermissionGranted()) {

                (request.userStorage as MutableMap).apply {
                    set("userName", "Pirate ${request.user!!.profile.givenName}")
                }

                var userName = request.userStorage["userName"] as String

                add("Much obliged $userName, Would you be loading your catapult now, sir?")
                addSuggestions(arrayOf("Yes", "No"))
            } else {
                add("I respect a Pirate's Privacy! Would you be loading your catapult now, sir?")
                addSuggestions(arrayOf("Yes", "No"))
            }
        }.build().also {
            LOGGER.info("Permissions intent end.")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }
}
