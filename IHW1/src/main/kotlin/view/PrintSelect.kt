package view

import java.time.LocalDateTime

class PrintSelect {
    fun printWorkers(result: List<String>) {
        println("Workers:")
        for (elem in result) {
            println(elem.split('|')[1])
        }
    }

    fun printMovies(result: List<String>) {
        println("Movies:")
        for (elem in result) {
            val elemCopy = elem.split('|')
            println("movie name: ${elemCopy[1]}\t|\tduration(in min): ${elemCopy[2]}")
        }
    }

    fun printSessions(result: List<String>) {
        println("Sessions:")
        for (elem in result) {
            val elemCopy = elem.split('|')
            if (LocalDateTime.parse(elemCopy[3]) > LocalDateTime.now()) {
                println(
                    "session name: ${elemCopy[1]}\t|\tstart time: ${elemCopy[2]}\t|" +
                            "\tend time: ${elemCopy[3]}\t|\tmovie: ${elemCopy[4]}"
                )
            }
        }
    }

    fun printHistoryOfSessions(result: List<String>) {
        println("Sessions' history:")
        for (elem in result) {
            println(elem)
        }
    }

    fun printPlaces(name: String, result: List<String>) {
        println("Seats on session $name:")
        val taken = mutableListOf<Int>()
        while (taken.count() != 9) {
            taken.add(0)
        }
        for (elem in result) {
            val elemCopy = elem.split('|')[2].toInt()
            val come = elem.split('|')[3]
            if (come == "NO") {
                taken[elemCopy - 1] = elemCopy
            } else {
                taken[elemCopy - 1] = elemCopy * 100
            }
        }
        println("---------")
        var j = 1
        for (i in taken) {
            if (j * 100 == i) {
                print("${j}c|")
                ++j
            } else if (j == i) {
                print("${i}t|")
                ++j
            } else {
                print("${j}f|")
                ++j
            }
            if ((j - 1) % 3 == 0) {
                println("\n---------")
            }
        }
    }
}