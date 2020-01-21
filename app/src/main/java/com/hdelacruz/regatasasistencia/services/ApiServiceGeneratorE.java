package com.hdelacruz.regatasasistencia.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hdelacruz.regatasasistencia.models.ApiError;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;
import com.hdelacruz.regatasasistencia.BuildConfig;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiServiceGeneratorE {
    private static final String TAG = ApiServiceGeneratorE.class.getSimpleName();

    private ApiServiceGeneratorE() {
    }

    private static Picasso picasso;

    public static Picasso createPicasso(final Context context) {
        if(picasso == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request originalRequest = chain.request();

                    // Load Token from SharedPreferences (firsttime is null)
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    String token = sp.getString("token", null);
                    Log.d(TAG, "Loaded Token: " + token);

                    if(token == null){
                        return chain.proceed(originalRequest);
                    }

                    // Request customization: add request headers
                    Request modifiedRequest = originalRequest.newBuilder()
                            .header("Authorization", token)
                            .build();

                    return chain.proceed(modifiedRequest); // Call request with token
                }
            });

            picasso = new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(httpClient.build()))
                    .build();
        }
        return picasso;
    }


//----------------------------------------------------

    private static Retrofit retrofit;

    public static <S> S createService(Class<S> serviceClass) {
        if(retrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS);

            if(BuildConfig.DEBUG) {
                httpClient.addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiServiceEntrada.API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()).build();
        }
        return retrofit.create(serviceClass);
    }
//----------------------------------------------------------------------------
    private static Retrofit retrofitWithAuth;
    public static <S> S createService(final Context context, Class<S> serviceClass) {
        if(retrofitWithAuth == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);

            if(BuildConfig.DEBUG) {
                httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            // Retrofit Token: https://futurestud.io/tutorials/retrofit-token-authentication-on-android
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request originalRequest = chain.request();

                    // Load Token from SharedPreferences (firsttime is null)
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    String token = sp.getString("token", null);
                    Log.d(TAG, "Loaded Token: " + token);

                    if(token == null){
                        return chain.proceed(originalRequest);
                    }

                    // Request customization: add request headers
                    Request modifiedRequest = originalRequest.newBuilder()
                            .header("Authorization", token)
                            .build();

                    return chain.proceed(modifiedRequest); // Call request with token
                }
            });

            retrofitWithAuth = new Retrofit.Builder()
                    .baseUrl(ApiServiceEntrada.API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())// Implementar en
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()).build();
        }
        return retrofitWithAuth.create(serviceClass);
    }
//-------------------------------------------------------------------------------------
    public static ApiError parseError(retrofit2.Response<?> response) {
        try {
            Converter<ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
            return converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError("Error en el servicio");
        }
    }


}
