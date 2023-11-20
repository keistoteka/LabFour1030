package com.example.labfour1030;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private boolean isEditingNote;
    private Notes editedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        final EditText headerEditText = findViewById(R.id.edit_text_header);
        final EditText bodyEditText = findViewById(R.id.edit_text_body);

        // Check if we are editing an existing note
        Intent intent = getIntent();
        if (intent.hasExtra("editNote")) {
            isEditingNote = true;
            editedNote = (Notes) intent.getSerializableExtra("editNote");

            // Set the existing note's data in the EditText fields
            headerEditText.setText(editedNote.getHeader());
            bodyEditText.setText(editedNote.getBody());
        } else {
            isEditingNote = false;
        }

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = headerEditText.getText().toString();
                String body = bodyEditText.getText().toString();
                if (header.isEmpty() && body.isEmpty()) {
                    // Show a toast message if either header or body is empty
                    Toast.makeText(AddNoteActivity.this, "Please enter both header and body.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new Note or update an existing one
                    if (isEditingNote) {
                        // Editing an existing note
                        editedNote.setHeader(header);
                        editedNote.setBody(body);

                        Intent intent = new Intent();
                        intent.putExtra("editedNote", (CharSequence) editedNote);
                        setResult(RESULT_OK, intent);
                    } else {
                        // Creating a new note
                        Notes newNote = new Notes(header, body);

                        Intent intent = new Intent();
                        intent.putExtra("newNote", (CharSequence) newNote);
                        setResult(RESULT_OK, intent);
                    }

                    finish();
                }
            }
        });
    }
}
