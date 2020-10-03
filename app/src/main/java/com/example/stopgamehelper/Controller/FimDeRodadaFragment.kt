package com.example.stopgamehelper.Controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.stopgamehelper.Model.*
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore


class FimDeRodadaFragment : Fragment() {
    var btnEnviar: Button? = null
    var etPontos: EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fim_de_rodada, container, false)
        var db = FirebaseFirestore.getInstance()
        (activity as SalaActivity).supportActionBar?.title = "Fim da rodada"
        var jogador = arguments?.get(Keys.JOGADOR.valor) as Jogador
        var sala = arguments?.get(Keys.SALA.valor) as Sala
        var criador = arguments?.getBoolean("criador")
        var rodada = arguments?.get(Keys.RODADA.valor) as Rodada

        btnEnviar = view.findViewById(R.id.btnEnviar)
        etPontos = view.findViewById(R.id.etPontos)

        sala.status = Status.FIMDERODADA.estado
        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)


        db.collection(Keys.SALAS.valor).document(sala.numero.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    Toast.makeText(context, "Ocorreu um erro ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                if (data != null){
                    sala = data.toObject(Sala::class.java)!!
                    if (sala.confirmacoes == sala.participantes!!.size){
                        sala.rodadas?.add(rodada)
                        if(criador!! && sala.status != Status.FIMDEJOGO.estado && sala.status != Status.CONTINUAR.estado) {
                            var dialog = AlertDialog.Builder(context)
                            dialog.setTitle("Deseja continuar?")
                            dialog.setMessage("Você pode escolher se o jogo acaba aqui ou continua")
                            dialog.setPositiveButton("Continuar", DialogInterface.OnClickListener{ dialog, which ->
                                sala.status = Status.CONTINUAR.estado
                                db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
                            })
                            dialog.setNegativeButton("Encerrar",DialogInterface.OnClickListener{dialog, which ->
                                sala.status = Status.FIMDEJOGO.estado
                                db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
                            })
                            dialog.show()
                        }
                        if(sala.status == Status.CONTINUAR.estado){
                            val bundle = bundleOf(
                                Keys.SALA.valor to sala,
                                Keys.JOGADOR.valor to jogador,
                                "criador" to criador,
                                Keys.RODADA.valor to rodada
                            )
                            findNavController().navigate(
                                R.id.action_fimDeRodadaFragment_to_emJogoFragment,
                                bundle
                            )
                        }
                        if(sala.status == Status.FIMDEJOGO.estado){
                            var intent = Intent(context,FimDeJogoActivity::class.java).apply {
                                putExtra(Keys.SALA.name, sala)
                                putExtra(Keys.JOGADOR.name, jogador)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }

        btnEnviar!!.setOnClickListener {
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).get()
                .addOnSuccessListener {
                    sala = it.toObject(Sala::class.java)!!
                    if (etPontos!!.text.toString().toInt() > sala.pontuacaoMax) {
                        etPontos!!.setText("")
                        Toast.makeText(
                            context,
                            "Essa pontuação não é válida pois excede o limite da sala",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        rodada.pontos?.put(jogador.nome, etPontos!!.text.toString().toInt())
                        var aux = sala.confirmacoes
                        if(aux != null){
                            sala.confirmacoes = aux + 1
                        }
                        else{
                            sala.confirmacoes = 1
                        }
                        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
                        btnEnviar!!.isEnabled = false
                        Toast.makeText(
                            context,
                            "Pontuação enviada com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        return view
    }
}