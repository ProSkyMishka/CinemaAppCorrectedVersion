package model

import repository.Database

class MovieTable(db: Database): EntityTable(db) {
    init {
        table = "movie"
        columns = db.getColumns(table)
        this.db = db
    }
}