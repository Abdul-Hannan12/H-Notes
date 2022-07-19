package com.rockykhan.hnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNotes extends AppCompatActivity {

    EditText edt_title, edt_text;
    DatabaseHelper databaseHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        edt_title = findViewById(R.id.editNotes_EditTextTitle);
        edt_text = findViewById(R.id.editNotes_EditTextText);

        intent = getIntent();
        String title = intent.getStringExtra(MainActivity.NOTES_TITLE);
        String text = intent.getStringExtra(MainActivity.NOTES_TEXT);

        edt_title.setText(title);
        edt_text.setText(text);

        databaseHelper = DatabaseHelper.getInstance(this);

    }

    @Override
    public void onBackPressed() {

        int id = intent.getIntExtra(MainActivity.NOTES_ID, 0);
        String newTitle = edt_title.getText().toString();
        String newText = edt_text.getText().toString();

        if (!newTitle.equals("") && !newText.equals("")) {
            databaseHelper.notesDAO().updateNote(new Note(id, newTitle, newText));
            super.onBackPressed();
        } else {
            Toast.makeText(EditNotes.this, "Note not updated: Plese Enter text to update the note", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
    }
}