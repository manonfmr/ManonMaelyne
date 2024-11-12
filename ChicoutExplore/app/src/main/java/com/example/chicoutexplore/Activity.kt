package com.example.chicoutexplore

data class Activity(val id: String = "",
                    val nom: String = "",
                    val description: String = "",
                    val location: String = "",
                    val prix: Double = 0.0,
                    val adresse: String= "",
                    val urlsPhoto: List<String> = listOf(),
                    val avis: List<String> = listOf(),
                    val latitude: Double = 0.0,
                    val longitude: Double = 0.0)
