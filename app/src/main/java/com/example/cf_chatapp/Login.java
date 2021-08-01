package com.example.cf_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    private  Toolbar toolbar;
    EditText edtEmailLogin, edtPasswordLogin;
    Button btnLogin;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    TextView tvResetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("LOGIN");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        tvResetPass = findViewById(R.id.forgot_pass);
        tvResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ResetPasswordActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        edtEmailLogin = (EditText) findViewById(R.id.edtUsernameMain) ;
        edtPasswordLogin =(EditText) findViewById(R.id.edtPassWord);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailLogin.getText().toString().trim();
                String pass = edtPasswordLogin.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) ){
                    Toast.makeText(Login.this, "khong duoc de trong cac muc ", Toast.LENGTH_SHORT).show();
                }else if (pass.length() <6){
                    Toast.makeText(Login.this, "pass phai lon hon 6 ky tu!", Toast.LENGTH_SHORT).show();
                }else {
                    login(email, pass);

                }
            }
        });
    }

    private void login(String email, String pass) {

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, HomePage.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(Login.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}