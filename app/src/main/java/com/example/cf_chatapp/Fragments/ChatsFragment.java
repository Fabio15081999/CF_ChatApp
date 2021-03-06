package com.example.cf_chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cf_chatapp.MainActivity;
import com.example.cf_chatapp.Model.ChatList;
import com.example.cf_chatapp.Notification.Token;
import com.example.cf_chatapp.R;
import com.example.cf_chatapp.Adapter.UsersAdapter;
import com.example.cf_chatapp.Model.Chat;
import com.example.cf_chatapp.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<UserModel> mUsers;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<ChatList> chatListArr;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.rvMessages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        chatListArr = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatListArr.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatList chatList = dataSnapshot.getValue(ChatList.class);
                    chatListArr.add(chatList);
                }
                chatListMethod();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("WWWWW", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        updateToken(token);
                        Log.e("AAAA",token);


                    }
                });
        return view;
    }




    private void chatListMethod() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    for (ChatList chatList : chatListArr) {
                        assert userModel != null;
                        if (userModel.getId().equals(chatList.getId())) {
                            mUsers.add(userModel);
                        }
                    }
                }
                adapter = new UsersAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(adapter);
//                recyclerView.scrollToPosition(0);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        reference.setValue(map);


    }


}



