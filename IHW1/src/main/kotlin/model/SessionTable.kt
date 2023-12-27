package model

import repository.Database

class SessionTable(db: Database): EntityTable(db) {
    init {
        table = "session"
        columns = db.getColumns(table)
        this.db = db
    }

    fun updateToSession(session: Session, value: String) {
        val sessionArr = session.toString().split(' ')
        update(2, sessionArr[1], value)
        update(3, sessionArr[2], value)
    }
}