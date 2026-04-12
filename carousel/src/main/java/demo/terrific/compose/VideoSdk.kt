package demo.terrific.compose

import android.content.Context
import demo.terrific.compose.network.VideoApi
import demo.terrific.compose.repository.VideoRepository
import demo.terrific.compose.repository.VideoRepositoryImpl
import demo.terrific.compose.storage.likes.LikesStorage
import demo.terrific.compose.storage.likes.SharedPrefsLikesStorage
import demo.terrific.compose.storage.storage.PollStorage
import demo.terrific.compose.storage.storage.SharedPrefsPollStorage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideoSdk {

    @Volatile
    private var isInitialized = false

    private lateinit var repository: VideoRepository
    private lateinit var likesStorage: LikesStorage
    private lateinit var pollStorage: PollStorage

    @Synchronized
    fun ensureInitialized(context: Context) {
        if (isInitialized) return

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            chain.proceed(requestBuilder.build())
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://terrific-staging-polls.web.app/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(VideoApi::class.java)

        repository = VideoRepositoryImpl(api)
        likesStorage = SharedPrefsLikesStorage(context.applicationContext)
        pollStorage = SharedPrefsPollStorage(context.applicationContext)

        isInitialized = true
    }

    internal fun repository(): VideoRepository = repository
    internal fun likesStorage(): LikesStorage = likesStorage
    internal fun pollStorage(): PollStorage = pollStorage

}

