package com.example.myapplication

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkSpeedTest(param: (Any) -> Unit) : AsyncTask<Void, Void, Double>() {

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): Double? {
        return try {
            val url = URL("http://speedtest.tele2.net/100MB.zip")
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val inputStream = BufferedInputStream(urlConnection.inputStream)
                val bytesRead = inputStream.readBytes().size
                inputStream.close()
                // Return the size of the downloaded file as an example of a Double result
                bytesRead.toDouble()
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: FileNotFoundException) {
            Log.e("NetworkSpeedTest", "File not found: ${e.message}")
            null
        } catch (e: IOException) {
            Log.e("NetworkSpeedTest", "I/O error: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("NetworkSpeedTest", "Unexpected error: ${e.message}")
            null
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Double?) {
        super.onPostExecute(result)
        if (result != null) {
            Log.d("NetworkSpeedTest", "Download successful, file size: $result bytes")
        } else {
            Log.d("NetworkSpeedTest", "Download failed")
        }
    }
}

