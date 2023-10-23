package com.suhas.sticky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class notyy extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private int appColor;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;
    FloatingActionButton mcreate_notes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notyy);
//        getSupportActionBar().setTitle("All Notes");
        mcreate_notes = findViewById(R.id.create_note);



//        appColor = getRandomColor(DocId);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        mcreate_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), create.class));


            }
        });
        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title");
        FirestoreRecyclerOptions<firebasemodel> alluseernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(alluseernotes) {


            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                holder.mnotes = findViewById(R.id.note);
                String DocId = noteAdapter.getSnapshots().getSnapshot(position).getId();


                ImageView popup_button = holder.itemView.findViewById(R.id.menupopupButton);


                LinearLayout mnotes = holder.itemView.findViewById(R.id.note);
                int colourCode = getColorForNoteId(DocId);
                mnotes.setBackgroundColor(colourCode);


                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //we have note detail


                        Intent intent = new Intent(view.getContext(), noteDetails.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        intent.putExtra("noteId", DocId);

                        view.getContext().startActivity(intent);
//                        Toast.makeText(getApplicationContext(),"this is clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                popup_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {

                                Intent intent = new Intent(view.getContext(), editnoteActivity.class);
                                intent.putExtra("title", model.getTitle());
                                intent.putExtra("content", model.getContent());
                                intent.putExtra("noteId", DocId);
                                view.getContext().startActivity(intent);
                                noteAdapter.notifyItemChanged(holder.getAdapterPosition());

                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(DocId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        int position = holder.getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            noteAdapter.getSnapshots().getSnapshot(position).getReference().delete();
                                            noteAdapter.notifyItemRemoved(position);
                                        }
                                        Toast.makeText(getApplicationContext(), "This is note is Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder((view));
            }
        };
        recyclerView = findViewById(R.id.recyclerView007);
//        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(noteAdapter);

    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTitle, noteContent;
        LinearLayout mnotes;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteContent = itemView.findViewById(R.id.notecontent);
            mnotes = itemView.findViewById(R.id.note);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }
    private int getColorForNoteId(String noteId) {
        // Use noteId.hashCode() to generate a hash value based on the note's ID
        int hash = noteId.hashCode();

        // Get the index of the color from the hash value
        int colorIndex = Math.abs(hash) % color.size();

        // Get the color resource ID from the list
        int colorResId = color.get(colorIndex);

        // Retrieve the actual color value associated with the resource ID
        int colorValue;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorValue = getResources().getColor(colorResId, null);
        } else {
            // For older Android versions
            colorValue = getResources().getColor(colorResId);
        }

        return colorValue;
    }

    private List<Integer> color = Arrays.asList(
            R.color.soft_blue,
            R.color.Lavender,
            R.color.peach,
            R.color.Light_gray,
            R.color.Mint_gray,
            R.color.sky_blue,
            R.color.rose_quartz,
            R.color.Light_salom,
            R.color.butter_cream,
            R.color.pale_green,
            R.color.pale_yellow,
            R.color.baby_pink,
            R.color.periwinkle,
            R.color.Light_coral,
            R.color.HoneyDew,
            R.color.Lilac,
            R.color.aqua,
            R.color.pistachio,
            R.color.Blush,
            R.color.powder_blue
    );

    @Override
    public void onBackPressed() {
        // Show an alert dialog when the back button is
        if (!isFinishing() || isDestroyed()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle logout action here, such as navigating to the login screen
                            // Finish the current activity
                            firebaseAuth.signOut();
                            finish();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Dismiss the dialog and do nothing (user chose not to log out)
                            dialog.dismiss();
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