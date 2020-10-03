package com.example.stopgamehelper.Model

import java.io.Serializable

class Rodada(
    var letra: String = "",
    var pontos: Map<String, Int>? = null, var status: Int? = 0
) : Serializable {
}