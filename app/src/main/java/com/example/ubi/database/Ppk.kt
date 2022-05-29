package com.example.ubi.database

data class Ppk (
    var name: String,
    var values: MutableList<String>,
    var dates: MutableList<String>,
    var visibility: Boolean = false
)