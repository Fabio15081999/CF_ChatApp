package com.example.cf_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cf_chatapp.adapter.MessageAdapter;
import com.example.cf_chatapp.model.Chat;
import com.example.cf_chatapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView img;
    private TextView tvusername;
    private ImageView btnSend;
    EditText edtmessage;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    MessageAdapter adapter;
    RecyclerView recyclerView;
    List<Chat> mchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        img = (CircleImageView) findViewById(R.id.imgProfileChat);
        tvusername = (TextView) findViewById(R.id.tvUsernameChat);
        edtmessage =  (EditText )findViewById(R.id.edtmessage); 
        btnSend = (ImageView) findViewById(R.id.button_send);
        recyclerView = (RecyclerView) findViewById(R.id.rvChats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Intent i = getIntent();
        final String userId = i.getStringExtra("userId");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                    tvusername.setText(userModel.getUsername());

                    if (userModel.getAvatar().equals("default")) {
                        img.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(userModel.getAvatar()).into(img);
                    }
                    readmessage(firebaseUser.getUid(), userId, userModel.getAvatar());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edtmessage.getText().toString();
                if (!msg.equals("")){
                    sendmessage(firebaseUser.getUid(), userId, msg);
                }else {
                    Toast.makeText(ChatActivity.this, "message is not null", Toast.LENGTH_SHORT).show();
                }
                edtmessage.setText("");
            }
        });


    }

    private void sendmessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        reference.child("chats").push().setValue(hashMap);


    }
    private void readmessage(String myid, String userid, String imageurl){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }
                    adapter = new MessageAdapter(ChatActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}