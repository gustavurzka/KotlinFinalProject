package com.example.stopgamehelper.Controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.stopgamehelper.Model.*
import com.example.stopgamehelper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class EmJogoFragment : Fragment() {
    var tvLetra: TextView? = null
    var btnStop: Button? = null
    var rodada: Rodada? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_em_jogo, container, false)
        var db = FirebaseFirestore.getInstance()
        (activity as SalaActivity).supportActionBar?.title = "Em Jogo"
        rodada = Rodada()
        tvLetra = view.findViewById(R.id.tvLetra)
        btnStop = view.findViewById(R.id.btnStop)
        var jogador = arguments?.get(Keys.JOGADOR.valor) as Jogador
        var sala = arguments?.get(Keys.SALA.valor) as Sala
        var criador = arguments?.getBoolean("criador")

        var r = Random.nextInt(1, sala.letras!!.size)
        var letra = sala.letras!!.get(r)
        if(criador!!) {
            rodada!!.letra = letra
            sala.letras!!.remove(letra)
            sala.status = Status.SALA_EMJOGO.estado
            sala.letraUsada = letra
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
        }
        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).get().addOnSuccessListener {
                sala = it.toObject(Sala::class.java)!!
                tvLetra!!.text = sala.letraUsada
            }


        btnStop!!.setOnClickListener {
            sala!!.status = Status.STOP.estado
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
        }

        db.collection(Keys.SALAS.valor).document(sala.numero.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    Toast.makeText(context, "Ocorreu um erro ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                if (data != null) {
                    sala = data.toObject(Sala::class.java)!!
                    if (sala.status == Status.STOP.estado) {
                        val bundle = bundleOf(
                            Keys.SALA.valor to sala,
                            Keys.JOGADOR.valor to jogador,
                            "criador" to criador,
                            Keys.RODADA.valor to rodada
                        )
                        findNavController().navigate(
                            R.id.action_emJogoFragment_to_fimDeRodadaFragment,
                            bundle
                        )

                    }
                }
            }
        return view
    }


}

