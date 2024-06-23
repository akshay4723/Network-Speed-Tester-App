package com.example.networkspeedtest

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var buttonTestSpeed: Button
    private lateinit var textViewSpeed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTestSpeed = findViewById(R.id.buttonTestSpeed)
        textViewSpeed = findViewById(R.id.textViewSpeed)

        buttonTestSpeed.setOnClickListener {
            NetworkSpeedTest().execute("http://ipv4.download.thinkbroadband.com/100MB.zip")
        }
    }

    inner class NetworkSpeedTest : AsyncTask<String, Void, Double>() {

        override fun doInBackground(vararg params: String?): Double? {
            val urlString = params[0] ?: return null
            return try {
                val url = URL(urlString)
                val urlConnection = url.openConnection() as HttpURLConnection
                val startTime = System.currentTimeMillis()
                try {
                    val inputStream = BufferedInputStream(urlConnection.inputStream)
                    val totalBytes = inputStream.readBytes().size
                    inputStream.close()
                    val endTime = System.currentTimeMillis()
                    val timeTakenMillis = endTime - startTime
                    // Convert time to seconds and bytes to megabytes for Mbps
                    val timeTakenSeconds = timeTakenMillis / 1000.0
                    val totalMegabytes = totalBytes / (1024.0 * 1024.0)
                    totalMegabytes / timeTakenSeconds
                } finally {
                    urlConnection.disconnect()
                }
            } catch (e: IOException) {
                Log.e("NetworkSpeedTest", "I/O error: ${e.message}")
                null
            } catch (e: Exception) {
                Log.e("NetworkSpeedTest", "Unexpected error: ${e.message}")
                null
            }
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
