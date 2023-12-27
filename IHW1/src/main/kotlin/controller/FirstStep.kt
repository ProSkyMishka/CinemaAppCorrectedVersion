package controller

import model.WorkerTable
import repository.Database
import view.PrintMenu
import view.PrintSelect
import kotlin.system.exitProcess

class FirstStep(ps: PrintSelect, rd: Reader, db: Database): Step(ps, rd, db) {
    override fun start() {
        val menu = Menu(
            listOf(1, 2, 0), listOf(
                Pair("registration") { registration() },
                Pair("authorisation") { authorisation() },
                Pair("close") { exitProcess(0) })
        )
        PrintMenu(menu).printMenu()
        MakeChoice(menu).makeChoice()
    }

    private fun registration() {
        println("registration:")
        val worker = rd.readWorker()
        val workerTable = WorkerTable(db)
        val result = workerTable.select(2, 1, 1, worker.name)
        if (result.isNotEmpty()) {
            println("Worker with this username already exist")
            start()
        }
        workerTable.insert(worker)
        SecondStep(ps, rd, db, this, worker.name).start()
    }

    private fun authorisation() {
        println("authorisation:")
        val worker = rd.readWorker()
        val workerTable = WorkerTable(db)
        val result = workerTable.select(2, 1, 1, worker.name)
        if (result.isNotEmpty()) {
            if (worker.check(result[0].split('|')[2])) {
                println("All right")
                SecondStep(ps, rd, db, this, worker.name).start()
            } else {
                println("Incorrect password")
                start()
            }
        }
        println("Firstly you should register oneself")
        start()
    }
}