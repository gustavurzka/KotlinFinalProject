package com.example.stopgamehelper.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bem Vindo(a)"
        val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        val userId = preferences.getString(getString(R.string.pref_userid), null)
        var jogador : Jogador
        if(userId.isNullOrEmpty()){
            tvUserId.text = "Você ainda não possui um ID, faça uma primeira conexão para criar um"
        }else{
            tvUserId.text = "Sua ID é ${userId}"
        }


        btnLogIn.setOnClickListener {
            jogador = Jogador(etNomeJogador.text.toString())
            if (userId.isNullOrEmpty()){
                jogador.id = ""
            }
            preferences.edit().apply(){
                putString(getString(R.string.pref_username), etNomeJogador.text.toString())
            }
            val intent = Intent(this@MainActivity, SalaActivity::class.java).apply {
                putExtra("sala", etCodSala.text.toString())
                putExtra("jogador", etNomeJogador.text.toString())
            }
            startActivity(intent)
        }
        btnNovaSala.setOnClickListener {
            preferences.edit().apply(){
                putString(getString(R.string.pref_username), etNomeJogador.text.toString())
            }

            val intent = Intent(this@MainActivity, NovaSalaActivity::class.java).apply {
                putExtra("jogador",etNomeJogador.text.toString())
            }
            startActivity(intent)
        }
    }
}