package model

import network.RetrofitClientPreAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Tag(val tagId: Int, val tagName: String) {
    companion object {
        private var tags_: ArrayList<String>? = null
        
        private var defaultTags_: String? = null
        
        val tags 
            get() = tags_!!
        
        val defaultTags
            get() = defaultTags_!!
        
        fun initTags() {
            RetrofitClientPreAuth.retrofitCall.getTags().enqueue(object :
                Callback<ArrayList<String>> {
                override fun onResponse(
                    call: Call<ArrayList<String>>,
                    response: Response<ArrayList<String>>
                ) {
                    if (response.code() == 200) {
                        tags_ = response.body()
                        defaultTags_ = "0".repeat(tags.size)
                    } else {
                        println("wrong code on getting all tags")
                    }
                }

                override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                    println("failure on getting all tags")
                }

            })
        }
    }
}
