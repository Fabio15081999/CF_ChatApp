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
            "Authorization:key=AAAAncP2JaQ:APA91bExLeN1jW3YuT38w1bx9hL179R4_stKj0OaPR1gAOFeHt9ceBFaxf0bjeVQR6rJlRDhWsKNW6X_GSQ-OYsEAA617TdvisWB8fhxHAuW5S4-2pMdkljdu0bG9x6BNc5K00KM-NSd"
    })
    @POST("fcm/send")
    Call<MyReponse> sendNotification(@Body Sender body);


}
