package com.example.stopgamehelper.Controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stopgamehelper.Model.*
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_fim_de_rodada.*


class FimDeRodadaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fim_de_rodada)
        setSupportActionBar(toolbar)
        var db = FirebaseFirestore.getInstance()
        supportActionBar?.title = "Fim da rodada"
        var jogador = intent.getSerializableExtra(Keys.JOGADOR.valor) as Jogador
        var sala = intent.getSerializableExtra(Keys.SALA.valor) as Sala
        var criador = intent.getBooleanExtra("criador", false)
        var rodada = intent.getSerializableExtra(Keys.RODADA.valor) as Rodada

        sala.status = Status.FIMDERODADA.estado
        sala.confirmacoes = 0
        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)

        db.collection(Keys.SALAS.valor).document(sala.numero.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    Toast.makeText(
                        this@FimDeRodadaActivity,
                        "Ocorreu um erro ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@addSnapshotListener
                }
                if (data != null) {
                    sala = data.toObject(Sala::class.java)!!
                    if(sala.letras.isNullOrEmpty()){
                        sala.status = Status.FIMDEJOGO.estado
//                        if(rodada.pontos.isNullOrEmpty()){
//                            rodada.pontos = mutableListOf(Pontos(jogador.nome, etPontos.text.toString().toInt()))
//                        }else{
//                            rodada.pontos!!.add(Pontos(jogador.nome, etPontos.text.toString().toInt()))
//                        }
                        if (sala.rodadas.isNullOrEmpty()) {
                            sala.rodadas = mutableListOf(rodada)
                        } else {
                            sala.rodadas?.add(rodada)
                        }
                        db.collection(Keys.SALAS.valor).document(sala.numero.toString())
                            .set(sala)
                    }
                    if (sala.confirmacoes == sala.participantes!!.size) {
                        if (criador!! && sala.status != Status.FIMDEJOGO.estado && sala.status != Status.CONTINUAR.estado && sala.status != Status.SALA_EMJOGO.estado) {
                            var dialog = AlertDialog.Builder(this@FimDeRodadaActivity)
                            dialog.setTitle("Deseja continuar?")
                            dialog.setMessage("Você pode escolher se o jogo acaba aqui ou continua")
                            dialog.setPositiveButton(
                                "Continuar",
                                DialogInterface.OnClickListener { dialog, which ->
                                    sala.status = Status.CONTINUAR.estado
//                                    if(rodada.pontos.isNullOrEmpty()){
//                                        rodada.pontos = mutableListOf(Pontos(jogador.nome, etPontos.text.toString().toInt()))
//                                    }else{
//                                        rodada.pontos!!.add(Pontos(jogador.nome, etPontos.text.toString().toInt()))
//                                    }
                                    if (sala.rodadas.isNullOrEmpty()) {
                                        sala.rodadas = mutableListOf(rodada)
                                    } else {
                                        sala.rodadas?.add(rodada)
                                    }
                                    db.collection(Keys.SALAS.valor).document(sala.numero.toString())
                                        .set(sala)
                                })
                            dialog.setNegativeButton(
                                "Encerrar",
                                DialogInterface.OnClickListener { dialog, which ->
                                    sala.status = Status.FIMDEJOGO.estado
//                                    if(rodada.pontos.isNullOrEmpty()){
//                                        rodada.pontos = mutableListOf(Pontos(jogador.nome, etPontos.text.toString().toInt()))
//                                    }else{
//                                        rodada.pontos!!.add(Pontos(jogador.nome, etPontos.text.toString().toInt()))
//                                    }
                                    if (sala.rodadas.isNullOrEmpty()) {
                                        sala.rodadas = mutableListOf(rodada)
                                    }
                                    else {
                                        sala.rodadas?.add(rodada)
                                    }
                                    db.collection(Keys.SALAS.valor).document(sala.numero.toString())
                                        .set(sala)
                                })
                            dialog.show()
                        }
                        if (sala.status == Status.CONTINUAR.estado) {
                            val intent2 = Intent(this@FimDeRodadaActivity, EmJogoActivity::class.java).apply {
                                    putExtra(Keys.SALA.valor, sala)
                                    putExtra(Keys.JOGADOR.valor, jogador)
                                    putExtra("criador", criador)
                                    putExtra(Keys.RODADA.valor, rodada)
                                }
                            startActivity(intent2)
                        }
                        if (sala.status == Status.FIMDEJOGO.estado) {
                            var intent3 = Intent(this@FimDeRodadaActivity, FimDeJogoActivity::class.java)
                            intent3.putExtra(Keys.SALA.valor, sala)
                            intent3.putExtra(Keys.JOGADOR.valor, jogador)
                            intent3.putExtra("criador", criador)
                            intent3.putExtra(Keys.RODADA.valor, rodada)

                            startActivity(intent3)
                        }
                    }
                }
            }

        btnEnviar!!.setOnClickListener {
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).get()
                .addOnSuccessListener {
                    if(etPontos!!.text.isNullOrEmpty()){
                        Toast.makeText(
                            this@FimDeRodadaActivity,
                            "Digite sua pontuação",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnSuccessListener
                    }
                    sala = it.toObject(Sala::class.java)!!
                    if (etPontos!!.text.toString().toInt() > sala.pontuacaoMax) {
                        etPontos!!.setText("")
                        Toast.makeText(
                            this@FimDeRodadaActivity,
                            "Essa pontuação não é válida pois excede o limite da sala",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        var aux = sala.confirmacoes
                        if(rodada.pontos.isNullOrEmpty()){
                            rodada.pontos = mutableListOf(Pontos(jogador.nome, etPontos.text.toString().toInt()))
                        }else{
                            rodada.pontos!!.add(Pontos(jogador.nome, etPontos.text.toString().toInt()))
                        }
                        if (aux != null) {
                            sala.confirmacoes = aux + 1
                        } else {
                            sala.confirmacoes = 1
                        }
                        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
                        btnEnviar!!.isEnabled = false
                        Toast.makeText(
                            this@FimDeRodadaActivity,
                            "Pontuação enviada com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}