package com.suhas.sticky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_up extends AppCompatActivity {
    private EditText mSignupEmail,mSignUppass;
    private RelativeLayout msignup;
    private TextView gotologin;
    FirebaseAuth firebaseAuth;
    private Button Register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();


        mSignupEmail = findViewById(R.id.sign_email);
        mSignUppass = findViewById(R.id.signup_password);
        msignup = findViewById(R.id.signup);
        gotologin = findViewById(R.id.gotoLogIn);
        Register = findViewById(R.id.sign_up);

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                Sign_up.this.finish();
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mSignupEmail.getText().toString().trim();
                String password = mSignUppass.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Sign_up.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }else if(password.length()<7){
                    Toast.makeText(Sign_up.this, "password must be more than 7 characters", Toast.LENGTH_SHORT).show();
                }else{
                    //connect to firebase...
                    firebaseAuth.createUserWithEmailAndPassword(mail,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                        sendEmailVerification();
                                    }else{

                                        Toast.makeText(getApplicationContext(),"failed to register",Toast.LENGTH_SHORT).show();


                                    }

                                }
                            });

                }
            }
        });
//        finish_act();

    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verfication emial is sent,verify and Login again",Toast.LENGTH_SHORT).show();
//                    firebaseAuth.signOut();

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish_act();


                }
            });
        }else{

            Toast.makeText(getApplicationContext(),"failed to send verification Email",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void finish_act(){
        Sign_up.this.finish();
    }

}
