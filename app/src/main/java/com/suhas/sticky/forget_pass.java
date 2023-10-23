package com.suhas.sticky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class forget_pass extends AppCompatActivity {
    private TextView go_back;
    private EditText mforgotpassword;

    private Button pass_recovery;

    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);


        firebaseAuth = FirebaseAuth.getInstance();

        mforgotpassword = findViewById(R.id.forgot_pass);
        pass_recovery = findViewById(R.id.pass_recovery_btn);
        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        pass_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mforgotpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(forget_pass.this, "enter mail", Toast.LENGTH_SHORT).show();
                }else{
//                    we have to send the mail message

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task. isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Mail sent ,you can recover your password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));

                            }else{

                                    Toast.makeText(getApplicationContext(), "Email is wrong account Not Exist", Toast.LENGTH_SHORT).show();

                            }
                        }


                    });
                }
            }
        });
    }

}