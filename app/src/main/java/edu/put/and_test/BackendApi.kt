package edu.put.and_test

import android.util.Log
import edu.put.and_test.models.Game
import edu.put.and_test.models.SingleGame
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class BackendApi(private val apiUrl: String) {

    private val xmlFactoryObject: XmlPullParserFactory = XmlPullParserFactory.newInstance()
    private val myparser: XmlPullParser = xmlFactoryObject.newPullParser()
    private val dataDownloader = DataDownloader()

    // Retrieve collections of games and expansions for a given username
    fun GetCollections(username: String): Pair<List<Game>, List<Game>>? {
        val boardgameApiResponse =
            dataDownloader.downloadData("${apiUrl}collection?username=$username&subtype=boardgame&excludesubtype=boardgameexpansion")
        val boardgameExpansionsApiResponse =
            dataDownloader.downloadData("${apiUrl}collection?username=$username&subtype=boardgameexpansion")

        if (boardgameApiResponse == null || boardgameExpansionsApiResponse == null) {
            // User not found, show a notification
            return null
        } else {
            val games = parseCollection(boardgameApiResponse)
            val expansions = parseCollection(boardgameExpansionsApiResponse)
            if (games == null || expansions == null) {
                return null
            }

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
    fun parseCollection(xml: String): List<Game>? {
        val games = mutableListOf<Game>()

        myparser.setInput(xml.reader())
        var eventType = myparser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && myparser.name == "error") {
                // User not found, show a notification
                return null
            }
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
                    Log.e(
                        "BackendApi",
                        "Error parsing game name = $name, yearPublished = $yearPublished, id = $id"
                    )
                }
            }
            eventType = myparser.next()
        }

        return games
    }

    fun GetSingleGame(game: Game): SingleGame? {
        val boardgameApiResponse = dataDownloader.downloadData("${apiUrl}thing?id=${game.id}&stats=1")
        if (boardgameApiResponse == null) {
            // Game not found, show a notification
            return null
        }
        val singleGame = ParseSingleGame(boardgameApiResponse, game)
        return singleGame
    }

    fun ParseSingleGame(xml: String, game: Game): SingleGame? {
        myparser.setInput(xml.reader())
        var eventType = myparser.eventType
        var singleGame: SingleGame? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            // Check if the current event is the start tag for the "item" element
            if (eventType == XmlPullParser.START_TAG && myparser.name == "item") {

                var description: String? = null
                var image: String? = null
                var rank: String? = null

                // Loop until the end tag for the "item" element is encountered
                while (!(eventType == XmlPullParser.END_TAG && myparser.name == "item")) {
                    if (eventType == XmlPullParser.START_TAG) {
                        when (myparser.name) {
                            "description" -> {
                                myparser.next()
                                description = myparser.text
                            }

                            "image" -> {
                                myparser.next()
                                image = myparser.text
                            }

                            "statistics" -> {
                                while (!(eventType == XmlPullParser.END_TAG && myparser.name == "statistics")) {
                                    if (eventType == XmlPullParser.START_TAG && myparser.name == "ratings") {
                                        while (!(eventType == XmlPullParser.END_TAG && myparser.name == "ratings")) {
                                            if (eventType == XmlPullParser.START_TAG && myparser.name == "ranks") {
                                                while (!(eventType == XmlPullParser.END_TAG && myparser.name == "ranks")) {
                                                    if (eventType == XmlPullParser.START_TAG && myparser.name == "rank") {
                                                        val type =
                                                            myparser.getAttributeValue(null, "name")
                                                        if (type == "boardgame") {
                                                            rank = myparser.getAttributeValue(
                                                                null,
                                                                "value"
                                                            )
                                                        }
                                                    }
                                                    eventType = myparser.next()
                                                }
                                            }
                                            eventType = myparser.next()
                                        }
                                    }
                                    eventType = myparser.next()
                                }
                            }

                        }
                    }
                    eventType = myparser.next()
                }
                singleGame = SingleGame(game, description, image, rank)
                return singleGame

            }
            eventType = myparser.next()
        }
        return null
    }


}
