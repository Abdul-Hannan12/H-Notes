package com.rockykhan.hnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNotes extends AppCompatActivity {

    public EditText edt_title, edt_text;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        findids();

        databaseHelper = DatabaseHelper.getInstance(this);


    }

    @Override
    public void onBackPressed() {

        String title = edt_title.getText().toString();
        String text = edt_text.getText().toString();

        if (!title.equals("") && !text.equals("")){
            databaseHelper.notesDAO().addNote(new Note(title, text));
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            super.onBackPressed();
        }else{
            Toast.makeText(getApplicationContext(), "Please Enter Text to add Notes", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

    }

    private void findids() {
        edt_title = findViewById(R.id.addNotesEditTextTitle);
        edt_text = findViewById(R.id.addNotesEditTextText);
    }
}