package com.ssstor.teleport43.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssstor.teleport43.R
import com.ssstor.teleport43.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var vb : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
    }
}