package com.example.coroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // test push
//        threadWithCallBack()
//        threadWithCallBacks(User("admin", "123"))
//
//        runBlocking {
//            withContext(Dispatchers.IO){
//                funA()
//            }
//
//            withContext(Dispatchers.IO){
//                funB()
//            }
//
//        }
//
//        CoroutineScope(Dispatchers.IO).launch {
//            var a = funA()
////            funB()
//            println("This is a $a")
//            var b = 0
//            var c = 0
//            launch {
//                b= funB()
//
//            }
//            launch {
//                c=funC()
//            }
//
////            launch {
////
////            }
//            println("this is b + c ${b+c}")
//
//        }
//        CoroutineScope(Dispatchers.IO).launch {
//
//        }
//        coroutineParallel()
        otherCoroutineParallel()


    }

    fun coroutineParallel(){
        // with current thread is main
//        runBlocking {
//            launch {
//                funA()
//            }
//            println("this is two coroutine") // run first in this
//            launch {
//                funB()
//            }
//        }
        // set up other thread
        runBlocking {
            withContext(Dispatchers.IO){
                launch {
                    funA()
                }
                println("this is two coroutine") // run first in this
                launch {
                    funB()
                }
            }
        }
    }

    fun otherCoroutineParallel(){
        runBlocking {
            var a: Deferred<Int> =  async{ funA() }
            println("This is parallel coroutine with async")
            var b: Deferred<Int> = async { funB() }
        }
    }

    sealed class Result()
    class Success : Result()
    class Error : Result()


    suspend fun funA(): Int{
        for(i in 1..10){

            println("funA $i thread: ${Thread.currentThread().name}")
            delay(1000)
        }
        return 10
    }

    suspend fun funB(): Int{
        for(i in 1..10){

            println("funB $i thread: ${Thread.currentThread().name}")
            delay(1000)
        }
        return 20
    }

    suspend fun funC(): Int{
        for(i in 1..10){

            println("funC $i thread: ${Thread.currentThread().name}")
            delay(1000)
        }

        return 30

    }


    fun threadWithCallBack() {
        thread(true) {
            executeLongTask { taskDone ->
                textView.text = taskDone
            }
        }
    }

    fun threadWithCallBacks(user: User) {
        thread(true) {
            register(user)
        }
    }

    fun registerWithRxJava(user: User){

    }

    fun executeLongTask(taskDone: (name: String) -> Unit) { // nhiệm vụ của hàm này là để truyền dữ liệu cho hàm được truyền vào,
        // ở đây hàm được truyền vào cần biến name lá String nên thân hàm là hàm.invoke(biến)
        // còn hàm task done thực hiện như thế nào thì lúc truyền vào mình sẽ viết code
        //=> muốn làm gì thì truyền vào hàm 1 hàm void(bất kì hàm nào) còn lây dữ liệu nào sài thì bỏ cái đó vào hàm vừa truyến
        taskDone.invoke("Doing task with this string using thread and callback") // gọi hàm truyền vào
    }

    fun register(newUser: User) {
        val username = newUser.username
        val password = newUser.password

        registerApi(newUser) { success ->
            if (success) {
                login(username, password)
            }
        }
    }

    private fun loginApi(authData: String, auth: (token: String) -> Unit) {
        if (authData == "granted") {
            auth.invoke("This is token")
        } else {
            auth.invoke("No token is found")
        }
    }

    private fun registerApi(user: User, taskRegister: (success: Boolean) -> Unit) {
        if (user.username == "admin" && user.password == "123") {
            taskRegister.invoke(true)
        } else {
            taskRegister.invoke(false)
        }
    }

    private fun getUserDetailsApi(token: String, findUserDetails: (user: String) -> Unit) {
        if (token == "This is token") {
            findUserDetails.invoke("This is information of user")
        } else {
            findUserDetails.invoke("User not found")
        }
    }

    private fun login(username: String, password: String) {
        loginApi(AuthData(username, password)) { token -> getUserDetail(token) }
    }

    private fun AuthData(username: String, password: String): String {
        if (username == "admin" && password == "123") {
            return "granted"
        } else {
            return "deny"
        }
    }

    private fun getUserDetail(token: String) {
        getUserDetailsApi(token) { userDetail ->
            // get được userDetail
            textView.text = userDetail
        }
    }

    // coroutine
    fun getTokenAndLogin() {
// launch a coroutine
        GlobalScope.launch {
            val token = getToken() // hàm getToken() này được chạy bất đồng bộ
            login(token)           // thế nhưng cách viết code lại giống như đang viết code đồng bộ (code từ trên xuống)
        }
    }

    suspend fun getToken(): String {
        // makes a request and suspends the coroutine
        return suspendCoroutine {
            // handle and return token

            it.resume("AdfGhhafHfjjryJjrtthhhFbgyhJjrhhBfrhghrjjyGHj")
        }
    }

    private fun login(token: String) {
    }
}