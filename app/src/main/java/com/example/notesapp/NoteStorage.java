package com.example.notesapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoteStorage {

    private static final String PREFERENCES_NAME = "notes_preferences";
    private static final String KEY_NOTES = "saved_notes";

    private NoteStorage() {
        // This class should not be instantiated.
    }

    public static ArrayList<Note> getNotes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );

        String savedNotes = preferences.getString(KEY_NOTES, "[]");
        ArrayList<Note> notes = new ArrayList<>();

        try {
            JSONArray notesArray = new JSONArray(savedNotes);

            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject noteObject = notesArray.getJSONObject(i);

                String name = noteObject.getString("name");
                String content = noteObject.getString("content");

                notes.add(new Note(name, content));
            }
        } catch (JSONException exception) {
            return new ArrayList<>();
        }

        return notes;
    }

    public static boolean addNote(Context context, Note newNote) {
        ArrayList<Note> notes = getNotes(context);

        for (Note note : notes) {
            if (note.getName().equalsIgnoreCase(newNote.getName())) {
                return false;
            }
        }

        notes.add(newNote);
        saveNotes(context, notes);
        return true;
    }

    public static boolean deleteNote(Context context, String noteName) {
        ArrayList<Note> notes = getNotes(context);

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getName().equals(noteName)) {
                notes.remove(i);
                saveNotes(context, notes);
                return true;
            }
        }

        return false;
    }

    private static void saveNotes(Context context, ArrayList<Note> notes) {
        JSONArray notesArray = new JSONArray();

        for (Note note : notes) {
            JSONObject noteObject = new JSONObject();

            try {
                noteObject.put("name", note.getName());
                noteObject.put("content", note.getContent());
                notesArray.put(noteObject);
            } catch (JSONException exception) {
                return;
            }
        }

        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );

        preferences.edit()
                .putString(KEY_NOTES, notesArray.toString())
                .apply();
    }
}