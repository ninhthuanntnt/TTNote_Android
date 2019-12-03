package com.example.ttnote.Model;

import java.io.Serializable;

public class TaskModel implements Serializable {
    private int id;
    private String taskName;
    private boolean status;
    private int noteId;

    public TaskModel() {
        id = 0;
        this.status = false;
    }

    public TaskModel(String taskName, boolean status, int noteId) {
        this.taskName = taskName;
        this.status = status;
        this.noteId = noteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
