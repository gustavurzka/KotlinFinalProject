package com.example.stopgamehelper.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.Model.Status
import com.example.stopgamehelper.R
import com.example.stopgamehelper.View.JogadorItem
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_sala.*

class SalaActivity : AppCompatActivity() {
    var sala: Sala? = null
    var jogador: Jogador? = null
    var criador: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala)
        setSupportActionBar(toolbar)
        var db = FirebaseFirestore.getInstance()
        val adapter = GroupAdapter<GroupieViewHolder>()
        var teste = intent
        sala = intent.getSerializableExtra(Keys.SALA.valor) as Sala?
        jogador = intent.getSerializableExtra(Keys.JOGADOR.valor) as Jogador?
        criador = intent.getBooleanExtra("criador", false)

        rcvParticipantes!!.layoutManager = LinearLayoutManager(this)
        rcvParticipantes!!.adapter = adapter
        tvNumSala!!.text = sala!!.numero.toString()

        sala!!.participantes?.add(jogador!!)

        db.collection(Keys.SALAS.valor).document(sala!!.numero.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    Toast.makeText(
                        this@SalaActivity,
                        "Ocorreu um erro ${error.message}",
                        Toast.LENGTH_LONG
                    )
                    return@addSnapshotListener
                }
                if (data != null) {
                    sala = data.toObject(Sala::class.java)!!
                    adapter.clear()
                    for (participante in sala!!.participantes!!) {
                        adapter.add(JogadorItem(participante))
                        adapter.notifyDataSetChanged()
                    }
                    tvParticipantes!!.text =
                        "${sala!!.participantes!!.count()} Participantes em sala"
                }
                if (sala!!.status == Status.SALA_FECHADA.estado) {
                    val intent = Intent(this@SalaActivity, EmJogoActivity::class.java).apply {
                        putExtra(Keys.SALA.valor, sala)
                        putExtra(Keys.JOGADOR.valor, jogador)
                        putExtra("criador", criador)
                    }
                    startActivity(intent)
                }
            }
        db.collection(Keys.SALAS.valor).document(sala!!.numero.toString()).set(sala!!)

        btnIniciar!!.setOnClickListener {
            if (!criador!!) {
                Toast.makeText(
                    this@SalaActivity,
                    "Somente o criador da sala pode começar o jogo",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            sala!!.status = Status.SALA_FECHADA.estado
            db.collection(Keys.SALAS.valor).document(sala!!.numero.toString()).set(sala!!)
        }
    }
}
