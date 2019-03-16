package com.example

import com.google.actions.api.ActionRequest
import com.google.actions.api.ActionResponse
import com.google.actions.api.DialogflowApp
import com.google.actions.api.ForIntent
import org.slf4j.LoggerFactory

class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {
        LOGGER.info("Welcome intent start.")

        val responseBuilder = getResponseBuilder(request)

        // Notice use of built-in User object to help with building a
        // customized experience.

        // bla bla bla
        val user = request.user

        if (user != null && user!!.getLastSeen() != null) {
            responseBuilder.add("Good to see you again!")
        } else {
            responseBuilder.add("Hey there!")
        }
        responseBuilder.add("Would you like to load your catapult?")

        responseBuilder.addSuggestions(arrayOf("Yes", "No"))

        LOGGER.info("Welcome intent end.")
        return responseBuilder.build()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }
}
