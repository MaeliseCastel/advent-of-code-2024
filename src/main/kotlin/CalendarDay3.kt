package main.kotlin

import kotlin.text.RegexOption.DOT_MATCHES_ALL

fun calendarDay3() {
    val basicMulRegex = Regex("""mul\((\d+),(\d+)\)""")
    val dontAndDoRegex = Regex("""don't\(\).*?do\(\)""", DOT_MATCHES_ALL)
    val mulInput = {}.javaClass.getResource("/calendar_day_3.txt")!!.readText()

    val dontAndDoRanges = dontAndDoRegex
        .findAll(mulInput)
        .map { it.range }.toList()
    val validMulsByRanges = basicMulRegex.findAll(mulInput).mapNotNull {
        val (stringMultiplier1, stringMultiplier2) = it.destructured
        val mutliplier1 = stringMultiplier1.toInt()
        val mutliplier2 = stringMultiplier2.toInt()

        if (mutliplier1.isAtMost3Digits() && mutliplier2.isAtMost3Digits()) {
            it.range to mutliplier1 * mutliplier2
        } else null
    }.toList()

    val sumOfMuls = validMulsByRanges.mapNotNull { (validMulRange, validMul) ->
        if (validMulRange.first.isContainedInRanges(dontAndDoRanges)) null
        else validMul
    }.sum()

    println(sumOfMuls)
}

private fun Int.isAtMost3Digits(): Boolean = this.toString().length <= 3

private fun Int.isContainedInRanges(ranges: List<IntRange>): Boolean {
    var isContainedInRanges = false
    ranges.forEach { range -> if (range.contains(this)) isContainedInRanges = true }
    return isContainedInRanges
}
