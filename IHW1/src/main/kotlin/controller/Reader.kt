package controller

import model.Movie
import model.Session
import model.Ticket
import model.Worker
import repository.Database
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class Reader(db: Database) {
    val db: Database

    init {
        this.db = db
    }

    fun readTicket(name: String): Ticket {
        println("Name of session:")
        println(name)
        var flag = false
        var seat = 0
        while (!flag) {
            println("Seat number:")
            try {
                seat = readln().toInt()
                flag = true
            } catch (e: NumberFormatException) {
                println("it must be number")
            }
        }
        var ticket: Ticket? = null
        try {
            ticket = Ticket(name, seat, db)
        } catch (e: IllegalStateException) {
            println(e)
        }
        return ticket!!
    }

    fun readDuration(): Long {
        var flag = false
        var duration: Long = 0
        while (!flag) {
            println("new duration(in min):")
            try {
                duration = readln().toLong()
                flag = true
            } catch (e: NumberFormatException) {
                println("it must be number")
            }
        }
        return duration
    }

    fun readMovie(): Movie {
        println("Name of movie:")
        val name = readln()
        var flag = false
        var duration = 0
        while (!flag) {
            println("duration(in min):")
            try {
                duration = readln().toInt()
                flag = true
            } catch (e: NumberFormatException) {
                println("it must be number")
            }
        }
        var movie: Movie? = null
        try {
            movie = Movie(name, duration)
        } catch (e: IllegalStateException) {
            println(e)
            readMovie()
        }
        return movie!!
    }

    fun readSessionStart(name: String, movie: String): Session {
        var flag = false
        var startTime = LocalDateTime.now()
        while (!flag) {
            println("New start time in yyyy-mm-ddThh:mm format(example: 2023-12-24T20:20):")
            val start = readln()
            try {
                startTime = LocalDateTime.parse(start)
                flag = true
            } catch (e: DateTimeParseException) {
                println("it is not correct")
            }
        }
        return Session(name, startTime, movie, db)
    }

    fun readSession(movie: String): Session {
        println("Name of session:")
        val name = readln()
        var flag = false
        var startTime = LocalDateTime.now()
        while (!flag) {
            println("Start time in yyyy-mm-ddThh:mm format(example: 2023-12-24T20:20):")
            val start = readln()
            try {
                startTime = LocalDateTime.parse(start)
                flag = true
            } catch (e: DateTimeParseException) {
                println("it is not correct")
            }
        }
        println("Name of movie:")
        println(movie)
        var session: Session? = null
        try {
            session =  Session(name, startTime, movie, db)
        } catch (e: IllegalStateException) {
            println(e)
            readSession(movie)
        }
        return session!!
    }

    fun readWorker(): Worker {
        println("Name:")
        val name = readln()
        println("password:")
        val password = readln()
        return Worker(name, password)
    }
}