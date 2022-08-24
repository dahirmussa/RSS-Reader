package com.example.customsrss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class info extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText rssItem;
    EditText rssDesc;
    Button sendToFB;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        rssItem = (EditText) findViewById(R.id.rssItem);
        rssDesc = (EditText) findViewById(R.id.rssDesc);
        sendToFB = (Button) findViewById(R.id.sendToFB);


        bottomNavigationView = bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.info:
                        startActivity(new Intent(getApplicationContext(),info.class));
                        overridePendingTransition(0,0);
                        return true;

                    case  R.id.home:
                        startActivity(new Intent(getApplicationContext(),CustomActivity.class));
                        overridePendingTransition(0,0);
                        return true;


                    case  R.id.logout:
                        startActivity(new Intent(getApplicationContext(),Logout.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void sendToFB(View view) {
        String title = ((EditText) findViewById(R.id.rssItem)).getText().toString();
        String description = ((EditText) findViewById(R.id.rssDesc)).getText().toString();

        NewsItems newsItem = new NewsItems(title, description);
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference();
            Log.d("User uid:", user.getUid());
            dbRef.child("users").child(user.getUid()).push().setValue(newsItem);
        } else {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }

    }
}
