package ch3_getting_started_with_coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    startFirstCoroutine()
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
        GlobalScope.launch {
            val threadName = Thread.currentThread().name
            println("$it printed on Thread ${threadName}")
        }
        Thread.sleep(1000)
    }
}