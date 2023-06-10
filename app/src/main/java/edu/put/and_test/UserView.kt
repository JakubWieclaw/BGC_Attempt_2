package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import edu.put.and_test.models.User

class UserView : AppCompatActivity() {

    // Declare the required UI elements
    private lateinit var usernameTextView: TextView
    private lateinit var numGamesTextView: TextView
    private lateinit var numAddOnsTextView: TextView
    private lateinit var lastSyncTextView: TextView
    private lateinit var gamesButton: Button
    private lateinit var addOnsButton: Button
    private lateinit var syncButton: Button
    private lateinit var clearDataButton: Button

    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        // Initialize the UI elements
        usernameTextView = findViewById(R.id.usernameTextView)
        numGamesTextView = findViewById(R.id.numGamesTextView)
        numAddOnsTextView = findViewById(R.id.numAddOnsTextView)
        lastSyncTextView = findViewById(R.id.lastSyncTextView)
        gamesButton = findViewById(R.id.gamesButton)
        addOnsButton = findViewById(R.id.addOnsButton)
        syncButton = findViewById(R.id.syncButton)
        clearDataButton = findViewById(R.id.clearDataButton)

        // Create DataManager instance with API URL
        dataManager = DataManager(this, "https://www.boardgamegeek.com/xmlapi2/")

        val user = dataManager.GetSavedUser()!!
        usernameTextView.text = "Username: ${user.username}"
        numGamesTextView.text = "Number of Games: ${user.numGames}"
        numAddOnsTextView.text = "Number of Add-Ons: ${user.numAddOns}"
        lastSyncTextView.text = "Last Sync Date: ${user.lastSyncDate}"

        gamesButton.setOnClickListener {
            // Start GamesView activity and indicate it to show user games
            val intent = Intent(this@UserView, GamesView::class.java)
            intent.putExtra("showGames", true)
            startActivity(intent)
        }

        addOnsButton.setOnClickListener {
            // Start GamesView activity and indicate it to show expansions
            val intent = Intent(this@UserView, GamesView::class.java)
            intent.putExtra("showGames", false)
            startActivity(intent)
        }

        clearDataButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear Data")
                .setMessage("Are you sure you want to clear all data?")
                .setPositiveButton("Clear") { _, _ ->
                    // Call clearData function in DataManager to clear data
                    dataManager.ClearData()
                    // Finish the activity to simulate the application closing
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        syncButton.setOnClickListener {
            val intent = Intent(this@UserView, SyncView::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            refreshUserData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun refreshUserData() {
//        val username = intent.getStringExtra("user") ?: return

        // Retrieve the user data from the DataManager
        val user: User = dataManager.GetSavedUser()!!

        // Update the UI with the retrieved user data
        usernameTextView.text = "Username: ${user.username}"
        numGamesTextView.text = "Number of Games: ${user.numGames}"
        numAddOnsTextView.text = "Number of Add-Ons: ${user.numAddOns}"
        lastSyncTextView.text = "Last Sync Date: ${user.lastSyncDate}"

    }

}
