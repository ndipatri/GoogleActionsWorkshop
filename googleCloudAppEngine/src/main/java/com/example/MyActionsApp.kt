package com.example

import com.google.actions.api.ActionRequest
import com.google.actions.api.ActionResponse
import com.google.actions.api.DialogflowApp
import com.google.actions.api.ForIntent
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import java.util.logging.Level
import java.util.logging.Logger


/**
 * Implements all intent handlers for this Action. Note that your App must extend from DialogflowApp
 * if using Dialogflow or ActionsSdkApp for ActionsSDK based Actions.
 */
class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {
        LOGGER.info("Welcome intent start.")

        val responseBuilder = getResponseBuilder(request)

        //
        val user = request.user

        if (user != null && user!!.getLastSeen() != null) {
            responseBuilder.add("Hey there! Good to see you again!")
        } else {
            responseBuilder.add("Hey there!")
        }

        responseBuilder.addSuggestions(arrayOf("Yes", "No"))

        LOGGER.info("Welcome intent end.")
        return responseBuilder.build()
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
                .header("Authorization", "Bearer {access token goes here}")
                .post(emptyBody)
                .url("https://api.particle.io/v1/devices/{device id goes here}/actuate")

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
