package com.evgeny5454.musicplayer.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.evgeny5454.musicplayer.data.entities.Song
import com.evgeny5454.musicplayer.other.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(Constants.SONG_COLLECTION)

    @SuppressLint("LogNotTimber")
    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            Log.d("MusicDatabaseException", e.toString())
            emptyList()
        }
    }
}