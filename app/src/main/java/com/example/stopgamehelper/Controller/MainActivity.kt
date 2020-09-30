package com.example.stopgamehelper.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import android.text.Editable as Editable

class MainActivity : AppCompatActivity() {
    var sala: Sala? = null
    var jogador: Jogador? = null
    var userId: String? = null
    var userName: String? = null
    var jogadores = HashMap<String, Jogador>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Bem Vindo(a)"

        var jogadores = HashMap<String, Jogador>()
        db.collection(Keys.USUARIOS.valor).get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.e("Banco", "O Banco ainda está vazio")
            } else {
                for (document in result) {
                   var jogador = document.toObject(Jogador::class.java)
                    Log.e("teste", "Vê se imprime isso ${jogador.nome}")
                }
            }
        }

        val preferences =
            getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)

        userId = preferences.getString(getString(R.string.pref_userid), null)
        userName = preferences.getString(getString(R.string.pref_username), null)

        if (userId.isNullOrEmpty() && userName.isNullOrEmpty()) {
            tvUserId.text =
                "Você ainda não possui um ID, faça uma primeira conexão para criar um"
        } else {
            tvUserId.text = "Sua ID é ${userId}"
            etNomeJogador.setText(userName)
        }

        btnLogIn.setOnClickListener {
            if (userId.isNullOrEmpty() && userName.isNullOrEmpty()) {
                jogador = Jogador(etNomeJogador.text.toString())
                db.collection(Keys.USUARIOS.valor).add(jogador!!).addOnSuccessListener {
                    jogador!!.id = it.id
                    preferences.edit().apply {
                        putString(getString(R.string.pref_userid),it.id)
                        putString(getString(R.string.pref_username),userName)
                        commit()
                    }
                    Log.d("Sucesso", "Sucesso ao adiocionar")
                }.addOnFailureListener {
                    Log.d("Falha", "Erro ao adicionar")
                }
            }else{
                jogador = Jogador(etNomeJogador.text.toString())
                jogador!!.id = userId!!
                var intent = Intent(this@MainActivity, Sala::class.java).apply {
                    putExtra(Keys.JOGADOR.valor, jogador)
                    //Colocar a sala aqui
                }
            }
        }

        btnNovaSala.setOnClickListener {
            preferences.edit().apply() {
                putString(getString(R.string.pref_username), etNomeJogador.text.toString())
            }
        }
    }
}

