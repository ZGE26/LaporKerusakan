package com.aryaersi0120.laporkerusakan.model

data class Kerusakan(
    val id : Int,
    val imagepath : String,
    val nama_barang : String,
    val deskripsi_kerusakan : String,
    val lokasi : String,
    val upload_date : String,
    val imageUrl : String?= null
)
