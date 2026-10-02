package com.example.sz18_rapidrecall

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Represents a round of the game.
 * It is used to check the input sequence against the target sequence.
 * @param _targetSeq the target sequence
 * @param _targetLen the length of the target sequence*/
class Round(
    private val _targetSeq: Sequence,
    private val _targetLen: Int
) {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalTime::class)
    fun check(
        inputSeq: Sequence,
        records: MutableList<Record>,
    ): Boolean {
        val compare = IntArray(_targetLen)

        for (i in 0 until _targetLen) {
            if (i < inputSeq.seq.size && inputSeq.seq[i] == _targetSeq.seq[i]) {     // it already calls the getter
                compare[i] = 1
            }
        }
        if (compare.count { it == 1}  == _targetLen && inputSeq.seq.size == _targetLen) {
            records.add(Record(_targetLen, _targetSeq, inputSeq, compare, true, LocalTime.now()))
        }
        else {
            records.add(Record(_targetLen, _targetSeq, inputSeq, compare, false, LocalTime.now()))
        }
        return (compare.count { it == 1}  == _targetLen && inputSeq.seq.size == _targetLen)
    }

}
