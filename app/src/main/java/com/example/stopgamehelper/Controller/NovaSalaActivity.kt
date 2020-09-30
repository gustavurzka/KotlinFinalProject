package com.example.stopgamehelper.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stopgamehelper.Model.Alfabeto
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_nova_sala.*
import kotlin.random.Random

class NovaSalaActivity : AppCompatActivity() {

    var jogador: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_sala)
        setSupportActionBar(toolbar)
        val db = FirebaseFirestore.getInstance()
        supportActionBar?.title = "Criar nova sala"
        jogador = intent.getStringExtra("jogador")

        rgDificuldade.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbFacil) {
                tvInf.text =
                    "Para um jogo rápido, as letras H, K, Q, W, X, Y e Z não estarão no jogo"
            } else if (checkedId == R.id.rbMedio) {
                tvInf.text = "Para um jogo simples, as letras K, W e Y não estarão no jogo"
            } else {
                tvInf.text = "Para um jogo desafiador, todas as letras estarão no jogo"
            }
        }

        btnCriarSala.setOnClickListener {
            var jogadores = mutableListOf<Jogador>()
            var alfabeto: List<Char>
            if (rbFacil.isChecked) {
                alfabeto = Alfabeto.ALFABETO_FACIL.letras
            } else if (rbMedio.isChecked) {
                alfabeto = Alfabeto.ALFABETO_NORMAL.letras
            } else {
                alfabeto = Alfabeto.ALFABETO_DIFICIL.letras
            }

            jogadores.add(Jogador(jogador!!))
            var sala = Sala(
                jogador!!,
                etItens.text.toString().toInt() * 10,
                jogadores,
                null,
                alfabeto,
                null
            )
            sala.numero = Random.nextInt(0, 5000)
            db.collection(Keys.SALAS.valor)

        }
    }
}