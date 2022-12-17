package ch3_getting_started_with_coroutine

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

fun main() {
//    startFirstCoroutine()
//    explainingJobs()
    dependentJobs()
}

/*
    This is launching my first coroutine with loop from 1 to 10000 which
    most program would get an OutOfMemoryException
    but for the coroutine able to launch a large number of 10000 thread without any performance impact
    it have 2 important in this function about launching coroutine
        1.block of code passed as the parameter to launch(), which is called s "coroutine builder"
        2.GlobalScope provide to coroutine because they are background mechanisms which don't care about lifecycle
          sometime program finish before coroutine completed. It this function use GlobalScope the coroutine lifecycle
          is bound to the lifecycle of the application
 */
fun startFirstCoroutine() {
    (1..10000).forEach {
        val scope = CoroutineScope(EmptyCoroutineContext)
        GlobalScope.launch {
            val threadName = Thread.currentThread().name
            println("$it printed on Thread ${threadName}")
        }
        Thread.sleep(1000)
    }
}

/* Job
   use to handle queue of coroutine builder's execute
   Job have state
        - New state is always in this state when start coroutine
        - Active job will move to this state directly from New State unless define start coroutine with LAZY
            it will move to Active when invoke start() or join()
        - Completing wait for coroutine child completed
        - Completed all coroutine child completed
        - cancelling invoke cancel or some coroutine child fail
            * if want to move Completed state when some child are not completed, it use SupervisorJob
        - Canceled

 */
fun explainingJobs() {
    // Parent Job
    GlobalScope.launch {
        // Child Job
        val jobA: Job = GlobalScope.launch { }
        val jobB: Job = GlobalScope.launch {
            val childJobB = launch { }

        }
        jobA.isActive
        jobA.cancel()
        jobB.join()
    }
}

/* Dependent Jobs
    Define jobPong start coroutine Lazy mode
    jobPong will execute when invoke join or start
    so when jobPong.join() next line will be execute until execute of jobPong completed
    Expect output
    PING
    PONG
    PING
 */
fun dependentJobs() {
    val jobPong = GlobalScope.launch(start = CoroutineStart.LAZY) {
        delay(200)
        println("PONG")
        delay(200)
    }

    GlobalScope.launch {
        delay(200)
        println("PING")
        println("Job pong status ${jobPong.isCompleted}")
        jobPong.join()
        println("Job pong status ${jobPong.isCompleted}")
        println("PING")
        delay(200)
    }

    Thread.sleep(1000)
}