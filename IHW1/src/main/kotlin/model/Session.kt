package model

import repository.Database
import java.lang.IllegalStateException
import java.time.LocalDateTime

class Session(session: String, start: LocalDateTime, movie: String, db: Database
): Entity() {
    private var name: String
    private var movie: String
    private var start: LocalDateTime
    private var end: LocalDateTime

    init {
        this.name = session
        values.add(this.name)
        val result = MovieTable(db).select(2, 1, 1, movie)
        if (result.isNotEmpty()) {
            end = start.plusMinutes(result[0].split('|')[2].toLong())
        } else {
            throw IllegalStateException("cinema has not this movie")
        }
        if (start >= end) {
            throw IllegalStateException("start time can't be more or equal end time")
        }
        val table = SessionTable(db)
        for (elem in table.select(1, 1)) {
            val elemCopy = elem.split('|')
            if (elemCopy[1] == this.name) {
                continue
            }
            if (!(LocalDateTime.parse(elemCopy[2]) >= end || LocalDateTime.parse(elemCopy[3]) <= start)) {
                throw IllegalStateException("there is session at this time")
            } else if (start < LocalDateTime.now()) {
                throw IllegalStateException("session can't start before we add it")
            }
        }
        this.start = start
        this.movie = movie
        values.add(this.start.toString())
        values.add(this.end.toString())
        values.add(this.movie)
    }

    override fun toString(): String {
        return "$name $start $end $movie"
    }
}