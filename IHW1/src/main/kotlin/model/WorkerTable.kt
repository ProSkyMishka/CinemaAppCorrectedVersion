package model

import repository.Database

class WorkerTable(db: Database): EntityTable(db) {
    init {
        table = "worker"
        columns = db.getColumns(table)
        this.db = db
    }
}