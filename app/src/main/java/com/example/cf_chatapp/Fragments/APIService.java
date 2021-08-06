package com.example.cf_chatapp.Fragments;

import com.example.cf_chatapp.Notification.MyReponse;
import com.example.cf_chatapp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAncP2JaQ:APA91bECE5JgA94-wfZpUbUXoPvKsX8mxdIxcNX0SwgYkhodfrR9SO5FvxltGkq67n8zBiMRbiW9E9AY60HRMluYIXKTckqDZi3qCdaM3fsqha-YpKhRaDl1G1Owc3MEfrE5OXnOmz0h"
    })
    @POST("fcm/send")
    Call<MyReponse> sendNotification(@Body Sender body);


}
