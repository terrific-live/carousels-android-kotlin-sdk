package demo.terrific.compose

import AnalyticsEventMapper
import android.content.Context
import com.google.gson.Gson
import demo.terrific.compose.analytics.VideoAnalytics
import demo.terrific.compose.analytics.VideoSdkAnalyticsListener
import demo.terrific.compose.network.TerrificAnalyticsApi
import demo.terrific.compose.network.VideoApi
import demo.terrific.compose.network.interceptor.AnalyticsLoggingInterceptor
import demo.terrific.compose.repository.VideoRepository
import demo.terrific.compose.repository.VideoRepositoryImpl
import demo.terrific.compose.storage.analytics.AnalyticsSessionStorage
import demo.terrific.compose.storage.analytics.SharedPrefsAnalyticsSessionStorage
import demo.terrific.compose.storage.likes.LikesStorage
import demo.terrific.compose.storage.likes.SharedPrefsLikesStorage
import demo.terrific.compose.storage.storage.PollStorage
import demo.terrific.compose.storage.storage.SharedPrefsPollStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideoSdk {

    private const val MAIN_BASE_URL =
        "https://terrific-live-polls.web.app/"

    private const val ANALYTICS_BASE_URL =
        "https://us-central1-terrific-deploy.cloudfunctions.net/"

    @Volatile
    private var isInitialized = false

    private lateinit var repository: VideoRepository
    private lateinit var likesStorage: LikesStorage
    private lateinit var pollStorage: PollStorage
    private lateinit var analyticsSessionStorage: AnalyticsSessionStorage
    private lateinit var analyticsInstance: VideoAnalytics

    @Volatile
    private var analyticsListener: VideoSdkAnalyticsListener? = null

    val analytics: VideoAnalytics
        get() {
            check(::analyticsInstance.isInitialized) {
                "VideoSdk is not initialized. Call ensureInitialized() first."
            }

            return analyticsInstance
        }

    @Synchronized
    fun ensureInitialized(
        context: Context,
        storeId: String
    ) {
        if (isInitialized) return

        val applicationContext = context.applicationContext
        val gson = Gson()

        val mainClient = OkHttpClient.Builder()
            .build()

        val analyticsClient = OkHttpClient.Builder()
            .addInterceptor(AnalyticsLoggingInterceptor())
            .build()

        val mainRetrofit = Retrofit.Builder()
            .baseUrl(MAIN_BASE_URL)
            .client(mainClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val analyticsRetrofit = Retrofit.Builder()
            .baseUrl(ANALYTICS_BASE_URL)
            .client(analyticsClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val videoApi = mainRetrofit.create(VideoApi::class.java)

        val analyticsApi = analyticsRetrofit.create(
            TerrificAnalyticsApi::class.java
        )

        repository = VideoRepositoryImpl(videoApi)
        likesStorage = SharedPrefsLikesStorage(applicationContext)
        pollStorage = SharedPrefsPollStorage(applicationContext)

        analyticsSessionStorage =
            SharedPrefsAnalyticsSessionStorage(applicationContext)

        val analyticsMapper = AnalyticsEventMapper(
            externalUserId = {
                analyticsSessionStorage.getOrCreateUserId()
            },
            sessionIdProvider = {
                analyticsSessionStorage.getOrCreateSessionId(storeId)
            },
            storeIdProvider = {
                storeId
            }
        )

        analyticsInstance = VideoAnalytics(
            storeId = storeId,
            api = analyticsApi,
            mapper = analyticsMapper,
            scope = CoroutineScope(
                SupervisorJob() + Dispatchers.IO
            )
        )

        isInitialized = true
    }

    fun setAnalyticsListener(
        listener: VideoSdkAnalyticsListener?
    ) {
        analyticsListener = listener
    }

    internal fun repository(): VideoRepository {
        check(isInitialized)
        return repository
    }

    internal fun likesStorage(): LikesStorage {
        check(isInitialized)
        return likesStorage
    }

    internal fun pollStorage(): PollStorage {
        check(isInitialized)
        return pollStorage
    }
}