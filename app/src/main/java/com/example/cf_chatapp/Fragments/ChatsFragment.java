package com.example.cf_chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.cf_chatapp.R;
import com.example.cf_chatapp.Adapter.UsersAdapter;
import com.example.cf_chatapp.Model.Chat;
import com.example.cf_chatapp.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<UserModel> mUsers;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<String> usersList;
    private ProgressBar progressBar ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.rvMessages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSender().equals(firebaseUser.getUid())){
                        usersList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())){
                        usersList.add(chat.getSender());
                    }

                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    for (String id: usersList){
                        if (userModel.getId().equals(id)){
                            if (mUsers.size()!=0){
                                for (UserModel userModel1 :mUsers){
                                    if (!userModel.getId().equals(userModel1.getId())){
                                        mUsers.add(userModel);
                                    }
                                }
                            } else {
                                mUsers.add(userModel);
                            }
                        }
                    }
                }
                adapter = new UsersAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}



