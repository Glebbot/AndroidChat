package com.example.androidchat.retrofit;

import com.example.androidchat.model.Channel;
import com.example.androidchat.model.ChatMessage;
import com.example.androidchat.model.LoginUser;
import com.example.androidchat.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @GET("/testProject_war_exploded/users/all")
    Call<List<User>> getAllUsers();
    @POST("/testProject_war_exploded/users/add")
    Call<User> save(@Body User user);
    @POST("/testProject_war_exploded/users/login")
    Call<Boolean> saveLogin(@Body LoginUser loginUser);
    @POST("/testProject_war_exploded/channels/add")
    Call<Boolean> saveChannel(@Body Channel channel);
    @POST("/testProject_war_exploded/channels/enter")
    Call<Boolean> enterChannel(@Body Channel channel);
    @POST("/testProject_war_exploded/channels/user_channels")
    Call<List<Channel>> getUserChannel(@Body User user);
    @POST("/testProject_war_exploded/messages/channel_messages")
    Call<List<ChatMessage>> getMessages(@Body Channel channel);
    @POST("/testProject_war_exploded/messages/save")
    Call<Boolean> sendMessage(@Body ChatMessage chatMessage);
    @POST("/testProject_war_exploded/messages/count")
    Call<Integer> updateMessages(@Body Channel channel);
    @POST("/testProject_war_exploded/users/channel")
    Call<List<User>> getUsers(@Body Channel channel);
}
