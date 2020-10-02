package com.example.stopgamehelper.View

import android.widget.TextView
import com.example.stopgamehelper.Model.Jogador
import com.example.stopgamehelper.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class JogadorItem(var jogador: Jogador) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var tvJogador : TextView = viewHolder.itemView.findViewById(R.id.tvNomeJogador)
        tvJogador.text = "${jogador.nome} entrou na sala"

    }

    override fun getLayout(): Int {
        return R.layout.jogador_item
    }
}