package com.faithl.pack.internal.utils

import com.google.gson.GsonBuilder

val gson =
    GsonBuilder().enableComplexMapKeySerialization()
        .setPrettyPrinting().create()!!