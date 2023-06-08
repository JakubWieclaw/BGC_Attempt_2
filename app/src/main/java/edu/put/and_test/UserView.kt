package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.io.File

class UserView : AppCompatActivity() {

    private lateinit var usernameTextView: TextView
    private lateinit var numGamesTextView: TextView
    private lateinit var numAddOnsTextView: TextView
    private lateinit var lastSyncTextView: TextView
    private lateinit var gamesButton: Button
    private lateinit var addOnsButton: Button
    private lateinit var syncButton: Button
    private lateinit var clearDataButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        usernameTextView = findViewById(R.id.usernameTextView)
        numGamesTextView = findViewById(R.id.numGamesTextView)
        numAddOnsTextView = findViewById(R.id.numAddOnsTextView)
        lastSyncTextView = findViewById(R.id.lastSyncTextView)
        gamesButton = findViewById(R.id.gamesButton)
        addOnsButton = findViewById(R.id.addOnsButton)
        syncButton = findViewById(R.id.syncButton)
        clearDataButton = findViewById(R.id.clearDataButton)

        // Retrieve user account data and display it
        val username = "JohnDoe"
        val numGames = 10
        val numAddOns = 5
        val lastSyncDate = "2023-06-08"

        usernameTextView.text = "Username: $username"
        numGamesTextView.text = "Number of Games: $numGames"
        numAddOnsTextView.text = "Number of Add-Ons: $numAddOns"
        lastSyncTextView.text = "Last Sync Date: $lastSyncDate"

        // Set click listeners for buttons
//        gamesButton.setOnClickListener {
//            // Navigate to Games list activity
//            startActivity(Intent(this, GamesListActivity::class.java))
//        }
//
//        addOnsButton.setOnClickListener {
//            // Navigate to Add-Ons list activity
//            startActivity(Intent(this, AddOnsListActivity::class.java))
//        }
//
//        syncButton.setOnClickListener {
//            // Navigate to Sync activity
//            startActivity(Intent(this, SyncActivity::class.java))
//        }

//        clearDataButton.setOnClickListener {
//            // Show confirmation dialog for clearing data
//            AlertDialog.Builder(this)
//                .setTitle("Clear Data")
//                .setMessage("Are you sure you want to clear all data?")
//                .setPositiveButton("Yes") { _, _ ->
//                    // Clear data and exit the application
//                    clearData()
//                    finish()
//                }
//                .setNegativeButton("No", null)
//                .show()
//        }
    }

    private fun clearData() {
        // Clear data logic goes here
        // Implement the necessary code to clear user data
    }
}
