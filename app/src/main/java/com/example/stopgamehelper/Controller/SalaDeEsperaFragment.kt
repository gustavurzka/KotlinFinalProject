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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopgamehelper.Model.Keys
import com.example.stopgamehelper.Model.Sala
import com.example.stopgamehelper.Model.Status
import com.example.stopgamehelper.R
import com.example.stopgamehelper.View.JogadorItem
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder

class SalaDeEsperaFragment : Fragment() {
    var tvSala: TextView? = null
    var tvPart: TextView? = null
    var rcvPart: RecyclerView? = null
    var btnIniciar: Button? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var db = FirebaseFirestore.getInstance()
        val view = inflater.inflate(R.layout.fragment_sala_de_espera, container, false)
        var sala = (activity as SalaActivity).sala!!
        var jogador = (activity as SalaActivity).jogador!!
        var criador = (activity as SalaActivity).criador
        val adapter = GroupAdapter<GroupieViewHolder>()

        (activity as SalaActivity).supportActionBar?.title = "Em Espera"

        tvSala = view.findViewById(R.id.tvNumSala)
        tvPart = view.findViewById(R.id.tvParticipantes)
        rcvPart = view.findViewById(R.id.rcvParticipantes)
        btnIniciar = view.findViewById(R.id.btnIniciar)

        rcvPart!!.layoutManager = LinearLayoutManager(context)
        rcvPart!!.adapter = adapter
        tvSala!!.text = sala.numero.toString()

        sala.participantes?.add(jogador)


        db.collection(Keys.SALAS.valor).document(sala.numero.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    Toast.makeText(context, "Ocorreu um erro ${error.message}", Toast.LENGTH_LONG)
                    return@addSnapshotListener
                }
                if (data != null) {
                    sala = data.toObject(Sala::class.java)!!
                    adapter.clear()
                    for (participante in sala.participantes!!) {
                        adapter.add(JogadorItem(participante))
                        adapter.notifyDataSetChanged()
                    }
                    tvPart!!.text =
                        "${sala.participantes!!.count().toString()} Participantes em sala"
                }
                if (sala.status == Status.SALA_FECHADA.estado) {
                    val bundle = bundleOf(
                        Keys.SALA.valor to sala,
                        Keys.JOGADOR.valor to jogador,
                        "criador" to criador
                    )
                    findNavController().navigate(
                        R.id.action_salaDeEsperaFragment_to_emJogoFragment,
                        bundle
                    )
                }
            }

        db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)

        btnIniciar!!.setOnClickListener {
            if (!criador!!) {
                Toast.makeText(
                    context,
                    "Somente o criador da sala pode começar o jogo",
                    Toast.LENGTH_SHORT
                )
                return@setOnClickListener
            }
            sala.status = Status.SALA_FECHADA.estado
            db.collection(Keys.SALAS.valor).document(sala.numero.toString()).set(sala)
        }

        return view
    }


}