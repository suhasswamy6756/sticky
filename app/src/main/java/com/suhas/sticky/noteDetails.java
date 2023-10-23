package com.suhas.sticky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public  class noteDetails extends AppCompatActivity  {
    private TextView Details,title;
    FloatingActionButton edit_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Details = findViewById(R.id.detail_contentOfNote);
        title = findViewById(R.id.DetailTitleOf_note);
        edit_note = findViewById(R.id.gotoEdit);

        Intent data = getIntent();
        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),editnoteActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));
               view.getContext().startActivity(intent);
                finish();

            }
        });
        title.setText(data.getStringExtra("title"));

        Details.setText(data.getStringExtra("content"));

    }

    @Override
    public void onBackPressed() {
        Log.d("finished Activity","noyhimh");

        startActivity(new Intent(getApplicationContext(),notyy.class));

        super.onBackPressed();
        finish();

    }
}