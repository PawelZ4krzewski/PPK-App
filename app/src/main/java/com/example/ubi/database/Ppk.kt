package com.example.ubi.database

data class Ppk (
    var id: String,
    var name: String,
    var values: MutableList<String>,
    var dates: MutableList<String>,
    var visibility: Boolean = false
)