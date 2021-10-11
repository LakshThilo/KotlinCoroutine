package com.example.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TAG = "MainActivity: "

        /** 1. --->>> */
     /*  GlobalScope.launch(Dispatchers.IO) {

          val fromNetwork = networkCall()
          val dbCall = dbCall()

          Log.d(TAG, networkCall())
          Log.d(TAG, dbCall())
        }*/


        /** 2. --->>> Switching Context between thread */
        /*
     GlobalScope.launch(Dispatchers.IO) {

       val answer = networkCall()
       val answer2 = dbCall()



         withContext(Dispatchers.Main){
             tv_dummyText.text = answer
         }

         delay(2000L)

         withContext(Dispatchers.Main){
             tv_dummyText.text = answer2
         }*/


        /** 3. --->>> runBlocking */
        /*   runBlocking { // this will block the main thread
               //delay(3000L) // behave as like Thread.sleep()
               Log.d(TAG, " Start IO Coroutine 1")
               launch(Dispatchers.IO) {
                   delay(3000L)
                   val text: String = ""
                   Log.d(TAG, networkCall())
               }

               Log.d(TAG, " Start IO Coroutine 2")
               launch(Dispatchers.IO) {
                   delay(3000L)
                   val text: String = ""
                   Log.d(TAG, dbCall())

               }
           }*/

        /** 4. --->> Jobs - wait for the and cancel them -- when ever we launching a Coroutine it will return job*/
       /* val job = GlobalScope.launch(Dispatchers.Default) {

            repeat(5) {
                Log.d(TAG, "Coroutine is still running")
                delay(1000L)
            }

        }

        runBlocking { // run in main Thread
            job.join()  // this will block main thread until this Coroutine(job) finish if it is finish it will execute after lines
            Log.d(TAG, "Main Thread is continuing ")

        }*/

        /** 5. --->> Jobs - canceling and check isActive*/
    /*    val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Start long running Calculation ")
                for (i in 30..40) {
                    if(isActive) {
                        Log.d(TAG, "Result for i $i: ${fib(i)} ")

                }
            }
            Log.d(TAG, "Ending long running calculation.. ")
        }

        runBlocking { // run in main Thread
            delay(2000L) // waiting 2s
            job.cancel() // after 2s cancel the job
            Log.d(TAG, "Cancel the job!!")

        }*/

        /** 6. --->> Jobs - withTime out --= is equivalent to starting new Coroutine and delaying in it 2s and simply cancel that job here */
        /*val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Start long running Calculation ")
            withTimeout(3000L){ //cancel automatically after 3s

                for (i in 30..40) {
                    if(isActive) {
                        Log.d(TAG, "Result for i $i: ${fib(i)} ")

                    }
                }
            }

            Log.d(TAG, "Ending long running calculation.. ")
        }*/

        /** 7. --->> Async and await function- when we want do 2 network calls at same time and don't want to wait for the other one*/
        // this process will take 6s to complete to solve this got two approaches
        GlobalScope.launch(Dispatchers.IO) {

            val time = measureTimeMillis {

               /*
                val newtworkCall = networkCall()
                val dbCall = dbCall()

                Log.d(TAG, "Data from $newtworkCall")
                Log.d(TAG, "Data from $dbCall")
                */

                //approach 1 - this approach take only 3s as we expected but there is better way

               /* var answer1: String? = null
                var answer2: String? = null

                val job1 = launch { answer1 = networkCall() }
                val job2 = launch { answer2 = dbCall() }
                job1.join()
                job2.join()

                Log.d(TAG, "Data from $answer1")
                Log.d(TAG, "Data from $answer2")*/

                //approach 2 async  --->>> we should always use async if you want use Coroutine that return some kind of result
                var asyncAnswer1 = async { networkCall() }
                var asyncAnswer2 = async { dbCall() }

                Log.d(TAG, "Data from ${asyncAnswer1.await()}") // await() will block the main thread until answer is available
                Log.d(TAG, "Data from ${asyncAnswer2.await()}")

            }

            Log.d(TAG, "Request took $time ms ")

        }
    }

    private fun fib(n: Int): Long {

        return if(n == 0) 0
        else if(n == 1) 1
        else fib(n -1 ) + fib(n - 2)
    }


    private suspend fun networkCall():String{
        delay(3000L)
        return "network"
    }

    private suspend fun dbCall():String{
        delay(3000L)
        return "bd"
    }
}