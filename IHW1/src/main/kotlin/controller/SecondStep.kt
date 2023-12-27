package controller

import model.WorkerTable
import repository.Database
import view.PrintMenu
import view.PrintSelect

class SecondStep(ps: PrintSelect, rd: Reader, db: Database,
                 first: FirstStep, name: String
): Step(ps, rd, db){
    private var name: String
    private val first: FirstStep

    init {
        this.first = first
        this.name = name
    }

    override fun start() {
        val menu = Menu(listOf(1, 2, 3, 0), listOf(
            Pair("delete account") { deleteWorker() },
            Pair("work with movies") { ThirdStep(ps, rd, db, this).start() },
            Pair("print workers") { workers() },
            Pair("back") { first.start() })
        )
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }

    private fun deleteWorker() {
        WorkerTable(db).delete(name)
        first.start()
    }

    private fun workers() {
        ps.printWorkers(WorkerTable(db).select(1, 1))
        start()
    }
}