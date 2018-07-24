package studio.forface.bottomappbar

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class RxTests {

    companion object {
        @JvmStatic @BeforeClass fun setup() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }


            RxJavaPlugins.reset()
            RxAndroidPlugins.reset()
        }
    }

    @Test
    fun interval() {
        val duration = 500L
        val period = 20L
        val steps = duration / period
        val result = Observable.interval( period, TimeUnit.MILLISECONDS )
                .map { it + 1 }
                .map { 1f / steps * it }
                .take( steps )
                .blockingIterable()

        println( result.joinToString { it.toString() } )
    }

}