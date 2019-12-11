package com.example.ttnote;

import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.adapters.NoteAdapter;
import com.google.android.material.snackbar.Snackbar;

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private NoteAdapter noteAdapter;

    public SwipeToDeleteCallback(NoteAdapter noteAdapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.noteAdapter = noteAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
}
