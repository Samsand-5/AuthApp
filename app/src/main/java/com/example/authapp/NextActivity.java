package com.example.authapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NextActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private BluetoothAdapter bluetoothAdapter;

    Button btnLogOut, btnBluetoothOn;
    TextView txtUserUID;  // TextView to display UID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnBluetoothOn = findViewById(R.id.btnOn);
        btnLogOut = findViewById(R.id.btnLogOut);
        txtUserUID = findViewById(R.id.txtUserUID);

        // Display the current user's UID if signed in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            txtUserUID.setText("User UID: " + uid);
        } else {
            Toast.makeText(this, "No user is signed in", Toast.LENGTH_SHORT).show();
        }

        // Bluetooth ON button click handler
        btnBluetoothOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NextActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // Request BLUETOOTH_CONNECT permission
                    ActivityCompat.requestPermissions(NextActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
                } else {
                    turnOnBluetooth();
                }
            }
        });

        // Logout button click handler
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    private void turnOnBluetooth() {
        try {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            } else if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            } else {
                Toast.makeText(this, "Bluetooth is already on", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Security Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                turnOnBluetooth();
            } else {
                Toast.makeText(this, "Permission denied. Bluetooth cannot be enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
