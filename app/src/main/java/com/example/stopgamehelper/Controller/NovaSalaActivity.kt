package com.example.stopgamehelper.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopgamehelper.R
import kotlinx.android.synthetic.main.activity_main.*

class NovaSalaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_sala)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Criar nova sala"
    }
}