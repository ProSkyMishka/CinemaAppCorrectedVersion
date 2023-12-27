package controller

import repository.Database
import view.PrintSelect

abstract class Step(ps: PrintSelect, rd: Reader, db: Database) {
    internal var db: Database
    internal var rd: Reader
    internal var ps: PrintSelect

    init {
        this.ps = ps
        this.db = db
        this.rd = rd
    }

    open fun start() {}
}