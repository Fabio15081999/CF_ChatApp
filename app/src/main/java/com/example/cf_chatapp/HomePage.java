package com.example.cf_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cf_chatapp.Fragments.ChatsFragment;
import com.example.cf_chatapp.Fragments.ContactsFragment;
import com.example.cf_chatapp.Fragments.ProfileFragment;
import com.example.cf_chatapp.Model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    Toolbar toolbar;
    ImageView btnProfile;
    TextView tvtiltle;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tvtiltle = findViewById(R.id.tvtoolbarTitle);

        toolbar = findViewById(R.id.toolBarHome);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_homepage, new ChatsFragment())
                .commit();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        btnProfile = findViewById(R.id.btnProfile);
        loadProfileImg();
        BottomNavigationView navigationView = findViewById(R.id.bottomNav);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        tvtiltle.setText("Chat");
                        fragment = new ChatsFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_contacts:
                        tvtiltle.setText("Contacts");
                        fragment = new ContactsFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.action_profile:
                        tvtiltle.setText("Profile");
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        break;

                }
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_homepage, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void userStatus(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        reference.updateChildren(map);

    }

    @Override
    protected void onResume() {
        super.onResume();
        userStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        userStatus("offline");
    }

    private void loadProfileImg() {

        storageReference = FirebaseStorage.getInstance().getReference("Uploads");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userModel.getAvatar().equals("default")) {
                    btnProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(userModel.getAvatar()).into(btnProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String userId = firebaseUser.getUid();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String data = dataSnapshot.toString();
//                    Log.d("AAAAA", data );
//                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
//                    if (userModel.getAvatar().equals("default")) {
//                        btnProfile.setImageResource(R.mipmap.ic_launcher);
//                    } else {
//                        Picasso.get().load(userModel.getAvatar()).into(btnProfile);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//    }

}