package controller

import model.SessionTable
import model.TicketTable
import repository.Database
import view.PrintMenu
import view.PrintSelect
import java.lang.Exception
import java.time.LocalDateTime

class FifthStep(ps: PrintSelect, rd: Reader, db: Database,
                forth: ForthStep, session: String
): Step(ps, rd, db) {
    private val value: String
    private val forth: ForthStep

    init {
        this.value = session
        this.forth = forth
    }

    override fun start() {
        val menu = Menu(listOf(1, 2, 3, 4, 5, 6, 0), listOf(
            Pair("change start time of $value") { update() },
            Pair("delete $value") { delete() },
            Pair("sell ticket to $value") { sellTicket() },
            Pair("print free and taken places on $value") { showSeats() },
            Pair("cancel ticket to $value") { cancel() },
            Pair("note that the visitor came to the $value") { cancel(false) },
            Pair("back") { forth.start() })
        )
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }

    private fun update() {
        val session = SessionTable(db).select(2, 1, 1, value)
        val elem = session[0].split('|')
        try {
            val new = rd.readSessionStart(value, elem[4])
            SessionTable(db).updateToSession(new, value)
        } catch (e: IllegalStateException) {
            println(e)
        }
        start()
    }

    private fun delete() {
        val table = TicketTable(db)
        val tickets = table.select(2, 1, 1, value)
        for (elem in tickets) {
            table.delete(elem.split('|')[0])
        }
        SessionTable(db).delete(value)
        forth.start()
    }

    private fun sellTicket() {
        try {
            TicketTable(db).insert(rd.readTicket(value))
        } catch (_: NullPointerException) {
        }
        start()
    }

    private fun showSeats() {
        ps.printPlaces(value, TicketTable(db).select(2, 1, 1, value))
        start()
    }

    private fun cancel(bool: Boolean = true) {

        val session = SessionTable(db).select(2, 1, 1, value)
        if (LocalDateTime.parse(session[0].split('|')[2]) <= LocalDateTime.now() && bool) {
            println("session has already started")
            start()
        } else if (LocalDateTime.parse(session[0].split('|')[2]) >= LocalDateTime.now() && !bool) {
            println("session has not started yet")
            start()
        } else {
            val values = mutableListOf<Pair<String, () -> Unit>>()
            val keys = mutableListOf<Int>()
            var i = 1
            for (elem in TicketTable(db).select(2, 1, 1, value)) {
                val elemCopy = elem.split('|')
                keys.add(i++)
                val action = if (bool) {
                    { TicketTable(db).delete(elemCopy[0]) }
                } else {
                    { TicketTable(db).update(3, "YES", elemCopy[0]) }
                }
                values.add(Pair(
                    "session: ${elemCopy[1]}, seat: ${elemCopy[2]}", action))
            }
            values.add(Pair("back") { start() })
            keys.add(0)
            val menu = Menu(keys, values)
            PrintMenu(menu).printMenu()
            MakeChoice(menu).makeChoice()
            start()
        }
    }
}
