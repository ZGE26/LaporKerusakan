package com.aryaersi0120.laporkerusakan.model

data class GeneralApiResponse(
    val status : String,
    val message : String? = "",
    val id : Int? = null,
    val imageUrl : String? = null,
    val imagePath : String? = null,
    val nama_barang : String? = null,
    val deskripsi_kerusakan : String? = null,
    val lokasi : String? = null
)
