package com.example.ttnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ttnote.Model.NoteModel;

import java.util.ArrayList;

public class TTNoteDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TTNoteDB";
    private static final String TABLE_NAME = "note";

    public TTNoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_NAME + "( id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " title TEXT,"
                + " content TEXT," +
                " created_date INTEGER," +
                " date INTEGER," +
                " background INTEGER DEFAULT -1," +
                " status INTEGER DEFAULT 1)";

        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<NoteModel> getAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<NoteModel> notes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM note", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteModel note = new NoteModel();
            // 1: id, 2:title, 3: content, 4: created_date, 5: date, 6: background, 7: status
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setCreatedDate(cursor.getLong(3));
            note.setDate(cursor.getLong(4));
            note.setBackground(cursor.getInt(5));
            note.setStatus(!(cursor.getInt(6) == 0));

            notes.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return notes;
    }

    public void addNote(NoteModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title", note.getTitle());
        value.put("content", note.getContent());
        value.put("created_date", note.getCreatedDate());
        value.put("date", note.getDate());
        value.put("background", note.getBackground());
        if ((note.isStatus())) {
            value.put("status", 1);
        } else {
            value.put("status", 0);
        }
        db.insert("note", null, value);
    }

    public void updateNote(NoteModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title", note.getTitle());
        value.put("content", note.getContent());
        value.put("created_date", note.getCreatedDate());
        value.put("date", note.getDate());
        value.put("background", note.getBackground());
        if ((note.isStatus())) {
            value.put("status", 1);
        } else {
            value.put("status", 0);
        }
        db.update("note", value, "id = ?", new String[]{String.valueOf(note.getId())});
    }
}
