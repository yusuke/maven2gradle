package com.samuraism.maven2gradle

import java.io.File

fun main(args: Array<String>) {
    var kts = false
    var pom: File? = null
    for (arg in args) {
        if (arg == "-kts") {
            kts = true
        } else {
            val file = File(arg)
            if (file.exists()) {
                pom = file
            }
        }
    }
    if (pom == null) {
        pom = File("./pom.xml")
    }

    println(Maven2Gradle(kts).maven2gradle(pom))
}