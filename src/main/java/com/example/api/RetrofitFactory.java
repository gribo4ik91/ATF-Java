package com.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RetrofitFactory {

    private static final int DEFAULT_TIMEOUT = 40;

    @Autowired
    private ObjectMapper jsonMapper;


    class Builder {

        private Map<String, String> headers;

        private String url;

        private OkHttpClient client;

        private int timeout;

        Builder(final String url) {
            this.url = url;
            headers = new HashMap<>();
        }


        Builder headers(final Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }


        Retrofit build() {

            timeout = timeout > 0 ? timeout : DEFAULT_TIMEOUT;

            if (Objects.isNull(client)) {
                var logInterceptor = new HttpLoggingInterceptor(message -> {
                    if (log.isDebugEnabled()) {
                        log.debug(message);
                    } else {
                        log.info(message);
                    }
                });
                if (log.isDebugEnabled()) {
                    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                } else {
                    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                }

                client = new OkHttpClient.Builder()
                        .addInterceptor(chain -> chain.proceed(
                                chain.request().newBuilder()
                                        .headers(chain.request().headers().newBuilder().addAll(Headers.of(headers)).build())
                                        .build()
                        ))
                        .addInterceptor(logInterceptor)
                        .readTimeout(timeout, TimeUnit.SECONDS)
                        .writeTimeout(timeout, TimeUnit.SECONDS)
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .build();
            }

            return new Retrofit.Builder()
                    .client(client)
                    .baseUrl(url)
                    .addConverterFactory(JacksonConverterFactory.create(jsonMapper))
                    .build();
        }

    }

}



