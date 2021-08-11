package com.example.cf_chatapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cf_chatapp.R;
import com.example.cf_chatapp.Adapter.UsersAdapter;
import com.example.cf_chatapp.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private static final String TAG = "AAAAA";
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<UserModel> mUsers = new ArrayList<>();
    private DatabaseReference mDatabase;
    private Context mContext;
    private EditText searchUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.rvContacts);
        recyclerView.setHasFixedSize(true);
        mUsers = new ArrayList<>();
        readUsers();
        searchUser = view.findViewById(R.id.search_user);
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UsersAdapter(getActivity(), mUsers, false);
        recyclerView.setAdapter(adapter);


        return view;
    }

    private void searchUser(String s) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
//                    assert userModel != null;
//                    assert firebaseUser != null;
                    if (userModel != null) {

                        if (!userModel.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(userModel);
                        }

                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        String userId = firebaseUser.getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (searchUser.getText().toString().equals("")) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (!userModel.getId().equals(userId)) {
                            mUsers.add(userModel);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), R.string.failed + error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}