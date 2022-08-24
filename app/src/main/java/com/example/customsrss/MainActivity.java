package com.example.customsrss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button Signup;
    Button Login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = firebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Signup = (Button) findViewById(R.id.Signup);
        Login = (Button) findViewById(R.id.Login);
    }

    public void singup(View view) {
        String uremail = email.getText().toString();
        String apassword = password.getText().toString();

        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        if(TextUtils.isEmpty(uremail))
        {
            email.setError("enter ur username");
            return;
        }else if(TextUtils.isEmpty(apassword))
        {
            password.setError("enter ur password again");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(uremail,apassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "user created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void Login(View view) {
        String urEmail = email.getText().toString();
        String apassword = password.getText().toString();

        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),CustomActivity.class));
            finish();
        }
        // if/ else statmeents to check if the user enter the correct information or the wrong information.
        if (TextUtils.isEmpty(urEmail)) {
            email.setError("enter ur username");
            return;
        } else if (TextUtils.isEmpty(apassword)) {
            password.setError("enter ur password again");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(urEmail,apassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "user lgoin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),CustomActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}