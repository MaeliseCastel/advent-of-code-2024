package main.kotlin

import com.google.gson.Gson
import kotlin.math.abs

fun calendarDay1() {
    val jsonData = {}.javaClass.getResource("/calendar_day_1.json")!!.readText()
    val gson = Gson()
    val calendarDay1Input = gson.fromJson(jsonData, CalendarDay1Input::class.java)

    val locationIds1 = calendarDay1Input.locationIds1.sorted()
    val locationIds2 = calendarDay1Input.locationIds2.sorted()

    var distance = 0
    var similarityScore = 0
    locationIds1.forEachIndexed { index, locationId1 ->
        distance += abs(locationId1 - locationIds2[index])
        similarityScore += locationIds2.count { it == locationId1 } * locationId1
    }
    println("Distance: $distance")
    println("Similarity score: $similarityScore")
}

private class CalendarDay1Input(val locationIds1: List<Int>, val locationIds2: List<Int>)
