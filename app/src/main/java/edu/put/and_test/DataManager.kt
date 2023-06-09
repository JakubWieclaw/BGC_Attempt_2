package edu.put.and_test

import android.content.Context
import edu.put.and_test.models.User

class DataManager(context: Context, apiUrl: String){
    private val dbConnector = DBConnector(context)
    private val BackendApi = BackendApi(apiUrl)

    fun GetUser(username: String): User? {
        val (gamesList, expansionsList) = BackendApi.GetCollections(username)?: return null
        val curDate = java.util.Date()
        val user = User(username, gamesList.size, expansionsList.size, curDate)
        dbConnector.InsertUser(username, curDate)
        dbConnector.GamesToDB(gamesList)
        dbConnector.ExpansionsToDB(expansionsList)
        return user
    }

    fun GetSavedUser(): User? {
        val user = dbConnector.GetUser()
        if (user != null) {
            return user
        } else {
            return null
        }
    }

    fun CheckUser(): Boolean {
        val user = dbConnector.GetUser()
        if (user != null) {
            return true
        } else {
            return false
        }
    }
}