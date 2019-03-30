/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example

import com.google.actions.api.App
import java.io.IOException
import java.util.Enumeration
import java.util.HashMap
import java.util.concurrent.ExecutionException
import java.util.stream.Collectors
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handles request received via HTTP POST and delegates it to your Actions app. See: [Request
 * handling in Google App
 * Engine](https://cloud.google.com/appengine/docs/standard/java/how-requests-are-handled).
 */
@WebServlet(name = "actions", value = ["/"])
class ActionsServlet : HttpServlet() {
    private val actionsApp = MyActionsApp()

    @Throws(IOException::class)
    protected override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        val body = req.getReader().lines().collect(Collectors.joining())
        LOG.info("doPost, body = {}", body)

        try {
            val jsonResponse = actionsApp.handleRequest(body, getHeadersMap(req)).get()
            LOG.info("Generated json = {}", jsonResponse)
            res.setContentType("application/json")
            writeResponse(res, jsonResponse)
        } catch (e: InterruptedException) {
            handleError(res, e)
        } catch (e: ExecutionException) {
            handleError(res, e)
        }

    }

    @Throws(IOException::class)
    protected override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.setContentType("text/plain")
        response
                .getWriter()
                .println(
                        "ActionsServlet is listening but requires valid POST request to respond with Action response.")
    }

    private fun writeResponse(res: HttpServletResponse, asJson: String) {
        try {
            res.getWriter().write(asJson)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun handleError(res: HttpServletResponse, throwable: Throwable) {
        try {
            throwable.printStackTrace()
            LOG.error("Error in App.handleRequest ", throwable)
            res.getWriter().write("Error handling the intent - " + throwable.message)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getHeadersMap(request: HttpServletRequest): Map<String, String> {
        val map = HashMap<String, String>()

        val headerNames = request.getHeaderNames()
        while (headerNames.hasMoreElements()) {
            val key = headerNames.nextElement() as String
            val value = request.getHeader(key)
            map.put(key, value)
        }
        return map
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(MyActionsApp::class.java!!)
    }

}