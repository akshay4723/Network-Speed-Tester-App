package com.example.myapplication

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity2 : AppCompatActivity() {

    private lateinit var buttonTestSpeed: Button
    private lateinit var textViewSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this points to the correct layout file

        buttonTestSpeed = findViewById(R.id.buttonTestSpeed)
        textViewSpeed = findViewById(R.id.textViewSpeed)

        buttonTestSpeed.setOnClickListener {
            NetworkSpeedTest().execute("https://speedtest.net")
        }
    }

    inner class NetworkSpeedTest : AsyncTask<String, Void, Double>() {

        override fun doInBackground(vararg params: String?): Double? {
            val urlString = params[0] ?: return null
            var attempts = 0
            val maxAttempts = 3

            while (attempts < maxAttempts) {
                try {
                    val url = URL(urlString)
                    var urlConnection: HttpURLConnection? = null
                    try {
                        urlConnection = url.openConnection() as HttpURLConnection
                        urlConnection.connectTimeout = 10000  // 10 seconds
                        urlConnection.readTimeout = 10000  // 10 seconds
                        val startTime = System.currentTimeMillis()

                        val inputStream = BufferedInputStream(urlConnection.inputStream)
                        val totalBytes = inputStream.readBytes().size
                        inputStream.close()

                        val endTime = System.currentTimeMillis()
                        val timeTakenMillis = endTime - startTime
                        val timeTakenSeconds = timeTakenMillis / 1000.0
                        val totalMegabytes = totalBytes / (1024.0 * 1024.0)

                        return totalMegabytes / timeTakenSeconds
                    } finally {
                        urlConnection?.disconnect()
                    }
                } catch (e: IOException) {
                    Log.e("NetworkSpeedTest", "I/O error: ${e.message}, attempt: ${attempts + 1}")
                    attempts++
                } catch (e: Exception) {
                    Log.e("NetworkSpeedTest", "Unexpected error: ${e.message}")
                    return null
                }
            }
            return null
        }

        override fun onPostExecute(result: Double?) {
            super.onPostExecute(result)
            if (result != null) {
                textViewSpeed.text = "Download speed: %.2f Mbps".format(result)
            } else {
                textViewSpeed.text = "Failed to measure speed"
            }
        }
    }
}
