package com.example.notesapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spnNotes;
    private TextView txtSelectedNoteContent;
    private Button btnDeleteNote;

    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.title_delete_note);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        spnNotes = findViewById(R.id.spnNotes);
        txtSelectedNoteContent = findViewById(R.id.txtSelectedNoteContent);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);

        displayNotesInSpinner();

        spnNotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                displaySelectedNoteContent(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtSelectedNoteContent.setText("");
            }
        });

        btnDeleteNote.setOnClickListener(view -> deleteSelectedNote());
    }

    private void displayNotesInSpinner() {
        notes = NoteStorage.getNotes(this);
        ArrayList<String> noteNames = new ArrayList<>();

        for (Note note : notes) {
            noteNames.add(note.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                noteNames
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNotes.setAdapter(adapter);

        boolean notesExist = !notes.isEmpty();

        spnNotes.setEnabled(notesExist);
        btnDeleteNote.setEnabled(notesExist);

        if (!notesExist) {
            txtSelectedNoteContent.setText(R.string.message_no_notes_to_delete);
        } else {
            displaySelectedNoteContent(0);
        }
    }

    private void displaySelectedNoteContent(int position) {
        if (position >= 0 && position < notes.size()) {
            txtSelectedNoteContent.setText(notes.get(position).getContent());
        }
    }

    private void deleteSelectedNote() {
        if (notes.isEmpty()) {
            return;
        }

        int selectedPosition = spnNotes.getSelectedItemPosition();

        if (selectedPosition < 0 || selectedPosition >= notes.size()) {
            return;
        }

        String selectedNoteName = notes.get(selectedPosition).getName();
        boolean noteDeleted = NoteStorage.deleteNote(this, selectedNoteName);

        if (noteDeleted) {
            Toast.makeText(this, R.string.message_note_deleted, Toast.LENGTH_SHORT).show();
            finish();
        }
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