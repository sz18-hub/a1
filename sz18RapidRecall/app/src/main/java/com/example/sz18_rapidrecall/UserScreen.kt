package com.example.sz18_rapidrecall

import android.R
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.temporal.ChronoUnit
import kotlin.math.round
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
@Composable
fun UserScreen(
    user: User,
    modifier: Modifier,
) {
    var selectedLength: Int by remember { mutableIntStateOf(0) }
    var selectedSequence: Sequence? by remember { mutableStateOf(null) }
    var selectedSequenceList = remember { mutableStateListOf<Int>() }
    var currentRecord: Record? by remember { mutableStateOf(null) }
    var currentRoundIndex: Int by remember { mutableIntStateOf(0) }
    var targetSeq: Sequence? by remember { mutableStateOf(null) }
    var viewSummary: Boolean by remember { mutableStateOf(false) }
    var doCheck: Boolean by remember { mutableStateOf(true) }

    var buttons: Array<Array<String>>

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // title of the game
        Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly) {
            if (selectedLength != 0) {
                Text("CCID: sz18")
                Text(text = "Length: $selectedLength")
                Text("Round ${currentRoundIndex+1}")
                Log.d("UserScreen", "targetSeq: ${targetSeq?.seq?.contentToString()}")
            }
            else {
                Text(
                    text = "Rapid Recall"
                    // TODO: introduction of the rules of the game
                )
            }
        }
        // select the length of the sequence to be generated
        if (selectedLength == 0) {
            buttons = arrayOf(
                arrayOf("1", "2", "3"),
                arrayOf("4", "5", "6"),
                arrayOf("7", "8", "9"),
                arrayOf("10")
            )
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally).fillMaxWidth().padding(top=18.dp, start = 18.dp)) {
                Text(
                    text = "Instruction:",
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
            }
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally).fillMaxWidth().padding(bottom =18.dp, start = 18.dp, end = 18.dp)) {
                Text(
                    text = "This is a memory game where you memorize a sequence of numbers and enter it correctly after selecting the length.",
                    lineHeight = 26.sp,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                )
            }
            Row(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(18.dp)) {
                Text(
                    text = "Please select your length of sequence:",
                    lineHeight = 42.sp,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in buttons) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (button in row) {
                            Button(
                                onClick = {
                                    selectedLength = button.toInt()
                                    user.startRound(selectedLength)
                                    targetSeq = user.getTargets()
                                    currentRoundIndex = user.currentRoundIndex
                                },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(4.dp)
                                    .weight(1f),
                                enabled = button.isNotEmpty(),
                                shape = RoundedCornerShape(16.dp),
                            ) {
                                Box(modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = button,
                                        fontSize = 48.sp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        // start the round get the sequence
        else if (selectedSequence == null) {
            buttons = arrayOf(
                arrayOf("1", "2", "3"),
                arrayOf("4", "5", "6"),
                arrayOf("7", "8", "9"),
                arrayOf("✓", "0", "X")
            )

            Row(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(32.dp)) {
                Text(
                    text = "Your sequence: \n${selectedSequenceList.joinToString(",")}",
                    lineHeight = 42.sp,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                )
            }

            var currentIndex by remember { mutableIntStateOf(0) }
            // animate the buttons
            LaunchedEffect(targetSeq, selectedLength) {
                for (i in 0 until 2*selectedLength+1) {
                    currentIndex = i
                    if (i % 2 == 0) {
                        delay(100L)
                    }
                    else {
                        delay(500L)
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in buttons) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (button in row) {
                            val currIndex = currentIndex/2
                            var isTarget = false
                            if (currentIndex % 2 == 1)
                                isTarget = targetSeq?.seq?.getOrNull((currIndex))?.toString() == button
                            else
                                isTarget = false
                            var run = false
                            if(currIndex == selectedLength) {
                                run = true
                            }
                            Button(
                                onClick = {
                                    if (run) {
                                    when (button) {
                                        "X" -> {
                                            if (selectedSequenceList.isNotEmpty()) {
                                                selectedSequenceList.removeAt(selectedSequenceList.size - 1)
                                            }
                                        }
                                        "✓" -> {
                                            if (selectedSequenceList.isNotEmpty()) {
                                                selectedSequence =
                                                    Sequence(selectedSequenceList.toIntArray())
                                                selectedSequenceList.clear()
                                            }
                                        }
                                        else -> {
                                            selectedSequenceList.add(button.toInt())
                                        }
                                    }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(4.dp)
                                    .weight(1f),
                                enabled = button.isNotEmpty(),
                                shape = RoundedCornerShape(16.dp),
                            ) {
                                Box(modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isTarget or run) button else "",
                                        fontSize = 48.sp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        // do the round, check the answer, and display the feedback
        else {
            Log.d("UserScreen", "selectedSequence: ${selectedSequence?.seq?.contentToString()}")
            if (doCheck) {
                user.doRound(selectedSequence!!)
                doCheck = false
            }
            Log.d("UserScreen", "records: ${user.records.size}")
            currentRecord = user.getRecord()
            // give chance to view summary -> lazyColumn, and restart the game

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // record
                if (viewSummary) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Summary:", fontWeight = FontWeight.Bold)
                        Text("Total number of attempts: ${user.records.size}")
                        Text("Total number of correct attempts: ${user.records.count { it.getCorrect() }}")
                        Text(
                            "Accuracy: ${
                                round(user.records.count { it.getCorrect() }
                                    .toDouble() / user.records.size * 100)
                            }%"
                        )
                    }
                    // display the records
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "Records:",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Text(text = "Pass", Modifier.weight(0.1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            Text(text = "Len", Modifier.weight(0.1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            Text(text = "Target", Modifier.weight(0.25f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            Text(text = "Input", Modifier.weight(0.25f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            Text(text = "Time", Modifier.weight(0.20f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        }

                        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) { items(user.records.size) { index ->
                            val curr = user.records[index]
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                            ) {
                                Text(text = if (curr.getCorrect()) "✓" else "X", Modifier.weight(0.1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text(text = "${curr.getLen()}", Modifier.weight(0.1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text(text = curr.getTarget().seq.contentToString(), Modifier.weight(0.25f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text(text = curr.getInput().seq.contentToString(), Modifier.weight(0.25f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text(text = curr.getTime().toString().take(8), Modifier.weight(0.20f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            }
                        }
                        }
                    }
                }
                else {
                    if (currentRecord!!.getCorrect()) {
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.weight(0.5f)) {}
                        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)){
                            Text(text = "Congradulation!", modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Your answer was correct!", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center , fontSize = 24.sp)
                        }
                        Column (modifier = Modifier.weight(1f).padding(16.dp), ){
                            Text(text = "Target sequence:", modifier = Modifier.fillMaxWidth())
                            Text(text = currentRecord!!.getInput().seq.contentToString(), modifier = Modifier.fillMaxWidth())
                            Text("")
                            Text(text = "Your answer:", modifier = Modifier.fillMaxWidth())
                            Text(text = currentRecord!!.getTarget().seq.contentToString(), modifier = Modifier.fillMaxWidth(), color = Color(0xFF006400))
                        }
                        Row (modifier = Modifier.weight(1f).padding(16.dp), ){
                        }

                    }
                    else {
                        val maxLength = maxOf(currentRecord!!.getTarget().seq.size, currentRecord!!.getInput().seq.size)

                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.weight(0.5f)) {}
                        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)){
                            Text(text = "Oops", modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Your answer was incorrect!", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center , fontSize = 24.sp)
                        }
                        Column (modifier = Modifier.weight(1f).padding(16.dp), ){
                            Text(text = "Target sequence:", modifier = Modifier.fillMaxWidth())
                            Text(text = currentRecord!!.getTarget().seq.contentToString(), modifier = Modifier.fillMaxWidth())
                            Text("")
                            Text(text = "Your answer:", modifier = Modifier.fillMaxWidth())
                            Row(modifier = Modifier.fillMaxWidth()) {
                                for (i in 0 until maxLength) {
                                    val userChar = currentRecord!!.getInput().seq.getOrNull(i)
                                    val targetChar = currentRecord!!.getTarget().seq.getOrNull(i)
                                    val (text, color) = when (userChar) {
                                        targetChar -> Pair(userChar.toString(), Color(0xFF006400))
                                        null -> Pair("_", Color.Gray)
                                        else -> Pair(userChar.toString(), Color.Red)
                                    }
                                    when (i) {
                                        targetSeq!!.seq.size -> {

                                        }
                                        0 -> {
                                            Text("[")
                                        }
                                        else -> {
                                            Text(", ")
                                        }
                                    }
                                    Text(
                                        text = text,
                                        color = color,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (i == targetSeq!!.seq.size-1) Text("] ")
                                }
                            }
                        }


                        Row (modifier = Modifier.weight(1f).padding(16.dp), ){}
                    }
                }
            }

            Row(
                modifier = Modifier.weight(0.15f)
            ) {
                if (!viewSummary) Button(onClick = { viewSummary = true } ,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),) { Text("View Summary") }
                else Button(onClick = {viewSummary = false},
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),) { Text("Close") }

                Button(onClick = {
                    // initialize the variables
                    targetSeq = null
                    selectedLength = 0
                    selectedSequence = null
                    currentRoundIndex = user.currentRoundIndex
                    currentRecord = user.getRecord()
                    viewSummary = false
                    doCheck = true
                },
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text("Start New Round")
                }
            }


        }
    }
}
