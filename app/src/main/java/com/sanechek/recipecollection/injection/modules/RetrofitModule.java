package com.sanechek.recipecollection.injection.modules;

import com.google.gson.Gson;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/* provide retrofit */
@Module
public class RetrofitModule {

    public static final String CONNECTION_URL = "CONNECTION_URL";

    @Provides
    @Singleton
    @Inject
    public Retrofit provideRetrofit(final Gson gson,
                                    @Named(CONNECTION_URL) final String connectionUrl
                                    ){
        HttpLoggingInterceptor consoleLogger = new HttpLoggingInterceptor();
        consoleLogger.setLevel(HttpLoggingInterceptor.Level.BODY);

        SSLSocketFactory socketFactory = getSslContext().getSocketFactory();

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(socketFactory)
                .hostnameVerifier((hostname, session) -> true)
                .addInterceptor(consoleLogger)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        builder.baseUrl(connectionUrl);
        builder.client(client);
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        return builder.build();
    }

    private static SSLContext getSslContext() {
        TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        } };

        SSLContext sslContext=null;

        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext;
    }
}
