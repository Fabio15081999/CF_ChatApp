package com.example.cf_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cf_chatapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "AAAAAA";
    EditText edtMessage;
    ImageView btnSend;
    CircleImageView profile_img;
    TextView tvUsername;
    Toolbar toolbar;
    DatabaseReference reference;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = (Toolbar) findViewById(R.id.toolBarChat);
        tvUsername = (TextView) findViewById(R.id.tvtoolbarChatTitle);
        profile_img = (CircleImageView) findViewById(R.id.btnprofileChat);
        edtMessage = (EditText) findViewById(R.id.edit_gchat_message);
        btnSend = (ImageView) findViewById(R.id.btnSend);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edtMessage.getText().toString();
                if (!msg.equals("")) {
                    sendmessage(msg, userId, firebaseUser.getUid());
                } else {
                    Toast.makeText(MessageActivity.this, "vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
//                edtMessage.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    tvUsername.setText(userModel.getUsername());

                    if (userModel.getAvatar().equals("default")) {
                        profile_img.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(userModel.getAvatar()).into(profile_img);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "failed: " + error.getMessage());

            }
        });


    }

    private void sendmessage(String message, String receiver, String sender) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        reference.child("chats").push().setValue(hashMap);
    }
}