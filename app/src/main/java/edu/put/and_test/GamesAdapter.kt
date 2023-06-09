package edu.put.and_test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.put.and_test.models.Game
import com.bumptech.glide.Glide

class GamesAdapter(private val context: Context, private val games: List<Game>) :
    RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_game_table, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serialNumberTextView: TextView = itemView.findViewById(R.id.serialNumberTextView)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val yearTextView: TextView = itemView.findViewById(R.id.yearTextView)



        fun bind(game: Game) {
            serialNumberTextView.text = (adapterPosition + 1).toString()

            // Load the thumbnail image using Glide library
            Glide.with(itemView)
                .load(game.thumbnail) // Assuming game.thumbnail is the URL of the thumbnail image
                .into(thumbnailImageView)

            titleTextView.text = game.name
            yearTextView.text = game.yearPublished.toString()
        }

    }
}

