package com.example

import com.google.actions.api.*
import com.google.actions.api.response.helperintent.Permission
import org.slf4j.LoggerFactory


class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {

        return getResponseBuilder(request).apply {
            LOGGER.info("Welcome intent started.")

            var userName = request.userStorage["userName"]

            if (userName != null) {
                // Using Speech Synthesis Markup Language (SSML) for effect:

                add("<speak><p><prosody rate=\"x-slow\" pitch=\"-3st\">Arg</prosody>" +
                    "<break time=\"200ms\"/></p></speak>")

                add("Ahoy $userName! Let me know when you are ready!")
            } else {
                add("ignore this text") // this has to be here or there will be
                                        // JSON error w/ Dialog Flow
                add(Permission().apply {
                    setPermissions(arrayOf(PERMISSION_NAME))
                    setContext("Beggin yer pardon, sir! " +
                               "So I don't have to call you sir anymore")
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

                add("Much obliged $userName, Let me know when you " +
                        "are ready!")

            } else {
                add("I respect a Pirate's privacy! Let me know when " +
                        "you are ready!")

            }
        }.build().also {
            LOGGER.info("Permissions intent end.")
        }
    }

    @ForIntent("Start Catapult")
    fun startCatapult(request: ActionRequest): ActionResponse {
        LOGGER.info("'Start Catapult' intent started.")

        return getResponseBuilder(request).apply {

            add("<speak>" +
                    "Put a Ping Pong ball in the basket, pull the arm back, " +
                    "and say 'Load!" +
                    "</speak>")

        }.build().also {
            LOGGER.info("'Start Catapult' intent ended.")
        }
    }





    @ForIntent("Start Catapult")
    fun stasdfrtCatapult(request: ActionRequest): ActionResponse {
        LOGGER.info("'Start Catapult' intent started.")

        return getResponseBuilder(request).apply {

            add(StringBuilder().apply {
                append("<speak>")
                append(
                    if (request.getParameter("confusion").toString().isNotEmpty()) {
                        "Sorry, I'm going a bit too fast it seems!  Go get the catapult " +
                        "and a ping ping ball and prepare to load the catapult."
                    } else {
                        "Got it! Prepare to load the catapult! "
                    }
                )

                append("Put a Ping Pong ball in the basket and get ready to pull the " +
                        "launch arm all the way back. When you are ready say 'Load'!")

                append("</speak>")
            }.toString())

        }.build().also {
            LOGGER.info("'Start Catapult' intent ended.")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }
}
