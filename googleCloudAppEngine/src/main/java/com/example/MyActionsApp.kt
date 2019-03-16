package com.example

import com.google.actions.api.*
import com.google.actions.api.response.helperintent.Permission
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import java.util.logging.Level
import java.util.logging.Logger


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

                add("Ahoy $userName! Would you be loading or launching your " +
                    "catapult now, sir?")
                addSuggestions(arrayOf("Load", "Launch"))
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

                add("Much obliged $userName, Would you be loading or " +
                    " launching your catapult now, sir?")
                addSuggestions(arrayOf("Load", "Launch"))
            } else {
                add("I respect a Pirate's privacy! Would you be loading " +
                    "or launching your catapult now, sir?")
                addSuggestions(arrayOf("Load", "Launch"))
            }
        }.build().also {
            LOGGER.info("Permissions intent end.")
        }
    }

    @ForIntent("Default Welcome Intent - custom")
    fun welcomeFollowUp(request: ActionRequest): ActionResponse {
        LOGGER.info("Welcome follow-up intent started.")

        return getResponseBuilder(request).apply {

            when (request.getParameter("command")) {
                "Load", "Loading" -> {
                    add("<speak>" +
                        "Got it! Prepare to load the catapult! Put a Ping Pong ball " +
                        "in the basket, pull the launch arm all the way back, " +
                        "and hold until the latch closes." +
                        "</speak>")

                    actuateServo()
                }

                "Launch", "Launching" -> {
                    add("<speak>" +
                        "Ok, launching catapult!  Stand back!" +
                        "</speak>")

                    actuateServo()
                }

                else -> {
                    add("Sorry, I didn't get that")
                }
            }

        }.build().also {
            LOGGER.info("Welcome follow-up intent ended.")
        }
    }

    private fun actuateServo() {
        var client = OkHttpClient()

        Logger.getLogger(OkHttpClient::class.java.name).level = Level.FINE

        // GCP has an Integration path for Particle, but we want to demonstrate the use of a
        // vanilla webhook

        // For our simple webhook, we don't need to send any arguments.
        // Particle also requires functions to be called via POST
        var emptyBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), "")

        var request = Request.Builder()
                .header("Authorization", "Bearer c038e134b7fd9ce81b3ed41944983106d49b8e12")
                .post(emptyBody)
                .url("https://api.particle.io/v1/devices/e00fce6801eb8f6eb0c23d32/actuate")

                .build()

        client.newCall(request).execute().also {

            var respondBody = it.body()!!.source().readUtf8()

            it.close()
            LOGGER.info("Response: success=${it.isSuccessful}, and response was $respondBody")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }
}
