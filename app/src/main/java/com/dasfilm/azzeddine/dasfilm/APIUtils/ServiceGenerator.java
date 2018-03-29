package com.dasfilm.azzeddine.dasfilm.APIUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by azeddine on 3/28/18.
 */

public class ServiceGenerator {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
                                                            .addConverterFactory(GsonConverterFactory.create());
    static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                                                                .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Interceptor apiKeyInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl originalHttpUrl = originalRequest.url();

            HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("api_key","94327dc22a17d2c12b806d241682cd96")
                                        .build();
            Request request = originalRequest.newBuilder().url(url).build();
            return chain.proceed(request);
        }
    };


    public static <S> S createService(Class<S> serviceClass){
        if(!httpClient.interceptors().contains(logging)){
        //    httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        if (!httpClient.interceptors().contains(apiKeyInterceptor)){
            httpClient.addInterceptor(apiKeyInterceptor);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return  retrofit.create(serviceClass);
    }

}
