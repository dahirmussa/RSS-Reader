package com.example.customsrss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Logout extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);



        bottomNavigationView = bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.logout:
                        startActivity(new Intent(getApplicationContext(),Logout.class));
                        overridePendingTransition(0,0);
                        return true;

                    case  R.id.home:
                        startActivity(new Intent(getApplicationContext(),CustomActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case  R.id.info:
                        startActivity(new Intent(getApplicationContext(),info.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}