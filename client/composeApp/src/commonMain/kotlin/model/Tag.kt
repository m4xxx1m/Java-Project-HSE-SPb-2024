package model


import kotlinx.coroutines.delay
import network.RetrofitClientPreAuth

data class Tag(val tagId: Int, val tagName: String) {
    companion object {
        private var tags_: ArrayList<String>? = null
        
        private var defaultTags_: String? = null
        
        val tags 
            get() = tags_!!
        
        val defaultTags
            get() = defaultTags_!!
        
        suspend fun initTags() {
//            RetrofitClientPreAuth.retrofitCall.getTags().enqueue(object :
//                Callback<ArrayList<String>> {
//                override fun onResponse(
//                    call: Call<ArrayList<String>>,
//                    response: Response<ArrayList<String>>
//                ) {
//                    if (response.code() == 200) {
//                        tags_ = response.body()
//                        defaultTags_ = "0".repeat(tags.size)
//                    } else {
//                        println("wrong code on getting all tags")
//                        initTags()
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
//                    println("failure on getting all tags")
//                    initTags()
//                }
//
//            })
            while (true) {
                try {
                    val response = RetrofitClientPreAuth.retrofitCall.getTags().execute()
                    if (response.code() == 200) {
                        tags_ = response.body()
                        defaultTags_ = "0".repeat(tags.size)
                        break
                    }
                    println("wrong code on getting tags")
                } catch (_: Throwable) {
                    println("error on getting tags")
                }
                delay(1000)
            }
        }
    }
}
