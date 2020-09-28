package com.example.stopgamehelper.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopgamehelper.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bem Vindo(a)"

        btnLogIn.setOnClickListener {
            val intent = Intent(this@MainActivity, SalaActivity::class.java).apply {
                putExtra("sala", etCodSala.text.toString())
                putExtra("jogador", etNomeJogador.text.toString())
            }
            startActivity(intent)
        }
        btnNovaSala.setOnClickListener {
            val intent = Intent(this@MainActivity, NovaSalaActivity::class.java)
            startActivity(intent)

        }
    }
}