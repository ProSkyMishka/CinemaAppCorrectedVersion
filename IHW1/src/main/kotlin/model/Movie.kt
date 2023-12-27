package model

class Movie(name: String, duration: Int): Entity() {
    private var name: String
    private var duration: Int

    init {
        this.name = name
        this.duration = duration
        values.add(this.name)
        values.add(this.duration.toString())
    }
}