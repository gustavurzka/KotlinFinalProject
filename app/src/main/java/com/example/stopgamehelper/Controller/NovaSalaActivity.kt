package com.example.stopgamehelper.Controller

import android.content.Intent
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.stopgamehelper.Model.*
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_nova_sala.*
import kotlin.random.Random

class NovaSalaActivity : AppCompatActivity() {

    var jogador: Jogador? = null
    var sala: Sala? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_sala)
        setSupportActionBar(toolbar)
        val db = FirebaseFirestore.getInstance()
        supportActionBar?.title = "Criar nova sala"
        jogador = intent.getSerializableExtra(Keys.JOGADOR.valor) as Jogador?

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
            if(etItens.text.isNullOrEmpty()){
                return@setOnClickListener
            }
            var jogadores = mutableListOf<Jogador>()
            var alfabeto: List<String>
            if (rbFacil.isChecked) {
                alfabeto = Alfabeto.ALFABETO_FACIL.letras
            } else if (rbMedio.isChecked) {
                alfabeto = Alfabeto.ALFABETO_NORMAL.letras
            } else {
                alfabeto = Alfabeto.ALFABETO_DIFICIL.letras
            }

            sala = Sala(
                jogador!!.nome,
                etItens.text.toString().toInt() * 10,
                jogadores,
                null,
                alfabeto,
                null
            )
            sala!!.numero = Random.nextInt(0, 5000)

            db.collection(Keys.SALAS.valor).get()
                .addOnSuccessListener {
                    for (item in it!!) {
                        if (sala!!.numero == item.toObject(Sala::class.java).numero) {
                            Toast.makeText(
                                this@NovaSalaActivity,
                                "Ocorreu um erro tente novamente",
                                Toast.LENGTH_SHORT
                            )
                            return@addOnSuccessListener
                        }
                    }
                    sala!!.status = Status.SALA_ABERTA.estado
                    db.collection(Keys.SALAS.valor).document(sala!!.numero.toString()).set(sala!!).addOnSuccessListener {
                        var intent = Intent(this@NovaSalaActivity, SalaActivity::class.java).apply {
                            putExtra("jogador", jogador)
                            putExtra("sala", sala)
                            putExtra("criador", true)
                        }
                        startActivity(intent)

                    }.addOnFailureListener {
                        Log.e("Falha", "Falha ao adicionar sala ${it.message}")
                    }
                }
        }
    }
}
