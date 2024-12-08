package main.kotlin

import java.util.*
import kotlin.math.min

fun calendarDay5() {
    val rulesAndUpdates = {}.javaClass.getResource("/calendar_day_5.txt")!!.readText()
        .split("\n\n")
    val rules = rulesAndUpdates[0].split("\n")
    val pagesThatMustBeAfterByPage = mutableMapOf<Int, MutableList<Int>>()
    rules.map { rule ->
        val pages = rule.split("|")
        val page1 = pages[0].toInt()
        val page2 = pages[1].toInt()
        pagesThatMustBeAfterByPage.getOrPut(page1) { mutableListOf() }.add(page2)
    }

    val updates = rulesAndUpdates[1].split("\n")
    val sumOfValidUpdatesMiddlePages = updates.mapNotNull { update ->
        val pagesToUpdate = update.split(",").map { it.toInt() }
        if (isValid(pagesToUpdate, pagesThatMustBeAfterByPage)) {
            getMiddlePage(pagesToUpdate)
        } else null
    }.sum()

    println(sumOfValidUpdatesMiddlePages)

    val invalidUpdates = updates.mapNotNull { update ->
        val pagesToUpdate = update.split(",").map { it.toInt() }
        if (!isValid(pagesToUpdate, pagesThatMustBeAfterByPage)) {
            pagesToUpdate
        } else null
    }

    val toValidUpdates: List<List<Int>> = invalidUpdates.map { invalidUpdate ->
        sortToValid(invalidUpdate, pagesThatMustBeAfterByPage)
    }

    val sumOfInvalidUpdatesMiddlePages = toValidUpdates.sumOf { update -> getMiddlePage(update) }

    println(sumOfInvalidUpdatesMiddlePages)
}

private fun isValid(pagesToUpdate: List<Int>, rules: Map<Int, List<Int>>): Boolean {
    var isValid = true
    pagesToUpdate.forEachIndexed { index, currentPage ->
        val specificRules = rules[currentPage]
        if (specificRules != null) {
            val pagesBeforeCurrentPage = pagesToUpdate.subList(0, index)
            if (!isUpdateRespectingRules(pagesBeforeCurrentPage, specificRules)) {
                isValid = false
            }
        }
    }
    return isValid
}

private fun getMiddlePage(pages: List<Int>): Int = pages[pages.size / 2]


private fun isUpdateRespectingRules(pagesBefore: List<Int>, rules: List<Int>): Boolean {
    var respectsRules = true
    rules.forEach { pageThatMustBeAfter ->
        if (pageThatMustBeAfter in pagesBefore) {
            respectsRules = false
        }
    }
    return respectsRules
}

private fun sortToValid(invalidUpdate: List<Int>, rules: Map<Int, List<Int>>): List<Int> {
    val mutableList = invalidUpdate.toMutableList()
    var index = 0
    while (index < invalidUpdate.size) {
        val currentPage = mutableList[index]
        val specificRules = rules[currentPage]
        if (specificRules == null) {
            index++
            continue
        }
        val pagesBeforeCurrentPage = mutableList.subList(0, index)
        var earliestError = index
        specificRules.forEach { pageThatMustBeAfter ->
            if (pageThatMustBeAfter in pagesBeforeCurrentPage) {
                earliestError = min(earliestError, pagesBeforeCurrentPage.indexOf(pageThatMustBeAfter))
            }
        }
        if (index != earliestError) {
            Collections.swap(mutableList, index, earliestError)
        } else index++
    }
    return mutableList
}
