package com.ar.smshub

import android.os.AsyncTask
import android.telephony.SmsManager
import android.util.Log
import khttp.responses.Response
import org.json.JSONObject

class PostReceivedMessage : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {
        val receiveURL = params[0]
        val deviceId = params[1]
        val smsBody = params[2]
        val smsSender = params[3]
        try {

            Log.d("-->", "POSTing SMS to $receiveURL")
            val apiResponse : Response = khttp.post(
                url = receiveURL,
                data = JSONObject(
                    mapOf("deviceId" to deviceId, "message" to smsBody, "number" to smsSender, "action" to "RECEIVED")
                )
            )

            val responseData = apiResponse.jsonObject
            val responseAction = responseData.get("action")

            if (!responseAction.equals("RESPOND")) {
                return "nothing responded"
            }

            // send the response back to the sender
            val responseText = responseData.get("text") as String
            val messageId = responseData.get("messageId") as String

            Log.d("-->", "Sending response SMS for messageId $messageId to $smsSender")
            val smsManager = SmsManager.getDefault() as SmsManager

            // split the message
            val splitMessage = smsManager.divideMessage(responseText)

            // send a multipart message
            smsManager.sendMultipartTextMessage(smsSender, null, splitMessage, null, null)
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