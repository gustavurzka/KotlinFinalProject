package com.example.stopgamehelper.Controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_fim_de_jogo.*

class FimDeJogoActivity : AppCompatActivity() {
    var sala: Sala? = null
    var jogador: Jogador? = null
    var criador: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fim_de_jogo)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Fim de Jogo"
        var db = FirebaseFirestore.getInstance()
        sala = intent.getSerializableExtra(Keys.SALA.valor) as Sala?
        jogador = intent.getSerializableExtra(Keys.JOGADOR.valor) as Jogador?
        criador = intent.getBooleanExtra("criador", false)


        db.collection(Keys.SALAS.valor).document(sala!!.numero.toString()).get()
            .addOnSuccessListener { data ->
                if (data != null) {
                    sala = data.toObject(Sala::class.java)!!
                    var pontuacao = 0
                    for (rodada in sala!!.rodadas!!){
                        for(pontos in rodada.pontos!!){
                            if(pontos.nomeJogador.equals(jogador!!.nome)){
                                pontuacao = pontuacao + pontos.pontuacao!!
                            }
                        }
                    }
                    tvPontos.text = "Sua pontuação é de: ${pontuacao}"
                }
            }

        btnVoltar.setOnClickListener {
            var intent = Intent(this@FimDeJogoActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}