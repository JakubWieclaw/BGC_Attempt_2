package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.put.and_test.models.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DataManager with API URL
        val dataManager =
            try {
                DataManager(this, "https://www.boardgamegeek.com/xmlapi2/")
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }

        // Check if a user is already stored in the local database
        if (dataManager.CheckUser()) {
            // User found, navigate to UserView activity
            val intent = Intent(this@MainActivity, UserView::class.java)
            startActivity(intent)
            return
        }

        val startButton: Button = findViewById(R.id.button_start)

        startButton.setOnClickListener {
            val usernameEditText: EditText = findViewById(R.id.username)
            val username = usernameEditText.text.toString()

            // Perform API request to search for the user's game collection in a background coroutine
            GlobalScope.launch(Dispatchers.IO) {
                val user: User? = dataManager.GetUser(username)

                // Handle the API response on the UI thread
                runOnUiThread {
                    handleApiResponse(user, username)
                }
            }
        }
    }

    // Handle the API response and take appropriate actions
    private fun handleApiResponse(user: User?, username: String) {
        if (user == null) {
            // User not found, show a notification
            Toast.makeText(
                this@MainActivity,
                "User not found, please try again.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // User found, navigate to UserView activity
            val intent = Intent(this@MainActivity, UserView::class.java)
            startActivity(intent)
        }
    }
}
