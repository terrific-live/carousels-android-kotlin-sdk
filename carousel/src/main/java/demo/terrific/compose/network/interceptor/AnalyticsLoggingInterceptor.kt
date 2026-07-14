package demo.terrific.compose.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

internal class AnalyticsLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!request.url.toString().contains("userEvents")) {
            return chain.proceed(request)
        }

        val body = request.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        }

        Log.d("Analytics", "========== REQUEST ==========")
        Log.d("Analytics", "URL: ${request.url}")
        Log.d("Analytics", "Method: ${request.method}")
        Log.d("Analytics", "Headers:\n${request.headers}")
        Log.d("Analytics", "Body:\n$body")

        val response = chain.proceed(request)

        val responseBody = response.peekBody(Long.MAX_VALUE).string()

        Log.d("Analytics", "========== RESPONSE ==========")
        Log.d("Analytics", "Code: ${response.code}")
        Log.d("Analytics", "Success: ${response.isSuccessful}")
        Log.d("Analytics", "Body: $responseBody")

        return response
    }
}