/*
 *    Copyright 2018 Makoto Consulting Group Inc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
//@file:JvmName("ObjectGenerator")

package com.makotogo.learn.kotlin.util

import com.makotogo.learn.kotlin.model.Person
import com.makotogo.learn.kotlin.model.Worker
import java.time.LocalDate
import java.time.YearMonth
import java.util.Random

/**
 * Slightly modified version of ObjectGenerator from previous units.
 * This one is evil: when generating an attribute value (say, family name),
 * occasionally the generated value is null.
 *
 * This is to simulate the bad data that you must interact with. Kotlin's
 * null vanguard is awesome, but you will have to interact with applications
 * you didn't write, and that may not be written in a language that has
 * such a conscientious approach to null safety.
 *
 * I've left the class written in Kotlin for two reasons:
 * 1. I'm somewhat lazy - I've tested this code extensively and I know it
 * works. If I translated it to Java I'd be forced to retest (that's just
 * how I roll).
 *
 * 2. This serves as a demonstrate of Java to Kotlin interop
 *
 * 3. This is a Kotlin course, so most of the code *should be* in Kotlin
 *
 * 4. That is three reasons.
 *
 * 5. I apparently cannot count.
 */

/**
 * Java pseudo-random number generator
 */
private var rng = Random(System.currentTimeMillis())

/**
 * Generate a random integer between zero and the [upperBoundExclusive]
 * and return it to the caller.
 */
internal fun generateRandomInt(upperBoundExclusive: Int): Int {
    return (generateRandomNumber() * upperBoundExclusive).toInt()
}

/**
 * Generate and return a random number between zero (inclusive)
 * and one (exclusive)
 */
private fun generateRandomNumber(): Float {
    //
    // Refresh the Pseudo random number generator
    rng = Random()
    return rng.nextFloat()
}

/**
 * A few (weird) family names. Not from around here.
 * As in anywhere on (this) Earth.
 */
private val FAMILY_NAME = arrayOf(
        "Anon",
        "Bazog",
        "Coln",
        "Daon",
        "Engan",
        "Fan",
        "Grale",
        "Horv",
        "Ix",
        "Jaxl",
        "Kath",
        "Lane",
        "Mord",
        "Naen",
        "Oon",
        "Ptal",
        "Tindale",
        "Ugzor",
        "Vahland",
        "Wragdhen",
        "Xntlh",
        "Yagnag",
        "Zhangth")

/**
 * A few (weird) given names. Frankly, I'd give 'em back.
 */
private val GIVEN_NAME = arrayOf(
        "Ag",
        "Bog",
        "Cain",
        "Doan",
        "Erg",
        "Fon",
        "Gor",
        "Heg",
        "In",
        "Jar",
        "Kol",
        "Lar",
        "Mog",
        "Nor",
        "Ojon",
        "Ptal",
        "Quon",
        "Rag",
        "Sar",
        "Thag",
        "Uxl",
        "Verd",
        "Wrog",
        "Xlott",
        "Yogrl",
        "Zelx")

/**
 * Generate a random last name using the FAMILY_NAME array
 * along with a random index into the array.
 */
internal fun generateRandomFamilyName(): String {
    return FAMILY_NAME[generateRandomInt(FAMILY_NAME.size)]
}

/**
 * Generate a random first name using the GIVEN_NAME array
 * along with a random index into the array.
 */
internal fun generateRandomGivenName(): String {
    return GIVEN_NAME[generateRandomInt(GIVEN_NAME.size)]
}

/**
 * Generate a random date of birth between 1950 and 2010.
 */
internal fun generateRandomDateOfBirth(): LocalDate {
    return generateRandomYearMonthDayTriple(1950, 2010).toLocalDate()
}

/**
 * Generate a random date of birth
 */
internal fun generateRandomYearMonthDayTriple(earliestYear: Int = 1000, latestYear: Int = 2100): Triple<Int, Int, Int> {
    val year = generateRandomYear(earliestYear = earliestYear, latestYear = latestYear)
    val month = generateRandomMonth()
    val day = generateRandomDayOfMonth(year, month)

    // Return the YMD components as a Triple object
    return Triple(year, month, day)

}

/**
 * Extension function - extend Triple to convert the Triple to a LocalDate
 * Could we just pass these parameters to LocalDate.of()? Absolutely.
 * But where's the fun in that?? (pun intended)
 */
internal fun Triple<Int, Int, Int>.toLocalDate(): LocalDate =
        LocalDate.of(
                this.first, // Year
                this.second, // Month
                this.third) // Day

/**
 * Generate a random year between [earliestYear] and
 * [latestYear]
 */
private fun generateRandomYear(earliestYear: Int, latestYear: Int): Int {
    return (generateRandomInt(latestYear - earliestYear) + earliestYear)
}

/**
 * Generate a random month between 1-12
 */
private fun generateRandomMonth(): Int {
    return (generateRandomInt(12) + 1)
}

/**
 * Generate a random day of the month, create a
 * [java.time.YearMonth] (using the specified [year] and [month])
 * and its internal TemporalAdjuster to use the correct range
 * for that month (taking into account leap years and all that
 * jazz).
 */
private fun generateRandomDayOfMonth(year: Int, month: Int): Int {
    // Get the last day of the month for the year/month pair
    val maxDayOfMonth = YearMonth.of(year, month).atEndOfMonth().dayOfMonth
    return (generateRandomInt(upperBoundExclusive = maxDayOfMonth) + 1)
}

/**
 * Generate random (fake) tax ID number of the form
 * 000-00-0000.
 */
internal fun generateRandomTaxIdNumber(): String {
    //
    // Simple but (more important) readable
    val part1 = "000"
    val part2 = "00" + generateRandomInt(99).toString()
    val part3 = "0000" + generateRandomInt(9999).toString()

    return "$part1-${part2.substring(part2.length - 2)}-${part3.substring(part3.length - 4)}"
}

/**
 * Create and return a [Worker] object filled with
 * random data.
 */
fun createPerson(): Person {
    return Person(
            generateRandomFamilyName(),
            generateRandomGivenName(),
            generateRandomYearMonthDayTriple().toLocalDate()
    )
}

/**
 * Create and return a [Worker] object filled with
 * random data.
 */
fun createWorker(): Worker {
    return Worker(
            familyName = generateRandomFamilyName(),
            givenName = generateRandomGivenName(),
            dateOfBirth = generateRandomYearMonthDayTriple().toLocalDate(),
            taxIdNumber = generateRandomTaxIdNumber())
}

