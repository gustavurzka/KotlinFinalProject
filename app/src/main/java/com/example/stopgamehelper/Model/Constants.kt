package com.example.stopgamehelper.Model

enum class Alfabeto(var letras: List<Char>) {
     ALFABETO_DIFICIL(listOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')),
     ALFABETO_NORMAL(listOf('A','B','C','D','E','F','G','H','I','J','L','M','N','O','P','Q','R','S','T','U','V','X','Z')),
     ALFABETO_FACIL(listOf('A','B','C','D','E','F','G','I','J','L','M','N','O','P','R','S','T','U','V'))
}

enum class Status(var estado: Int){
     SALA_ABERTA(1),
     SALA_FECHADA(2);
}

enum class Keys(var valor: String){
     USUARIOS("usuarios"),
     SALAS("salas"),
     JOGADOR("jogador")
}