package com.example.chicoutexplore

data class Activity(val id: String = "",
                    val nom: String = "",
                    val description: String = "",
                    val location: String = "",
                    val prix: String = "",
                    val adresse: String= "",
                    val urlsPhoto: List<String> = listOf(),
                    val avis: List<String> = listOf())
