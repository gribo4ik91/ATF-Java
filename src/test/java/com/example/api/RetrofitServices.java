package com.example.api;

import com.example.api.models.LoginResponse;
import com.example.global.GlobalMap;
import okhttp3.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import retrofit2.Response;

import java.util.Map;

import static com.example.global.GlobalMapKey.HTTP_RESPONSE_BODY;
import static javax.swing.text.DefaultStyledDocument.ElementSpec.ContentType;

public class RetrofitServices extends RetrofitFactory {

    @Autowired
    private Environment environment;

    @Value("${add.contact.token}")
    private String token;

    @Value("${add.user.token}")
    private String addUserToken;

    public ApiService getAuthorizationService() {
        return new Builder(environment.getProperty("home.page.url"))
                .headers(Map.of( "Content-Type", "application/json",
                        "Authorization", token))
                .build()
                .create(ApiService.class);
    }

    public ApiService getAuthorizationServiceForAddUser() {
        return new Builder(environment.getProperty("home.page.url"))
                .headers(Map.of( "Content-Type", "application/json",
                        "Authorization", addUserToken))
                .build()
                .create(ApiService.class);
    }

    public ApiService getAuthorizationServiceWithNewToken() {
        LoginResponse newToken = (LoginResponse) GlobalMap.getInstance().get(HTTP_RESPONSE_BODY);
        return new Builder(environment.getProperty("home.page.url"))
                .headers(Map.of(
                        "Authorization", "Bearer " + newToken.getToken()))
                .build()
                .create(ApiService.class);
    }

}
