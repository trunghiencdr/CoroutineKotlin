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

    fun executeLongTask(taskDone: (name: String) -> Unit) { // nhi???m v??? c???a h??m n??y l?? ????? truy???n d??? li???u cho h??m ???????c truy???n v??o,
        // ??? ????y h??m ???????c truy???n v??o c???n bi???n name l?? String n??n th??n h??m l?? h??m.invoke(bi???n)
        // c??n h??m task done th???c hi???n nh?? th??? n??o th?? l??c truy???n v??o m??nh s??? vi???t code
        //=> mu???n l??m g?? th?? truy???n v??o h??m 1 h??m void(b???t k?? h??m n??o) c??n l??y d??? li???u n??o s??i th?? b??? c??i ???? v??o h??m v???a truy???n
        taskDone.invoke("Doing task with this string using thread and callback") // g???i h??m truy???n v??o
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
            // get ???????c userDetail
            textView.text = userDetail
        }
    }

    // coroutine
    fun getTokenAndLogin() {
// launch a coroutine
        GlobalScope.launch {
            val token = getToken() // h??m getToken() n??y ???????c ch???y b???t ?????ng b???
            login(token)           // th??? nh??ng c??ch vi???t code l???i gi???ng nh?? ??ang vi???t code ?????ng b??? (code t??? tr??n xu???ng)
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