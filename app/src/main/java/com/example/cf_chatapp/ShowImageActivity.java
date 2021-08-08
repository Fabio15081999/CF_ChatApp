package com.example.cf_chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowImageActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_acticity);
        imageView = findViewById(R.id.img_view);
        textView= findViewById(R.id.tvBack);
        i = getIntent();
        String imageUrl = i.getStringExtra("imageUrl");
        Picasso.get().load(imageUrl).into(imageView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}