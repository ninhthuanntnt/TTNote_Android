package com.example.ttnote.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class NoteModel implements Serializable {
    private int id;
    private String title;
    private String content;
    private long createdDate;
    private long date;
    private int background;
    private boolean status;
    private ArrayList<TaskModel> tasks;

    public NoteModel() {
        this.background = -1;
        this.status = true;
        this.date = 0;
        this.tasks = null;
    }

    public NoteModel(int id, String title, String content, long createdDate, long date, int background, boolean status, ArrayList<TaskModel> tasks) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.date = date;
        this.background = background;
        this.status = status;
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TaskModel> tasks) {
        this.tasks = tasks;
    }
}
