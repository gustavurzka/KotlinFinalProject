package com.example.stopgamehelper.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.R
import kotlinx.android.synthetic.main.activity_sala.*

class SalaActivity : AppCompatActivity() {
    var sala : Sala? = null
    var jogador : Jogador? = null
    var criador :  Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala)
        setSupportActionBar(toolbar)
        sala = intent.getSerializableExtra(Keys.SALA.valor) as Sala?
        jogador = intent.getSerializableExtra(Keys.JOGADOR.valor) as Jogador?
        criador = intent.getBooleanExtra("criador", false)
    }
}