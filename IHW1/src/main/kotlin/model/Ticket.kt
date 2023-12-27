package model

import repository.Database
import java.lang.IllegalStateException

class Ticket(session: String, place: Int, db: Database): Entity() {
    private var session: String
    private var place: String
    private var come: String = "NO"
    private val db: Database

    init {
        if (place > 9 || place < 1) {
            throw IllegalStateException("Number of seats is 9")
        }
        this.db = db
        this.place = place.toString()
        this.session = session
        val result = TicketTable(db).select(2, 1, 1, session)
        if (result.count() == 9) {
            throw IllegalStateException("No seats")
        }
        for (elem in result) {
            if (elem.split('|')[2] == this.place) {
                throw IllegalStateException("This seat is already taken")
            }
        }
        values.add(this.session)
        values.add(this.place)
        values.add(this.come)
    }
}