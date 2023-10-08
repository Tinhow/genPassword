package com.example.genPass

import java.util.Random

data class Password(
    val description: String,
    val length: Int,
    val includeUppercase: Boolean,
    val includeNumbers: Boolean,
    val includeSpecialChars: Boolean
)