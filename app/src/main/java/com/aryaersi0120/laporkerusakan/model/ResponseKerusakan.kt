package com.aryaersi0120.laporkerusakan.model

data class ResponseKerusakan(
    val status : String,
    val data : List<Kerusakan>,
    val message : String? = ""
)
