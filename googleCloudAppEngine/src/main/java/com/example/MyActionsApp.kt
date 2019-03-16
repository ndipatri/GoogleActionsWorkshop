package com.example

import com.google.actions.api.*
import com.google.actions.api.response.helperintent.Permission
import org.slf4j.LoggerFactory


class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {
        LOGGER.info("Welcome intent started.")

        return getResponseBuilder(request).apply {
            LOGGER.info("Welcome intent started.")

            var userName = request.userStorage["userName"]

            if (userName != null) {
                add("Arr! Good to see you again, $userName! Would you be loading your catapult now, sir?")
                addSuggestions(arrayOf("Arr! Yes", "Arr! No"))
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
                addSuggestions(arrayOf("Arr! Yes", "Arr! No"))
            } else {
                add("I respect a Pirate's Privacy! Would you be loading your catapult now, sir?")
                addSuggestions(arrayOf("Arr! Yes", "Arr! No"))
            }
        }.build().also {
            LOGGER.info("Permissions intent end.")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }
}
