package com.example.stopgamehelper.Controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.Model.Status
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
            if (etNomeJogador.text.isNullOrEmpty() || etCodSala.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            if (userId.isNullOrEmpty() || userName.isNullOrEmpty()) {
                jogador = Jogador(etNomeJogador.text.toString())
                db.collection(Keys.USUARIOS.valor).add(jogador!!).addOnSuccessListener {
                    jogador!!.id = it.id
                    preferences.edit().apply {
                        putString(getString(R.string.pref_userid), it.id)
                        putString(getString(R.string.pref_username), etNomeJogador.text.toString())
                        commit()
                    }
                    db.collection(Keys.USUARIOS.valor).document(it.id).set(jogador!!)
                    db.collection(Keys.SALAS.valor)
                        .whereEqualTo("numero", etCodSala.text.toString().toInt()).get()
                        .addOnSuccessListener {
                            var salaEncontrada = Sala()
                            if (it.isEmpty) {
                                val dialog = AlertDialog.Builder(this@MainActivity)
                                dialog.setTitle("Sala não encontrada")
                                dialog.setMessage("A sala com o código digitado não foi encontrada")
                                dialog.setCancelable(true)
                                dialog.show()
                                return@addOnSuccessListener
                            }
                            for (sala in it) {
                                salaEncontrada = sala.toObject(Sala::class.java)
                                if (salaEncontrada.status == Status.SALA_FECHADA.estado) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "A sala que você está procurando ja está em jogo",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@addOnSuccessListener
                                }
                            }
                            var intent = Intent(this@MainActivity, SalaActivity::class.java).apply {
                                putExtra(Keys.JOGADOR.valor, jogador)
                                putExtra(Keys.SALA.valor, salaEncontrada)
                                putExtra("criador", false)
                            }
                            startActivity(intent)
                        }
                    Log.d("Sucesso", "Sucesso ao adiocionar")
                }.addOnFailureListener {

                    Log.d("Falha", "Erro ao adicionar")
                }
            } else {
                jogador = Jogador(etNomeJogador.text.toString())
                jogador!!.id = userId!!
                db.collection(Keys.USUARIOS.valor).document(userId!!).set(jogador!!)
                    .addOnSuccessListener {
                        preferences.edit().apply {
                            putString(getString(R.string.pref_userid), userId)
                            putString(
                                getString(R.string.pref_username),
                                etNomeJogador.text.toString()
                            )
                            commit()
                        }
                        db.collection(Keys.SALAS.valor)
                            .whereEqualTo("numero", etCodSala.text.toString().toInt()).get()
                            .addOnSuccessListener {
                                var salaEncontrada = Sala()
                                if (it.isEmpty) {
                                    val dialog = AlertDialog.Builder(this@MainActivity)
                                    dialog.setTitle("Sala não encontrada")
                                    dialog.setMessage("A sala com o código digitado não foi encontrada")
                                    dialog.setCancelable(true)
                                    dialog.show()
                                    return@addOnSuccessListener
                                }
                                for (sala in it) {
                                    salaEncontrada = sala.toObject(Sala::class.java)
                                    if (salaEncontrada.status == Status.SALA_FECHADA.estado) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "A sala que você está procurando ja está em jogo",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        return@addOnSuccessListener
                                    }
                                }
                                var intent =
                                    Intent(this@MainActivity, SalaActivity::class.java).apply {
                                        putExtra(Keys.JOGADOR.valor, jogador)
                                        putExtra(Keys.SALA.valor, salaEncontrada)
                                        putExtra("criador", false)
                                    }
                                startActivity(intent)
                            }
                    }
            }
        }
        btnNovaSala.setOnClickListener {
            if (etNomeJogador.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            if (userId.isNullOrEmpty() && userName.isNullOrEmpty()) {
                jogador = Jogador(etNomeJogador.text.toString())
                db.collection(Keys.USUARIOS.valor).add(jogador!!).addOnSuccessListener {
                    jogador!!.id = it.id
                    preferences.edit().apply {
                        putString(getString(R.string.pref_userid), it.id)
                        putString(getString(R.string.pref_username), etNomeJogador.text.toString())
                        commit()
                    }
                    db.collection(Keys.USUARIOS.valor).document(it.id).set(jogador!!)
                    var intent = Intent(this@MainActivity, NovaSalaActivity::class.java).apply {
                        putExtra(Keys.JOGADOR.valor, jogador)
                    }
                    startActivity(intent)
                    Log.d("Sucesso", "Sucesso ao adiocionar")
                }.addOnFailureListener {

                    Log.d("Falha", "Erro ao adicionar")
                }
            } else {
                jogador = Jogador(etNomeJogador.text.toString())
                jogador!!.id = userId!!
                db.collection(Keys.USUARIOS.valor).document(userId!!).set(jogador!!)
                    .addOnSuccessListener {
                        preferences.edit().apply {
                            putString(getString(R.string.pref_userid), userId)
                            putString(getString(R.string.pref_username), etNomeJogador.text.toString())
                            commit()
                        }
                        var intent = Intent(this@MainActivity, NovaSalaActivity::class.java).apply {
                            putExtra(Keys.JOGADOR.valor, jogador)
                        }
                        startActivity(intent)
                    }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
