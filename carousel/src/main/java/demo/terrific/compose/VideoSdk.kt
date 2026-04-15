package demo.terrific.compose

import android.content.Context
import demo.terrific.compose.analytics.TerrificAnalyticsManager
import demo.terrific.compose.analytics.VideoSdkAnalyticsListener
import demo.terrific.compose.network.TerrificAnalyticsApi
import demo.terrific.compose.network.VideoApi
import demo.terrific.compose.repository.VideoRepository
import demo.terrific.compose.repository.VideoRepositoryImpl
import demo.terrific.compose.repository.analytics.TerrificAnalyticsRepository
import demo.terrific.compose.storage.analytics.AnalyticsSessionStorage
import demo.terrific.compose.storage.analytics.SharedPrefsAnalyticsSessionStorage
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
    private lateinit var analyticsManager: TerrificAnalyticsManager
    private lateinit var analyticsSessionStorage: AnalyticsSessionStorage

    @Volatile
    private var analyticsListener: VideoSdkAnalyticsListener? = null

    @Synchronized
    fun ensureInitialized(
        context: Context,
        storeId: String
    ) {
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
        val analyticsApi = retrofit.create(TerrificAnalyticsApi::class.java)

        repository = VideoRepositoryImpl(api)
        likesStorage = SharedPrefsLikesStorage(context.applicationContext)
        pollStorage = SharedPrefsPollStorage(context.applicationContext)

        analyticsSessionStorage =
            SharedPrefsAnalyticsSessionStorage(context.applicationContext)

        val analyticsRepository = TerrificAnalyticsRepository(analyticsApi)

        analyticsManager = TerrificAnalyticsManager(
            storeId = storeId,
            parentUrl = "",
            sessionStorage = analyticsSessionStorage,
            repository = analyticsRepository,
            analyticsListenerProvider = { analyticsListener }
        )

        isInitialized = true
    }


    fun setAnalyticsListener(listener: VideoSdkAnalyticsListener?) {
        analyticsListener = listener
    }

    internal fun repository(): VideoRepository = repository
    internal fun likesStorage(): LikesStorage = likesStorage
    internal fun pollStorage(): PollStorage = pollStorage

    internal fun analytics(): TerrificAnalyticsManager = analyticsManager

}

