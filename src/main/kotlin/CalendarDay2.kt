package main.kotlin

import kotlin.math.abs
import kotlin.math.sign

fun calendarDay2() {
    val csvFile = {}.javaClass.getResource("/calendar_day_2.txt")!!.readText()
    var safeReports = 0
    csvFile.lines().forEach { line ->
        val levels = line.split(",").map { it.toInt() }
        val safeReport1 = levels.hasOnlyOneDirection() && levels.differenceIsAtMostThree()
        var safeReport2 = false
        levels.forEachIndexed { index, _ ->
            val copy = levels.toMutableList()
            copy.removeAt(index)
            if (copy.hasOnlyOneDirection() && copy.differenceIsAtMostThree()) {
                safeReport2 = true
            }
        }

        val safeReport = safeReport1 || safeReport2
        if (safeReport) {
            safeReports++
        }
    }
    println(safeReports)
}

private fun List<Int>.hasOnlyOneDirection(): Boolean {
    if (this.size < 2) return true
    var index = 2
    val direction: Int = (this[0] - this[1]).sign
    if (direction == 0) return false
    while (index < this.size) {
        if (direction != (this[index - 1] - this[index]).sign) return false
        index++
    }
    return true
}

private fun List<Int>.differenceIsAtMostThree(): Boolean {
    if (this.size < 2) return true
    var index = 1
    while (index < this.size) {
        if (abs(this[index - 1] - this[index]) > 3) return false
        index++
    }
    return true
}
