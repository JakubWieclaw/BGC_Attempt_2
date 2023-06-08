package edu.put.BGC_151765

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.put.and_test.R
import edu.put.and_test.UserView
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

//    private fun performGameCollectionRequest(username: String): String {
//        val apiUrl = "https://www.boardgamegeek.com/xmlapi2/collection?username=$username"
//        val url = URL(apiUrl)
//        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//        connection.requestMethod = "GET"
//
//        val responseCode = connection.responseCode
//
//        return if (responseCode == HttpURLConnection.HTTP_OK) {
//            val reader = BufferedReader(InputStreamReader(connection.inputStream))
//            val response = StringBuilder()
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                response.append(line)
//            }
//            reader.close()
//
//            response.toString()
//        } else {
//            ""
//        }
//    }

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
//            println("API Response: $apiResponse") // Print the API response for debugging

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
            // User found, fetch and save the XML response
            val gamesListUrl = "https://www.boardgamegeek.com/xmlapi2/collection?username=$username"
            val apiRequest = URL(gamesListUrl)
            val connection: HttpURLConnection = apiRequest.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = connection.inputStream
                val file = File(filesDir, "game_list.xml") // Name and location of the XML file
                val fileOutputStream = FileOutputStream(file)

                inputStream.use { input ->
                    fileOutputStream.use { output ->
                        input.copyTo(output) // Copy the XML content to the file
                    }
                }

                fileOutputStream.close()
                inputStream.close()

                val intent = Intent(this@MainActivity, UserView::class.java)
                startActivity(intent)
            } else {
                // Handle the case when the response code is not HTTP_OK
                Toast.makeText(
                    this@MainActivity,
                    "Error: Response code $responseCode",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



}
