package edu.put.and_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import edu.put.and_test.models.SingleGame
import edu.put.and_test.R

class SingleGameView : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var rankTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var idTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_game_view)

        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        imageView = findViewById(R.id.imageView)
        rankTextView = findViewById(R.id.rankTextView)
        nameTextView = findViewById(R.id.nameTextView)
        yearTextView = findViewById(R.id.yearTextView)
        idTextView = findViewById(R.id.idTextView)

        val singleGame: SingleGame? = intent.getSerializableExtra("singleGame") as? SingleGame

        singleGame?.let {
            titleTextView.text = it.game.name
            descriptionTextView.text = android.text.Html.fromHtml(it.description, android.text.Html.FROM_HTML_MODE_LEGACY)
            rankTextView.text = "Rank: ${it.rank}"
            nameTextView.text = "Name: ${it.game.name}"
            yearTextView.text = "Year Published: ${it.game.yearPublished}"
            idTextView.text = "ID: ${it.game.id}"

            // Load the image using Glide library
            Glide.with(this)
                .load(it.image)
                .into(imageView)
        }
    }
}
