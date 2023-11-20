package com.example.labfour1030;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Notes> notesList;
    private ArrayAdapter<Notes> adapter;
    private ListView listView;
    private Notes selectedNote;

    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.notes_list);

        notesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(adapter);

        // Handle click on Add button
        Button addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddNoteActivity();
            }
        });

        // Handle long-press to delete a note
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true; // Return true to consume the long-press event
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startEditNoteActivity(position);
            }
        });
    }

    // Method to add a new note to the list
    public void addNoteToList(Notes note) {
        notesList.add(note);
        adapter.notifyDataSetChanged();
    }

    // Method to delete a note
    private void deleteNote(Notes note) {
        notesList.remove(note);
        adapter.notifyDataSetChanged();
    }

    // Method to start AddNoteActivity
    private void startAddNoteActivity() {
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
    }

    // Method to start EditNoteActivity
    private void startEditNoteActivity(int position) {
        selectedNote = notesList.get(position);
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        intent.putExtra("editNote", (CharSequence) selectedNote);
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
    }

    // Method to show delete confirmation dialog
    private void showDeleteConfirmationDialog(final int position) {
        final Notes selectedNote = notesList.get(position);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Note")
                .setMessage("Confirm deletion of this note")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(selectedNote);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST) {
            handleAddNoteResult(resultCode, data);
        } else if (requestCode == EDIT_NOTE_REQUEST) {
            handleEditNoteResult(resultCode, data);
        }
    }

    private void handleAddNoteResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Notes newNote = (Notes) data.getSerializableExtra("newNote");
            if (newNote != null) {
                addNoteToList(newNote);
            }
        }
    }

    private void handleEditNoteResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Notes editedNote = (Notes) data.getSerializableExtra("editedNote");
            if (editedNote != null) {
                int position = notesList.indexOf(selectedNote);
                if (position != -1) {
                    notesList.set(position, editedNote);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
