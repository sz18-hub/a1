package com.example.sz18_rapidrecall

import android.os.Build
import androidx.annotation.RequiresApi

/**
* Handles the user, stores everything related to the user
 * It will generate a new sequence as the target sequence. Then instantiate a Round, check the input sequence against the target sequence.
 * @param currentRoundIndex the current round index
 * @param records the list of records*/
class User(
    var currentRoundIndex: Int = -1,
    val records: MutableList<Record> = mutableListOf<Record>()
) {
    var len = -1
    var round: Round? = null
    var targetSeq: Sequence? = null
    var selectedSeq: Sequence? = null


    fun getRecord(): Record {return records[currentRoundIndex]}
    fun getTargets(): Sequence? {return targetSeq}


    fun genSeq(length: Int) : Sequence {
        // generate a random number of integers between 1 and 10
        // append them to an array
        // return the array/sequence
        val seq = IntArray(length) { (0..9).random() }
        return Sequence(seq)
    }

    fun startRound(selectedLength: Int) {
        currentRoundIndex++
        len = selectedLength
        targetSeq = genSeq(len)
        round = Round(targetSeq!!, len)     // targetSeq can never be null because I just set it
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun doRound(selectedSequence: Sequence) {
        selectedSeq = selectedSequence
        round?.check(selectedSeq!!, records,)
    }
}