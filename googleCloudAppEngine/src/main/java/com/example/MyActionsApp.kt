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

//        (request.userStorage as MutableMap).apply {
//            clear()
//        }

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

    @ForIntent("Permission Intent")
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

    @ForIntent("Ready Catapult")
    fun readyCatapult(request: ActionRequest): ActionResponse {
        LOGGER.info("'Ready Catapult' intent started.")

        return getResponseBuilder(request).apply {

            add(StringBuilder().apply {
                append("<speak>")

                LOGGER.info("parameter is ${request.getParameter("confusion")}")

                if (request.getParameter("confusion").toString().isNotEmpty()) {
                    append("Sorry, I'm going a bit too fast it seems!  We're going to " +
                            "launch a ping pong ball using a popsicle stick and rubber " +
                            "band catapult. Go get the ping pong ball and the " +
                            "catapult. Put the ball in the basket and get ready to pull " +
                            "the launch arm all the way back. ")
                }

                append("Want to Load the catapult?")

                append("</speak>")
            }.toString())

        }.build().also {
            LOGGER.info("'Ready Catapult' intent ended.")
        }
    }

    @ForIntent("Load Catapult Follow-Up")
    fun loadCatapult(request: ActionRequest): ActionResponse {
        LOGGER.info("'Load Catapult Follow-Up' intent started.")

        return getResponseBuilder(request).apply {

            actuateServo()

            add("<speak>" +
                    "Catapult is loaded! Set your target and let " +
                    "me know when to launch the pint pong ball!" +
                "</speak>")

        }.build().also {
            LOGGER.info("'Load Catapult Follow-Up' intent ended.")
        }
    }

    @ForIntent("Launch Catapult Follow-Up")
    fun launchCatapult(request: ActionRequest): ActionResponse {
        LOGGER.info("'Launch Catapult Follow-Up' intent started.")

        return getResponseBuilder(request).apply {

            actuateServo()

            add("<speak>" +
                    "Great shot! Ready to start again or do you want to say goodbye?" +
                "</speak>")

        }.build().also {
            LOGGER.info("'Launch Catapult Follow-Up' intent ended.")
        }
    }

    private fun actuateServo() {
        var client = OkHttpClient()

        Logger.getLogger(OkHttpClient::class.java.name).level = Level.FINE

        // GCP has an Integration path for Particle, but we want to
        // demonstrate the use of a vanilla webhook

        // For our simple webhook, we don't need to send any arguments.
        // Particle also requires functions to be called via POST
        var emptyBody =
                RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                                         "")

        var request = Request.Builder()
                .header("Authorization",
                        "Bearer {ACCESS_TOKEN}")
                .post(emptyBody)
                .url("https://api.particle.io/v1/devices/{DEVICE_ID}/actuate")

                .build()

        client.newCall(request).execute().also {

            var respondBody = it.body()!!.source().readUtf8()

            it.close()
            LOGGER.info("Response: success=${it.isSuccessful}, " +
                        "and response was $respondBody")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }
}
