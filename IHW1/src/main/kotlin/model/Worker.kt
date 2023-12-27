package model

import java.math.BigInteger
import java.security.MessageDigest

class Worker(name: String, password: String): Entity() {
    private var password: String
    var name: String

    init {
        this.name = name
        this.password = md5(password)
        values.add(this.name)
        values.add(this.password)
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun check(password: String): Boolean {
        return this.password == password
    }
}