package edu.put.and_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view)

        // Create DataManager instance with API URL
        val dataManager = DataManager(this, "https://www.boardgamegeek.com/xmlapi2/")

        // Initialize the UI elements
        usernameTextView = findViewById(R.id.usernameTextView)
        numGamesTextView = findViewById(R.id.numGamesTextView)
        numAddOnsTextView = findViewById(R.id.numAddOnsTextView)
        lastSyncTextView = findViewById(R.id.lastSyncTextView)
        gamesButton = findViewById(R.id.gamesButton)
        addOnsButton = findViewById(R.id.addOnsButton)
        syncButton = findViewById(R.id.syncButton)
        clearDataButton = findViewById(R.id.clearDataButton)

        // Retrieve the saved user data from the DataManager
        val user: User = dataManager.GetSavedUser()!!

        // Display the user data on the UI elements
        usernameTextView.text = "Username: ${user.username}"
        numGamesTextView.text = "Number of Games: ${user.numGames}"
        numAddOnsTextView.text = "Number of Add-Ons: ${user.numAddOns}"
        lastSyncTextView.text = "Last Sync Date: ${user.lastSyncDate}"
    }
}
