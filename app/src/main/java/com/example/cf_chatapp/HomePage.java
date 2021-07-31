package com.example.cf_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cf_chatapp.Fragments.ChatsFragment;
import com.example.cf_chatapp.Fragments.ContactsFragment;
import com.example.cf_chatapp.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {
    Toolbar toolbar;
    ImageView btnProfile, btnSettings;
    TextView tvtiltle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tvtiltle = findViewById(R.id.tvtoolbarTitle);

        toolbar = findViewById(R.id.toolBarHome);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

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


}