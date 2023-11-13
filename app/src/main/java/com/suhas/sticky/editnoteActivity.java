package com.suhas.sticky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    notyy nnnn;
    Intent data;
    private EditText edit_details,edit_title;
    FloatingActionButton save_edit_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        edit_details = findViewById(R.id.Edit_contentOfNote);
        edit_title = findViewById(R.id.EditTitleOf_note);
        save_edit_data= findViewById(R.id.save_Edit_note);

        Toolbar toolbar_edit = findViewById(R.id.ToolbarofEditNote);
        data = getIntent();

        nnnn = new notyy();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String note_title = data.getStringExtra("title");
        String note_details = data.getStringExtra("content");
        edit_details.setText(note_details);
        edit_title.setText(note_title);

        save_edit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(editnoteActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    String new_title = edit_title.getText().toString();
                    String new_content = edit_details.getText().toString();
                    if(new_title.isEmpty() || new_content.isEmpty()){
                        Toast.makeText(getApplicationContext(),"something is empty",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                        Map<String ,Object> note = new HashMap<>();
                        note.put("title",new_title);
                        note.put("content",new_content);
                        documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                startActivity((new Intent(getApplicationContext(), notyy.class)));
                                Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                                editnoteActivity.this.finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),notyy.class));
                               editnoteActivity.this.finish();

                            }
                        });


                    }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Show an alert dialog when the back button is
        if (!isFinishing() || isDestroyed()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("are you want to save the changes...")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle logout action here, such as navigating to the login screen
                            // Finish the current activity
                            Toast.makeText(editnoteActivity.this, "then Hit the damn save button below you m@d@r Ch@d ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();



                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editnoteActivity.this.finish();

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