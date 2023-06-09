package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.put.and_test.models.Game
import edu.put.and_test.R

class GamesView : AppCompatActivity() {

    private lateinit var gamesRecyclerView: RecyclerView
    private lateinit var headerTextName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games_view)

        gamesRecyclerView = findViewById(R.id.gamesRecyclerView)
        headerTextName = findViewById(R.id.headerTextView)

        val dataManager = DataManager(this, "https://www.boardgamegeek.com/xmlapi2/")
        val showGames = intent.getBooleanExtra("showGames", true)
        val username = dataManager.GetSavedUser()!!.username
        val games: List<Game>

        if (showGames) {
            games = dataManager.GetGames()
            headerTextName.text = "${username}'s Games"
        } else {
            games = dataManager.GetExpansions()
            headerTextName.text = "${username}'s Expansions"
        }

        val sortedGames = games.sortedWith(Comparator { game1, game2 ->
            game1.name.compareTo(game2.name)
        })




        val gamesAdapter = GamesAdapter(this, sortedGames)
        gamesRecyclerView.adapter = gamesAdapter
        gamesRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}
