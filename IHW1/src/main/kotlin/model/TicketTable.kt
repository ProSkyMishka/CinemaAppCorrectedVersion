package model

import repository.Database

class TicketTable(db: Database): EntityTable(db) {
    init {
        table = "ticket"
        columns = db.getColumns(table)
        this.db = db
    }
}