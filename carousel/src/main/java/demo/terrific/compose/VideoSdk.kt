package demo.terrific.compose

import demo.terrific.compose.network.VideoApi
import demo.terrific.compose.repository.VideoRepository
import demo.terrific.compose.repository.VideoRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VideoSdk {

    @Volatile
    private var isInitialized = false

    private lateinit var config: VideoSdkConfig
    private lateinit var retrofit: Retrofit
    private lateinit var api: VideoApi
    private lateinit var repository: VideoRepository

    fun initialize(config: VideoSdkConfig) {

        if (config.baseUrl.isEmpty()) {
            this.config = VideoSdkConfig("https://terrific-staging-polls.web.app/")
        } else {
            this.config = config
        }

        retrofit = Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val requestBuilder = chain.request().newBuilder()
                        chain.proceed(requestBuilder.build())
                    }
                    .build()
            )
            .build()

        api = retrofit.create(VideoApi::class.java)
        repository = VideoRepositoryImpl(api)
        isInitialized = true
    }

    fun repository(): VideoRepository {
        check(isInitialized) {
            "VideoSdk is not initialized. Call VideoSdk.initialize(...) first."
        }
        return repository
    }
}

