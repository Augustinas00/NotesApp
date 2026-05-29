package com.example.notesapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText txtNoteName;
    private EditText txtNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.title_add_note);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txtNoteName = findViewById(R.id.txtNoteName);
        txtNoteContent = findViewById(R.id.txtNoteContent);
        Button btnSaveNote = findViewById(R.id.btnSaveNote);

        btnSaveNote.setOnClickListener(view -> saveNote());
    }

    private void saveNote() {
        String noteName = txtNoteName.getText().toString().trim();
        String noteContent = txtNoteContent.getText().toString().trim();

        if (noteName.isEmpty()) {
            txtNoteName.setError(getString(R.string.error_note_name_empty));
            txtNoteName.requestFocus();
            return;
        }

        if (noteContent.isEmpty()) {
            txtNoteContent.setError(getString(R.string.error_note_content_empty));
            txtNoteContent.requestFocus();
            return;
        }

        Note note = new Note(noteName, noteContent);
        boolean noteAdded = NoteStorage.addNote(this, note);

        if (!noteAdded) {
            txtNoteName.setError(getString(R.string.error_note_name_exists));
            txtNoteName.requestFocus();
            return;
        }

        Toast.makeText(this, R.string.message_note_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}