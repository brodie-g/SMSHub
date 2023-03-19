package com.ar.smshub

import android.os.AsyncTask
import android.util.Log
import khttp.responses.Response
import org.json.JSONObject

class PostReceivedMessage : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {
        var receiveURL = params[0]
        var deviceId = params[1]
        var smsBody = params[2]
        var smsSender = params[3]
        try {
            lateinit var apiResponse : Response

            Log.d("-->", "POSTing SMS to $receiveURL")
            apiResponse = khttp.post(
                url = receiveURL,
                data = JSONObject(
                    mapOf("deviceId" to deviceId, "message" to smsBody, "number" to smsSender, "action" to "RECEIVED")
                )
            )
            return "did it"
        } catch (e: Exception) {
            Log.d("-->", "Failure POSTing received SMS")
            return "not great"
        }
    }

    override fun onPostExecute(result: String) {

    }

    override fun onPreExecute() {}

    override fun onProgressUpdate(vararg values: Void) {}
}