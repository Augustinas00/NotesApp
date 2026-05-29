package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listNotes;
    private TextView txtEmptyNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listNotes = findViewById(R.id.listNotes);
        txtEmptyNotes = findViewById(R.id.txtEmptyNotes);

        listNotes.setEmptyView(txtEmptyNotes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySavedNotes();
    }

    private void displaySavedNotes() {
        ArrayList<Note> notes = NoteStorage.getNotes(this);
        ArrayList<String> displayedNotes = new ArrayList<>();

        for (Note note : notes) {
            String displayedText = note.getName() + "\n" + note.getContent();
            displayedNotes.add(displayedText);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayedNotes
        );

        listNotes.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedItemId = item.getItemId();

        if (selectedItemId == R.id.menuAddNote) {
            Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(addNoteIntent);
            return true;
        }

        if (selectedItemId == R.id.menuDeleteNote) {
            Intent deleteNoteIntent = new Intent(MainActivity.this, DeleteNoteActivity.class);
            startActivity(deleteNoteIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}