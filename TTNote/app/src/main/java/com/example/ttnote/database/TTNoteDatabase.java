package com.example.ttnote.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;

import java.util.ArrayList;

public class TTNoteDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "TTNoteDB";
    public static final String NOTE_TABLE_NAME = "note";
    public static final String TASK_TABLE_NAME = "task";

    public TTNoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String scriptNote = "CREATE TABLE " + NOTE_TABLE_NAME + "( id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " title TEXT," +
                            " content TEXT, " +
                            " created_date INTEGER, " +
                            " date INTEGER, " +
                            " background INTEGER DEFAULT -1, " +
                            " status INTEGER DEFAULT 1 ) ";
        String scriptTask = " CREATE TABLE " + TASK_TABLE_NAME + "( id " + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " task_name TEXT, " +
                            " status INTEGER DEFAULT 1, " +
                            " note_id INTEGER, " +
                            " FOREIGN KEY (note_id) REFERENCES " + NOTE_TABLE_NAME + "note(id) )";
        db.execSQL(scriptNote);
        db.execSQL(scriptTask);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<NoteModel> getAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<NoteModel> notes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM note WHERE date = 0", null);

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

            ArrayList<TaskModel> tasks = getTasksByNoteId(note.getId());
            note.setTasks(tasks);
            notes.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return notes;
    }

    public long addNote(NoteModel note) {
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
        return db.insert("note", null, value);
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
        db.update("note", value, "id = ? ", new String[]{String.valueOf(note.getId())});
    }

    public ArrayList<NoteModel> searchNote(String value){
        value = "%" + value + "%";
        ArrayList<NoteModel> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM note WHERE date = 0 and (title LIKE ? OR content LIKE ?)", new String[]{value, value});

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

    public void addTaskNote(NoteModel note){
        ArrayList<TaskModel> tasks = note.getTasks();
        int noteId = (int) addNote(note);
        for(TaskModel taskTemp : tasks){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues value = new ContentValues();
            taskTemp.setNoteId(noteId);
            value.put("task_name", taskTemp.getTaskName());
            value.put("status", (taskTemp.isStatus())?1:0);
            value.put("note_id", taskTemp.getNoteId());
            db.insert("task", null, value);
        }
    }

    public ArrayList<TaskModel> getTasksByNoteId(int noteId){
        ArrayList<TaskModel> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM task WHERE note_id = ?", new String[]{String.valueOf(noteId)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TaskModel task = new TaskModel();
            task.setId(cursor.getInt(0));
            task.setTaskName(cursor.getString(1));
            task.setStatus((cursor.getInt(2) == 1) ? true : false);
            task.setNoteId(cursor.getInt(3));
            tasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }
}
