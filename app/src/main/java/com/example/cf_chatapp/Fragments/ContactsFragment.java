package com.example.cf_chatapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cf_chatapp.MainActivity;
import com.example.cf_chatapp.R;
import com.example.cf_chatapp.adapter.UsersAdapter;
import com.example.cf_chatapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsFragment extends Fragment {
    private static final String TAG = "AAAAA";
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<UserModel> mUsers = new ArrayList<>();
    private DatabaseReference mDatabase;
    private Context mContext;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.rvContacts);
        recyclerView.setHasFixedSize(true);
        mUsers = new ArrayList<>();
        readUsers();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UsersAdapter(getActivity(), mUsers,true);
        recyclerView.setAdapter(adapter);


        return view;
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        String userId = firebaseUser.getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (!userModel.getId().equals(userId)) {
                        mUsers.add(userModel);
                    }

                }
                adapter.notifyDataSetChanged();

                //Log.d(TAG, "User name: " + mUsers.() + ", email: " + userModel.getEmail()+"avatar: "+userModel.getAvatar());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "The read failed: " + error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}