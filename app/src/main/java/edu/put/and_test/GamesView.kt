package edu.put.and_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.put.and_test.models.Game
import edu.put.and_test.R
import edu.put.and_test.models.SingleGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesView : AppCompatActivity() {

    private lateinit var gamesRecyclerView: RecyclerView
    private lateinit var headerTextName: TextView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var games: List<Game>
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games_view)

        gamesRecyclerView = findViewById(R.id.gamesRecyclerView)
        headerTextName = findViewById(R.id.headerTextView)

        dataManager = DataManager(this, "https://www.boardgamegeek.com/xmlapi2/")
        val showGames = intent.getBooleanExtra("showGames", true)
        val username = dataManager.GetSavedUser()!!.username

        if (showGames) {
            games = dataManager.GetGames()
            headerTextName.text = "${username}'s Games"
        } else {
            games = dataManager.GetExpansions()
            headerTextName.text = "${username}'s Expansions"
        }

        val sortedGames = games.sortedBy { it.name }

        gamesAdapter = GamesAdapter(this, sortedGames)
        gamesAdapter.setItemClickListener { position ->
            val game = games[position]
            GlobalScope.launch(Dispatchers.Main) {
                val singleGame = withContext(Dispatchers.IO) {
                    try {
                        dataManager.GetSingleGame(game)
                    } catch (e: Exception) {
                        println(e)
                        null
                    }

                }
                RunSingleGameView(singleGame)
            }
        }
        gamesRecyclerView.adapter = gamesAdapter
        gamesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun RunSingleGameView(singleGame: SingleGame?) {
        val intent = Intent(this, SingleGameView::class.java)
        intent.putExtra("singleGame", singleGame)
        startActivity(intent)
    }
}

