package com.example.api;

import com.example.api.models.Contact;
import com.example.api.models.LoginResponse;
import com.example.api.models.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {

    // Add Contact
    @POST("/contacts")
    Call<Contact> addContact(@Body Contact contact);

    // Get Contact List
    @GET("/contacts")
    Call<List<Contact>> getContactList();

    // Add User
    @POST("/users")
    Call<LoginResponse> addUser(@Body User user);
    // Log In User
    @POST("/users/login")
    Call<LoginResponse> loginUser(@Body User user);

    // Log Out User
    @POST("/users/logout")
    Call<Void> logoutUser();

    // Delete User
    @DELETE("/users/me")
    Call<Void> deleteUser();
}
