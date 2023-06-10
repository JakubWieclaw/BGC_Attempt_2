package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import edu.put.and_test.models.User
import edu.put.and_test.R
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

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

        startButton = findViewById(R.id.button_start)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        startButton.setOnClickListener {
            startButton.isEnabled = false
            startButton.isClickable = false // Disable the button
            showProgressBar(true)

            val usernameEditText: EditText = findViewById(R.id.username)
            val username = usernameEditText.text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val user: User? = withContext(Dispatchers.IO) {
                        dataManager.GetUser(username)
                    }
                    handleApiResponse(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    startButton.isEnabled =
                        true // Enable the button after the operation is completed
                    startButton.isClickable = true
                    showProgressBar(false)
                }
            }
        }
    }

    // Handle the API response and take appropriate actions
    private fun handleApiResponse(user: User?) {
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
            //intent.putExtra("user", user.username)
            startActivity(intent)
        }
    }

    // Show or hide the progress bar
    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = ProgressBar.VISIBLE
            progressText.visibility = TextView.VISIBLE
        } else {
            progressBar.visibility = ProgressBar.GONE
            progressText.visibility = TextView.GONE
        }
    }
}
