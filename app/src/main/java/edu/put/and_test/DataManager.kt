package edu.put.and_test

import android.content.Context
import edu.put.and_test.models.User

// DataManager class responsible for managing user data
class DataManager(context: Context, apiUrl: String) {
    // Create an instance of the DBConnector class
    private val dbConnector = DBConnector(context)
    // Create an instance of the BackendApi class
    private val BackendApi = BackendApi(apiUrl)

    // Retrieve a User object for the given username
    fun GetUser(username: String): User? {
        // Retrieve game and expansion lists from the Backend API
        val (gamesList, expansionsList) = BackendApi.GetCollections(username) ?: return null
        // Get the current date and time
        val curDate = java.util.Date()
        // Create a User object with the retrieved data
        val user = User(username, gamesList.size, expansionsList.size, curDate)
        // Insert the user into the local database
        dbConnector.InsertUser(username, curDate)
        // Store game data in the local database
        dbConnector.GamesToDB(gamesList)
        // Store expansion data in the local database
        dbConnector.ExpansionsToDB(expansionsList)
        // Return the User object
        return user
    }

    // Retrieve the saved User object from the local database
    fun GetSavedUser(): User? {
        // Retrieve the user from the local database
        val user = dbConnector.GetUser()
        // If the user exists, return it; otherwise, return null
        if (user != null) {
            return user
        } else {
            return null
        }
    }

    // Check if a User object exists in the local database
    fun CheckUser(): Boolean {
        // Retrieve the user from the local database
        val user = dbConnector.GetUser()
        // If the user exists, return true; otherwise, return false
        if (user != null) {
            return true
        } else {
            return false
        }
    }
}
