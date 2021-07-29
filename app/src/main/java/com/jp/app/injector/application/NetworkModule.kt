package com.jp.app.injector.application

import android.content.Context
import com.jp.app.SampleApplication
import com.jp.data.network.gateway.IAppAuthGateway
import com.jp.data.network.gateway.IAppGateway
import com.jp.data.preferences.CredentialsPreferenceManager
import com.jp.data.preferences.URLPreferenceManager
import com.jp.data.network.gateway.retrofit.AppAuthGateway
import com.jp.data.network.gateway.retrofit.AppGateway
import com.jp.data.network.gateway.retrofit.authenticator.RefreshAuthenticator
import com.jp.data.network.gateway.retrofit.callAdapter.RxErrorHandlingCallAdapterFactory
import com.jp.data.network.gateway.retrofit.interceptor.HttpCacheInterceptor
import com.jp.data.network.gateway.retrofit.interceptor.RequestAuthInterceptor
import com.jp.data.network.gateway.retrofit.interceptor.RequestInterceptor
import com.jp.data.network.gateway.retrofit.service.IAuthRestServices
import com.jp.data.network.gateway.retrofit.service.IRestServices
import com.jp.app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val HTTP_CACHE_MAX_AGE = 60 * 10              // read from cache for 10 minutes
    private const val HTTP_CACHE_MAX_STALE = 60 * 60 * 24 * 28  // tolerate 4-weeks stale
    private const val CONNECT_TIME_OUT = 30.toLong()
    private const val READ_TIME_OUT = 2.toLong()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }


    @Singleton
    @Provides
    fun provideRequestInterceptor(@ApplicationContext context: Context): RequestInterceptor {
        return RequestInterceptor(context)
    }


    @Provides
    @Singleton
    fun cacheInterceptor(@ApplicationContext context: Context): HttpCacheInterceptor {
        val httpCacheInterceptor = HttpCacheInterceptor(context)
        httpCacheInterceptor.cacheMaxAge = HTTP_CACHE_MAX_AGE
        httpCacheInterceptor.cacheMaxStale = HTTP_CACHE_MAX_STALE
        return httpCacheInterceptor
    }


    @Singleton
    @Provides
    fun provideRequestAuthInterceptor(@ApplicationContext context: Context, credentialsPreferenceManager: CredentialsPreferenceManager): RequestAuthInterceptor {
        return RequestAuthInterceptor(context, credentialsPreferenceManager)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            httpCacheInterceptor: HttpCacheInterceptor,
            requestInterceptor: RequestInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(httpCacheInterceptor)
                .addInterceptor(requestInterceptor)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)

        return builder.build()
    }


    @Singleton
    @Provides
    @Named("Auth")
    fun provideAuthOkHttpClient(
            application: SampleApplication,
            httpLoggingInterceptor: HttpLoggingInterceptor,
            httpCacheInterceptor: HttpCacheInterceptor,
            requestAuthInterceptor: RequestAuthInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(httpCacheInterceptor)
                .addInterceptor(requestAuthInterceptor)
                .authenticator(RefreshAuthenticator(application))
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)

        return builder.build()
    }


    // RetrofitService =============================================================================


    @Singleton
    @Provides
    fun provideRestServices(okHttpClient: OkHttpClient): IRestServices {
        val restAdapter = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        return restAdapter.create(IRestServices::class.java)
    }


    @Singleton
    @Provides
    fun provideAuthRestServices(@Named("Auth") okHttpClient: OkHttpClient): IAuthRestServices {
        val restAdapter = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return restAdapter.create(IAuthRestServices::class.java)
    }


    // Gateway =====================================================================================

    /**
     * If the app is set to NOT read from SERVICES or is launch to run test Build.FINGERPRINT == "robolectric"
     * then use AppMockGateway
     */


    @Provides
    @Singleton
    internal fun appGateway(service: IRestServices, urlPreferenceManager: URLPreferenceManager): IAppGateway {
        return AppGateway(service, urlPreferenceManager)
    }


    @Provides
    @Singleton
    internal fun appAuthGateway(service: IAuthRestServices, urlPreferenceManager: URLPreferenceManager): IAppAuthGateway {
        return AppAuthGateway(service, urlPreferenceManager)

    }
}
