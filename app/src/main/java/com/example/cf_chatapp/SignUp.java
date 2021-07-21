package com.example.cf_chatapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cf_chatapp.model.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText edtfirstname, edtlastname, edtPhone, edtPass, edtUsername;
    Button btnSignUp;
    Toolbar toolbar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtfirstname =(EditText) findViewById(R.id.edtfirstName);
        edtlastname = (EditText) findViewById(R.id.edtLastName);
        edtPhone = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText)findViewById(R.id.edtPass);
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);



        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SIGN UP");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

    }

    public void SignUp(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        String userID = mDatabase.push().getKey();

        final String firstName = edtfirstname.getText().toString().trim();
        final String lastName = edtlastname.getText().toString().trim();
        final String phone = edtPhone.getText().toString().trim();
        final String userName = edtUsername.getText().toString().trim();
        final String passWord = edtPass.getText().toString().trim();


        UserModel userModel = new UserModel(firstName, lastName, phone, userName, passWord  );
        mDatabase.child(userID).setValue(userModel);
        Toast.makeText(this, "Đăng ký thành công",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);

    }
}