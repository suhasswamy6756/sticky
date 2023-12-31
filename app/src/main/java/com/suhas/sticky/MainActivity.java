package com.suhas.sticky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText mloginEmail,mloginpass;

    private FirebaseAuth firebaseAuth;
    Button login,mgotosignup;
    public static final String SHARED_PREFS_ = "SHARED_PREFS";



    TextView mforgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            finish();
            startActivity(new Intent(getApplicationContext(),Sign_up.class));
        }

        checkBox();

        mloginEmail = findViewById(R.id.login_email);
        mloginpass = findViewById(R.id.login_password);
        login = findViewById(R.id.btn_log);
        mgotosignup = findViewById(R.id.btn_signup);
        mforgot = findViewById(R.id.forgot_pass_login);
        mgotosignup.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Sign_up.class)));
        mforgot.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),forget_pass.class)));

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // User is already logged in, navigate to the main activity
            startActivity(new Intent(MainActivity.this, notyy.class));
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginEmail.getText().toString().trim();
                String pass = mloginpass.getText().toString().trim();
                if(mail.isEmpty() || pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{
                    //            login the user using firebase....
                    firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS_,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("name","true");
                                editor.apply();


                                checkemailverification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Account Doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

    }

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_,MODE_PRIVATE);
        String check = sharedPreferences.getString("name","");
        if(check.equals("true")){
            Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,notyy.class));
            MainActivity.this.finish();
        }else{


        }
    }

    private void checkemailverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        assert firebaseUser != null;
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notyy.class));
        }else{
            Toast.makeText(getApplicationContext(),"first Verify your mail",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        clearAppCache(this);
//        clearAppData(this);
        MainActivity.this.finish();
    }


//    MainActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

}