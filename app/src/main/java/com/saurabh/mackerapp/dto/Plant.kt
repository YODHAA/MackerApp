package com.saurabh.mackerapp.dto

data class Plant(var genus: String, var species: String, var common: String) {
    constructor() : this("", "", "")

    override fun toString(): String {
        return common
    }
}