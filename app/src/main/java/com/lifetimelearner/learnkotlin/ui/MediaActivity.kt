package com.lifetimelearner.learnkotlin.ui

import android.content.ContentUris
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.lifetimelearner.learnkotlin.R
import java.io.File

class MediaActivity : AppCompatActivity() {

    var player : MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),108)

        getMediaAbove28()

    }

    private fun getMediaAbove28() {

        val contentUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        Toast.makeText(applicationContext,contentUri.toString(),Toast.LENGTH_SHORT).show()
        val projection = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.RELATIVE_PATH)
        val cursor = contentResolver.query(contentUri,projection,null,null,null)
        val from = arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME)

        val adapter = SimpleCursorAdapter(this,android.R.layout.simple_list_item_activated_1,cursor,from,
        intArrayOf(android.R.id.text1,android.R.id.text2),0)

        val listView = findViewById<ListView>(R.id.LV_Show_Media)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> // this is another function
            val id = listView.getChildAt(position).findViewById<TextView>(android.R.id.text1).text
            val name = listView.getChildAt(position).findViewById<TextView>(android.R.id.text2).text
            if(player.isPlaying) {
                player.pause()
                player.release()
            }
            val externalUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id.toString().toLong())
            player = MediaPlayer.create(this,externalUri)
            player.isLooping = true
            player.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(player.isPlaying) player.stop()
        player.release()
    }


}