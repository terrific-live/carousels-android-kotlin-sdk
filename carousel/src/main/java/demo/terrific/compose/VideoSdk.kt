package demo.terrific.compose

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import demo.terrific.compose.analytics.TerrificAnalyticsManager
import demo.terrific.compose.analytics.VideoSdkAnalyticsListener
import demo.terrific.compose.network.TerrificAnalyticsApi
import demo.terrific.compose.network.VideoApi
import demo.terrific.compose.network.interceptor.AnalyticsLoggingInterceptor
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideoSdk {

    private const val MAIN_BASE_URL =
        "https://terrific-staging-polls.web.app/"

    private const val ANALYTICS_BASE_URL =
        "https://us-central1-terrific-deploy.cloudfunctions.net/userEvents/"

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

        val gson = Gson()

        val client = OkHttpClient.Builder()
            .addInterceptor(AnalyticsLoggingInterceptor())
            .build()

        val mainRetrofit = Retrofit.Builder()
            .baseUrl(MAIN_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val analyticsRetrofit = Retrofit.Builder()
            .baseUrl(ANALYTICS_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val videoApi = mainRetrofit.create(VideoApi::class.java)
        val analyticsApi =
            analyticsRetrofit.create(TerrificAnalyticsApi::class.java)

        repository = VideoRepositoryImpl(videoApi)

        likesStorage =
            SharedPrefsLikesStorage(context.applicationContext)

        pollStorage =
            SharedPrefsPollStorage(context.applicationContext)

        analyticsSessionStorage =
            SharedPrefsAnalyticsSessionStorage(context.applicationContext)

        val analyticsRepository =
            TerrificAnalyticsRepository(analyticsApi, "")

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

