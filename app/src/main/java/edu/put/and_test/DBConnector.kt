package edu.put.and_test

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import edu.put.and_test.models.Game
import android.database.Cursor
import edu.put.and_test.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DBConnector(private val context: Context) {

    private val databaseName = "mydatabase.db"
    private val databaseVersion = 1

    private lateinit var database: SQLiteDatabase

    init {
        // Open or create the database
        try {
            openOrCreateDatabase()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        // Create the "Games" table if it doesn't exist
        CreateCollectionTableIfNotExist("Games")

        // Create the "Expansions" table if it doesn't exist
        CreateCollectionTableIfNotExist("Expansions")

        CreateUserTableIfNotExist()
    }

    private fun openOrCreateDatabase() {
        database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null)
    }

    private fun CreateCollectionTableIfNotExist(tableName: String) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $tableName (" +
                "name TEXT," +
                "yearPublished INTEGER," +
                "id INTEGER," +
                "thumbnail TEXT)"

        database.execSQL(createTableQuery)
    }

    private fun CreateUserTableIfNotExist() {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS User (" +
                "username TEXT, " +
                "lastSyncDate DATE)"

        database.execSQL(createTableQuery)
    }

    fun GetGames(): List<Game> {
        val games = mutableListOf<Game>()

        val cursor: Cursor = database.query("Games", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val yearPublished = cursor.getInt(cursor.getColumnIndex("yearPublished"))
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"))

                games.add(Game(name, yearPublished, id, thumbnail))
            } while (cursor.moveToNext())
        }

        cursor.close()

        return games
    }

    fun GamesToDB(games: List<Game>) {
        database.delete("Games", null, null)

        for (game in games) {
            val values = ContentValues()
            values.put("name", game.name)
            values.put("yearPublished", game.yearPublished)
            values.put("id", game.id)
            values.put("thumbnail", game.thumbnail)

            database.insert("Games", null, values)
        }
    }

    fun GetExpansions(): List<Game> {
        val expansions = mutableListOf<Game>()

        val cursor: Cursor = database.query("Expansions", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val yearPublished = cursor.getInt(cursor.getColumnIndex("yearPublished"))
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"))

                expansions.add(Game(name, yearPublished, id, thumbnail))
            } while (cursor.moveToNext())
        }

        cursor.close()

        return expansions
    }

    fun ExpansionsToDB(expansions: List<Game>) {
        database.delete("Expansions", null, null)

        for (expansion in expansions) {
            val values = ContentValues()
            values.put("name", expansion.name)
            values.put("yearPublished", expansion.yearPublished)
            values.put("id", expansion.id)
            values.put("thumbnail", expansion.thumbnail)

            database.insert("Expansions", null, values)
        }
    }

//    fun InsertUser(username: String, date: Date) {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val dateString = dateFormat.format(date)
//
//        val values = ContentValues()
//        values.put("username", username)
//        values.put("lastSyncDate", dateString)
//
//        database.insert("User", null, values)
//    }

    fun InsertUser(username: String, date: Date) {

        val query = "INSERT INTO User (username, lastSyncDate) VALUES (?, ?)"
        val statement = database.compileStatement(query)

        statement.bindString(1, username)
        statement.bindLong(2, date.time)

        statement.executeInsert()
    }



    fun GetUser(): User? {
        val cursor: Cursor = database.query("User", null, null, null, null, null, null)
        if (!cursor.moveToFirst()) {
            cursor.close()
            return null
        }
        val username = cursor.getString(cursor.getColumnIndex("username"))
        val lastSyncDate = cursor.getLong(cursor.getColumnIndex("lastSyncDate"))

        cursor.close()
        return User(username, GetGames().size, GetExpansions().size, Date(lastSyncDate))
    }

}