package com.example.sz18_rapidrecall

import java.time.LocalTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Represents a record of a round of the game.
 * @param len the length of the sequence
 * @param target the target sequence
 * @param input the input sequence
 * @param comparison the comparison of the input sequence with the target sequence
 * @param correct whether the input sequence is correct
 * @param time the time of the round*/
class Record @OptIn(ExperimentalTime::class) constructor(
    private val len: Int,
    private val target: Sequence,
    private val input: Sequence,
    private val comparison: IntArray,
    private val correct: Boolean,
    private val time: LocalTime
) {
    fun getLen(): Int {return len}
    fun getTarget(): Sequence {return target}
    fun getInput(): Sequence {return input}
    fun getComparison(): IntArray {return comparison}
    fun getCorrect(): Boolean {return correct}
    @OptIn(ExperimentalTime::class)
    fun getTime(): LocalTime {return time}
}