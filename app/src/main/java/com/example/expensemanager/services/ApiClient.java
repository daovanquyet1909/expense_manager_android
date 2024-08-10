package com.example.expensemanager.services;

import android.content.Context;
import com.example.expensemanager.utils.SharedPrefManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + SharedPrefManager.getInstance(context).getToken());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            }).build();

            // Sử dụng đúng IP và cổng của server
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://be-expense-asm.onrender.com/api/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
