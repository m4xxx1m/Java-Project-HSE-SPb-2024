package model

import androidx.compose.runtime.mutableStateOf
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriberManager(private val userId: Int) {
    private val thisUserId = AuthManager.currentUser.id

    val isSubscribed = mutableStateOf(false)
    
    init {
        checkSubscription()
    }

    private fun checkSubscription() {
        RetrofitClient.retrofitCall.checkSubscription(thisUserId, userId)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.code() == 200) {
                        response.body()?.let { 
                            isSubscribed.value = it
                        }
                    } else {
                        println("wrong code on getting subscription status")
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    println("failure while getting subscription status")
                }
            })
    }

    private fun subscribe() {
        RetrofitClient.retrofitCall.subscribe(thisUserId, userId).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.code() == 200) {
                    checkSubscription()
                } else {
                    println("wrong code on subscribing")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println("failure while subscribing")
            }
        })
    }

    private fun unsubscribe() {
        RetrofitClient.retrofitCall.unsubscribe(thisUserId, userId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        checkSubscription()
                    } else {
                        println("wrong code on subscribing")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    println("failure while subscribing")
                }
            })
    }

    fun changeSubscription() {
        if (isSubscribed.value) {
            unsubscribe()
        } else {
            subscribe()
        }
    }
}