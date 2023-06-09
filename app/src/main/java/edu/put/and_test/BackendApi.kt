package edu.put.and_test

import android.util.Log
import edu.put.and_test.models.Game
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class BackendApi(private val apiUrl: String) {

    private val xmlFactoryObject: XmlPullParserFactory = XmlPullParserFactory.newInstance()
    private val myparser: XmlPullParser = xmlFactoryObject.newPullParser()
    private val dataDownloader = DataDownloader()

    // Retrieve collections of games and expansions for a given username
    fun GetCollections(username: String): Pair<List<Game>, List<Game>>? {
        val boardgameApiResponse = dataDownloader.downloadData("${apiUrl}collection?username=$username&subtype=boardgame&excludesubtype=boardgameexpansion")
        val boardgameExpansionsApiResponse = dataDownloader.downloadData("${apiUrl}collection?username=$username&subtype=boardgameexpansion")

        if (boardgameApiResponse == null || boardgameExpansionsApiResponse == null) {
            // User not found, show a notification
            return null
        } else {
            val games = parseCollection(boardgameApiResponse)
            val expansions = parseCollection(boardgameExpansionsApiResponse)

            val numGames: Int = games.size
            val numAddOns: Int = expansions.size

            return if (numGames != null && numAddOns != null) {
                Pair(games, expansions)
            } else {
                null
            }
        }
    }

    // Parse the XML response and extract the collection of games
    fun parseCollection(xml: String): List<Game> {
        val games = mutableListOf<Game>()

        myparser.setInput(xml.reader())
        var eventType = myparser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && myparser.name == "item") {
                // Read values from children
                var name: String? = null
                var yearPublished: Int? = null
                var id: Int? = myparser.getAttributeValue(null, "objectid").toInt()
                var thumbnail: String? = null
                while (eventType != XmlPullParser.END_TAG || myparser.name != "item") {
                    if (eventType == XmlPullParser.START_TAG && myparser.name == "name") {
                        myparser.next()
                        name = myparser.text
                    }
                    if (eventType == XmlPullParser.START_TAG && myparser.name == "yearpublished") {
                        myparser.next()
                        yearPublished = myparser.text.toInt()
                    }
                    if (eventType == XmlPullParser.START_TAG && myparser.name == "thumbnail") {
                        myparser.next()
                        thumbnail = myparser.text
                    }
                    eventType = myparser.next()
                }

                if (name != null && id != null) {
                    games.add(Game(name, yearPublished, id, thumbnail))
                } else {
                    Log.e("BackendApi", "Error parsing game name = $name, yearPublished = $yearPublished, id = $id")
                }
            }
            eventType = myparser.next()
        }

        return games
    }
}
