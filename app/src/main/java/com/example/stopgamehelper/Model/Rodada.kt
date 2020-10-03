package com.example.stopgamehelper.Model

import java.io.Serializable

class Rodada(
    var letra: String = "",
    var pontos: MutableMap<String, Int>? = null
) : Serializable {
}