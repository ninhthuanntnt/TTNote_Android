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

    private static final int VERSION = 2;
    public static final String DATABASE_NAME = "NTNoteDB";
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
        Cursor cursor = db.rawQuery(" SELECT note.* FROM note LEFT OUTER JOIN task " +
                " ON note.id = task.note_id " +
                " WHERE date = 0 AND task.note_id IS NULL AND note.status = 1", null);

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

    public NoteModel getNoteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        NoteModel note = new NoteModel();
        Cursor cursor = db.rawQuery(" SELECT * FROM note WHERE id = ?", new String[]{String.valueOf(id)});

        cursor.moveToFirst();
        // 1: id, 2:title, 3: content, 4: created_date, 5: date, 6: background, 7: status
        note.setId(cursor.getInt(0));
        note.setTitle(cursor.getString(1));
        note.setContent(cursor.getString(2));
        note.setCreatedDate(cursor.getLong(3));
        note.setDate(cursor.getLong(4));
        note.setBackground(cursor.getInt(5));
        note.setStatus(!(cursor.getInt(6) == 0));


        cursor.close();
        return note;
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

    public ArrayList<NoteModel> searchNote(String value) {
        value = "%" + value + "%";
        ArrayList<NoteModel> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM note LEFT OUTER JOIN task " +
                " ON note.id = task.note_id " +
                " WHERE note.date = 0 " +
                " AND task.note_id IS NULL " +
                " AND (note.title LIKE ? OR note.content LIKE ?) " +
                " AND note.status = 1 ", new String[]{value, value});

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

    public ArrayList<NoteModel> searchTaskNote(String value) {
        value = "%" + value + "%";
        ArrayList<NoteModel> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT note.* FROM note JOIN task " +
                        " ON note.id = task.note_id " +
                        " WHERE note.date = 0 " +
                        " AND (note.title LIKE ? OR note.content LIKE ? OR task.task_name LIKE ?) " +
                        " AND note.status = 1 "
                , new String[]{value, value, value});

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

        for (NoteModel noteTemp : notes) {
            noteTemp.setTasks(this.getTasksByNoteId(noteTemp.getId()));
        }
        cursor.close();
        return notes;
    }

    public void addTaskNote(NoteModel note) {
        ArrayList<TaskModel> tasks = note.getTasks();
        int noteId = (int) addNote(note);
        for (TaskModel taskTemp : tasks) {
            taskTemp.setNoteId(noteId);
            this.addTask(taskTemp);
        }
    }

    public void addTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("task_name", task.getTaskName());
        value.put("status", (task.isStatus()) ? 1 : 0);
        value.put("note_id", task.getNoteId());
        db.insert("task", null, value);
    }

    public void updateTaskNote(NoteModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        this.updateNote(note);
        //update the tasks
        for (TaskModel task : note.getTasks()) {
            //check if task doesn't exist
            if (task.getId() == 0) {
                task.setNoteId(note.getId());
                this.addTask(task);
                continue;
            }
            ContentValues valueTemp = new ContentValues();
            valueTemp.put("task_name", task.getTaskName());
            valueTemp.put("status", (task.isStatus()) ? 1 : 0);
            db.update("task", valueTemp, "id = ?", new String[]{String.valueOf(task.getId())});
        }
    }

    public ArrayList<TaskModel> getTasksByNoteId(int noteId) {
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

    public ArrayList<NoteModel> getAllTaskNotes() {
        ArrayList<NoteModel> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT DISTINCT note.* FROM note INNER JOIN task " +
                " ON note.id = task.note_id " +
                " WHERE note.status = 1 ", null);
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

        for (NoteModel noteTemp : notes) {
            noteTemp.setTasks(this.getTasksByNoteId(noteTemp.getId()));
        }

        return notes;
    }

    public ArrayList<NoteModel> getAllRemindNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<NoteModel> notes = new ArrayList<>();
        Cursor cursor = db.rawQuery(" SELECT note.* FROM note LEFT OUTER JOIN task " +
                " ON note.id = task.note_id " +
                " WHERE date != 0 AND task.note_id IS NULL AND note.status = 1", null);

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

    public ArrayList<NoteModel> searchRemindNote(String value) {
        value = "%" + value + "%";
        ArrayList<NoteModel> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM note LEFT OUTER JOIN task " +
                " ON note.id = task.note_id " +
                " WHERE note.date != 0 " +
                " AND task.note_id IS NULL " +
                " AND (note.title LIKE ? OR note.content LIKE ?) " +
                " AND note.status = 1 ", new String[]{value, value});

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
}
