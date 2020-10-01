package com.example.stopgamehelper.Model

import java.io.Serializable

class Sala(
    var criador: String = "",
    var pontuacaoMax: Int = 0,
    var participantes: List<Jogador> = emptyList(),
    var rodadas: List<Rodada>?= emptyList(),
    var letras: List<String> = emptyList(),
    var letrarUsadas: List<String>? = emptyList(),
) : Serializable {
    var numero = 0;
    var status = 0;


}