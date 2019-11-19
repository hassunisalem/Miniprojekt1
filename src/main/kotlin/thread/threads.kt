package thread

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun threadsCount(limit: Int, step: Int)
{
    for (i in 1.. limit) thread {
        Thread.sleep(100)
        if(i%step == 0) print("T$i ")
    }
    println()
}

fun doCoroutines(limit: Int, Step: Int)
{
    runBlocking {
        for (i in 1..limit) {
            launch {
                delay(100)
                if(i%Step == 0) print("A$i ")
            }
        }
        println()
    }
}

fun  main(){
    doCoroutines(10000, 1000)
    threadsCount(10000000, 1000)
}