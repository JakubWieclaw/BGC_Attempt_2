package edu.put.and_test.models
import java.io.Serializable
import java.util.Date

data class User(val username: String, val numGames: Int, val numAddOns: Int, val lastSyncDate: Date): Serializable