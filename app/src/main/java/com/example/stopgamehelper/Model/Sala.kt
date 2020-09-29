package com.example.stopgamehelper.Model

class Sala(
    var criador: String,
    var pontuacaoMax: Int,
    var participantes: MutableList<Jogador>,
    var rodadas: MutableList<Rodada>?,
    var letras: List<Char>,
    var letrarUsadas: MutableList<Char>?,
) {
    var id = 0;
}