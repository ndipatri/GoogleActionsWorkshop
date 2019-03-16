package com.example

import com.google.actions.api.*
import com.google.actions.api.response.helperintent.Permission
import org.slf4j.LoggerFactory

class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {

        return getResponseBuilder(request).apply {
            LOGGER.info("Welcome intent started.")

            // Notice use of built-in User object to help with building a
            // customized experience.
            val user = request.user

            if (user?.lastSeen != null) {
                add("Hey there! Good to see you again!")
            } else {
                add("Hey there!")
            }
            add("Ping Pong balls don't hurt even if they hit you on the head.")

            endConversation()

        }.build().also {
            LOGGER.info("Welcome intent ended.")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java)
    }
}
