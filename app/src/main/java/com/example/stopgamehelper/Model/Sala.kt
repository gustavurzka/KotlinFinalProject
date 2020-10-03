package com.example.stopgamehelper.Model

import java.io.Serializable

class Sala(
    var criador: String = "",
    var pontuacaoMax: Int = 0,
    var participantes: MutableList<Jogador>? = null,
    var rodadas: MutableList<Rodada>?= null,
    var letras: MutableList<String>? = null,
    var letraUsada: String? = null,
    var confirmacoes: Int? = null
) : Serializable {
    var numero = 0;
    var status = 0;


}