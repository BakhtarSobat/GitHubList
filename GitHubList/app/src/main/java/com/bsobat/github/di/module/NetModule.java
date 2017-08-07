package com.bsobat.github.di.module;

import android.app.Application;
import android.util.Log;

import com.bsobat.github.utils.LiveDataCallAdapterFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    final private String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public Cache provideHttpCache(Application application) {
        long cacheSize = 10 * 1024 * 1024L;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient(Cache cache, Interceptor interceptor){
        OkHttpClient.Builder client = (new OkHttpClient.Builder().addInterceptor(interceptor));
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public Interceptor provideInterceptor(){
        return (Interceptor)(new Interceptor() {
            public final Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Request.Builder requestBuilder = request.newBuilder();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("format", "json")
                        .build();
                Log.d("URL", url.toString());
                return chain.proceed(request);
            }
        });
    }
}
