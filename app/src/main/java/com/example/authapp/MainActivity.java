package com.example.authapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView verifyText;
    Button btnVerify;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyText = findViewById(R.id.textViewVerify);
        btnVerify = findViewById(R.id.btnVerify);

        auth = FirebaseAuth.getInstance();

        if(!auth.getCurrentUser().isEmailVerified()){
            btnVerify.setVisibility(View.VISIBLE);
            verifyText.setVisibility(View.VISIBLE);
        }

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Verification Email Send", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),NextActivity.class));
                        finish();
                    }
                });
            }
        });

    }
}