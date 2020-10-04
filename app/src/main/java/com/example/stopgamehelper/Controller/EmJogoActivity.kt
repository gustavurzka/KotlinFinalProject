package com.example.stopgamehelper.Controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stopgamehelper.Model.*
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_em_jogo.*
import kotlin.random.Random

class EmJogoActivity : AppCompatActivity() {
    var rodada: Rodada? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_em_jogo)
        var db = FirebaseFirestore.getInstance()
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Em Jogo"
        rodada = Rodada()
        var jogador = intent?.getSerializableExtra(Keys.JOGADOR.valor) as Jogador
        var sala = intent?.getSerializableExtra(Keys.SALA.valor) as Sala
        var criador = intent?.getBooleanExtra("criador", false)

        var r = Random.nextInt(1, sala.letras!!.size)
        var letra = sala.letras!!.get(r)
        sala.letras!!.remove(letra)
        rodada!!.letra = letra
        sala.status = Status.SALA_EMJOGO.estado
        sala.letraUsada = letra
        if (criador!!) {
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
        } else {
            Log.e("nada", "não é pra fazer nada")
        }
//        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).get()
//            .addOnSuccessListener {
//                sala = it.toObject(Sala::class.java)!!
//                rodada!!.letra = sala.letraUsada.toString()
//                tvLetra!!.text = sala.letraUsada.toString()
//            }

        btnParo.setOnClickListener {
            sala.status = Status.STOP.estado
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
        }

        db.collection(Keys.SALAS.valor).document(sala.numero.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    Toast.makeText(
                        this@EmJogoActivity,
                        "Ocorreu um erro ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (data != null) {
                    sala = data.toObject(Sala::class.java)!!
                    rodada!!.letra = sala.letraUsada.toString()
                    tvLetra!!.text = sala.letraUsada
                    if (sala.status == Status.STOP.estado) {
                        val intent =
                            Intent(this@EmJogoActivity, FimDeRodadaActivity::class.java).apply {
                                putExtra(Keys.SALA.valor, sala)
                                putExtra(Keys.JOGADOR.valor, jogador)
                                putExtra("criador", criador)
                                putExtra(Keys.RODADA.valor, rodada)
                            }
                        startActivity(intent)
                    }
                }
            }
    }
}


