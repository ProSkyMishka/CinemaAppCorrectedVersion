package controller

import model.MovieTable
import model.SessionTable
import repository.Database
import view.PrintMenu
import view.PrintSelect

class ThirdStep(ps: PrintSelect, rd: Reader, db: Database,
                second: SecondStep
): Step(ps, rd, db) {
    private val second: SecondStep

    init {
        this.second = second
    }

    override fun start() {
        val menu = Menu(listOf(1, 2, 3, 4, 5, 0), listOf(
            Pair("add movie") {addMovie()},
            Pair("print movies") { movies() },
            Pair("choose one movie") { chooseMovie() },
            Pair("print sessions") { sessions() },
            Pair("print sessions' history") { history() },
            Pair("back") { second.start() })
        )
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }

    private fun movies() {
        ps.printMovies(MovieTable(db).select(1, 1))
        start()
    }

    private fun addMovie() {
        MovieTable(db).insert(rd.readMovie())
        start()
    }

    private fun sessions() {
        ps.printSessions(SessionTable(db).select(1, 1))
        start()
    }

    private fun history() {
        ps.printHistoryOfSessions(SessionTable(db).select(1, 1))
        start()
    }

    private fun chooseMovie() {
        val values = mutableListOf<Pair<String, () -> Unit>>()
        val keys = mutableListOf<Int>()
        var i = 1
        for (elem in MovieTable(db).select(1, 1)) {
            keys.add(i++)
            values.add(Pair(elem.split('|')[1]
            ) { ForthStep(ps, rd, db, this, elem.split('|')[1]).start() })
        }
        values.add(Pair("back") { start() })
        keys.add(0)
        val menu = Menu(keys, values)
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }
}