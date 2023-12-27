package controller

import model.MovieTable
import model.SessionTable
import repository.Database
import view.PrintMenu
import view.PrintSelect
import java.time.LocalDateTime

class ForthStep(ps: PrintSelect, rd: Reader, db: Database,
                third: ThirdStep, value: String
): Step(ps, rd, db) {
    private var value: String
    private val third: ThirdStep

    init {
        this.value = value
        this.third = third
    }

    override fun start() {
        val menu = Menu(listOf(1, 2, 3, 4, 5, 6, 0), listOf(
            Pair("change $value duration") { update() },
            Pair("delete $value") { delete() },
            Pair("add session for $value") { addSession() },
            Pair("print session of $value") { sessions() },
            Pair("print sessions' history for $value") { history() },
            Pair("choose one session of $value") { chooseSession() },
            Pair("back") { third.start() })
        )
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }

    private fun update() {
        val duration = rd.readDuration()
        val oldDuration = MovieTable(db).select(2, 1, 1, value)[0].split('|')[2].toLong()
        val def = duration - oldDuration
        val sessions = SessionTable(db)
        var flag = true
        if (def > 0) {
            val result = sessions.select(1, 1)
            var i = 0
            while (i < (result.count() - 1)) {
                val elemCopy = result[i].split('|')
                if (LocalDateTime.parse(elemCopy[3]) > LocalDateTime.now()) {
                    if (elemCopy[4] == value) {
                        if (LocalDateTime.parse(elemCopy[3]).plusMinutes(def) >
                            LocalDateTime.parse(result[i + 1].split('|')[2])
                        ) {
                            flag = false
                            break
                        }
                    }
                }
                ++i
            }
        }
        if (flag) {
            val result = sessions.select(2, 1, 4, value)
            for (elem in result) {
                val elemCopy = elem.split('|')
                sessions.update(3, LocalDateTime.parse(elemCopy[3]).plusMinutes(def).toString(), elemCopy[1])
            }
            MovieTable(db).update(2, duration.toString(), value)
        } else {
            println("It is impossible as there would be session intersections")
        }
        start()
    }

    private fun delete() {
        val table = SessionTable(db)
        val sessions = table.select(2, 1, 4, value)
        for (elem in sessions) {
            table.delete(elem.split('|')[1])
        }
        MovieTable(db).delete(value)
        third.start()
    }

    private fun sessions() {
        ps.printSessions(SessionTable(db).select(2, 1, 4, value))
        start()
    }

    private fun history() {
        ps.printHistoryOfSessions(SessionTable(db).select(2, 1, 4, value))
        start()
    }

    private fun addSession() {
        SessionTable(db).insert(rd.readSession(value))
        start()
    }

    private fun chooseSession() {
        val values = mutableListOf<Pair<String, () -> Unit>>()
        val keys = mutableListOf<Int>()
        var i = 1
        for (elem in SessionTable(db).select(2, 1, 4, value)) {
            val elemCopy = elem.split('|')
            if (LocalDateTime.parse(elemCopy[3]) > LocalDateTime.now()) {
                keys.add(i++)
                values.add(Pair(
                    elemCopy[1]
                ) { FifthStep(ps, rd, db, this, elemCopy[1]).start() })
            }
        }
        values.add(Pair("back") { start() })
        keys.add(0)
        val menu = Menu(keys, values)
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }
}