package edu.put.and_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class SyncView : AppCompatActivity() {
    private lateinit var lastSyncTextView: TextView
    private lateinit var syncButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_view)

        lastSyncTextView = findViewById(R.id.lastSyncTextView)
        syncButton = findViewById(R.id.syncButton)
        progressBar = findViewById(R.id.progressBar)

        dataManager = DataManager(this, "https://www.boardgamegeek.com/xmlapi2/")

        // Retrieve the last synchronization date from the DataManager or use a default value
        val lastSyncDate = dataManager.GetLastSyncDate()

        // Set the last synchronization date in the TextView
        lastSyncTextView.text = "Last Synchronization: $lastSyncDate"

        // Set click listener for the syncButton
        syncButton.setOnClickListener {
            val isSyncRequired = dataManager.CheckForSync()

            if (!isSyncRequired) {
                // Perform the synchronization
                showConfirmationDialog()
            } else {
                dataManager.PerformSync()
                onBackPressed()
            }
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Synchronization Confirmation")
            .setMessage("The last synchronization was performed less than 24 hours ago. Do you want to synchronize again?")
            .setPositiveButton("Yes") { _, _ ->
                // Perform the synchronization
                dataManager.PerformSync()
                onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }
}