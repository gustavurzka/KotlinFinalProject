package com.example.stopgamehelper.Model

import java.io.Serializable

class Rodada(
    var letra: String = "",
    var pontos: MutableList<Pontos>? = null
) : Serializable {
}