package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.button_start)

        startButton.setOnClickListener {
            val usernameEditText: EditText = findViewById(R.id.username)
            val username = usernameEditText.text.toString()

            // Perform API request to search for the user's game collection
            GlobalScope.launch(Dispatchers.IO) {
                val apiResponse = performGameCollectionRequest(username)

                runOnUiThread {
                    handleApiResponse(apiResponse, username)
                }
            }
        }
    }

    private fun performGameCollectionRequest(username: String): String {
        val apiUrl = "https://www.boardgamegeek.com/xmlapi2/collection?username=$username"
        val url = URL(apiUrl)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode

        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            val apiResponse = response.toString()

            apiResponse
        } else {
            ""
        }
    }


    private fun handleApiResponse(apiResponse: String, username: String) {
        if (apiResponse.contains("<error>")) {
            // User not found, show a notification
            Toast.makeText(
                this@MainActivity,
                "User not found, please try again.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
                val intent = Intent(this@MainActivity, UserView::class.java)
                startActivity(intent)
            }
        }
    }
