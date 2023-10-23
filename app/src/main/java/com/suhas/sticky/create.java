package com.suhas.sticky;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class create extends AppCompatActivity {
    private EditText mcreateTitle_note,mcreatecontent_note;
    FloatingActionButton mSave_Note;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseFirestore firebaseFirestore;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mSave_Note=findViewById(R.id.save_note);
        mcreatecontent_note = findViewById(R.id.create_contentOfNote);
        mcreateTitle_note = findViewById(R.id.createTitleOf_note);

        Toolbar toolbar = findViewById(R.id.ToolbarofcreateNote);
        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        mSave_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mcreateTitle_note.getText().toString();
                String content = mcreatecontent_note.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Both field are required ",Toast.LENGTH_SHORT).show();
                }else{
                   DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String , Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Note Created successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), notyy.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to Create note",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), notyy.class));

                        }
                    });

                }
            }
        });


    }
gir
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.date_pick){
            showDate_picker();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Show an alert dialog when the back button is
        if (!isFinishing() || isDestroyed()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to create a note...")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle logout action here, such as navigating to the login screen
                            // Finish the current activity
                            Toast.makeText(getApplicationContext() ,"then Hit the damn save button below,you m@d@r Ch@d ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();



                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                            // Dismiss the dialog and do nothing (user chose not to log out)
                            startActivity(new Intent(getApplicationContext(), notyy.class));
                        }
                    })
                    .setCancelable(false); // Prevent dialog dismissal outside of buttons

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else{
            super.onBackPressed();
        }
    }
}