package com.example.cf_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.cf_chatapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "AAAAAA";
    CircleImageView profile_img;
    TextView tvUsername;
    Toolbar toolbar;
    Intent intent;
    DatabaseReference reference;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = (Toolbar) findViewById(R.id.toolBarChat);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);


        tvUsername = (TextView) findViewById(R.id.tvtoolbarTitle);

        profile_img = (CircleImageView) findViewById(R.id.btnProfile);

        intent = getIntent();
        String userId = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                tvUsername.setText(userModel.getUsername());
                if (userModel.getAvatar().equals("default")){
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Picasso.get().load(userModel.getAvatar()).into(profile_img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d( TAG, "failed: "+error.getMessage());

            }
        });






    }
}