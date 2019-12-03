package com.example.ttnote.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.example.ttnote.R;
import com.example.ttnote.TaskNoteAdditionActivity;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private RecyclerView rvTaskList;
    private FloatingActionButton btnAdd;
    private NoteAdapter noteAdapter;
    private ArrayList<NoteModel> notes;
    private TTNoteDatabase db;

    public static final int ADD_TASK_NOTE_CODE = 1003;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        db = new TTNoteDatabase(getContext());
        //init
        btnAdd = root.findViewById(R.id.btn_add);
        rvTaskList = root.findViewById(R.id.rv_task_list);
        notes = db.getAllNotes();

        //note filter
        ArrayList<NoteModel> taskNotes = new ArrayList<>();
        for(int i=0 ; i < notes.size(); i++){
            if(!notes.get(i).getTasks().isEmpty()){
                taskNotes.add(notes.get(i));
            }
        }
        notes = taskNotes;
        // set adapter
        noteAdapter = new NoteAdapter(notes, this);
        rvTaskList.setAdapter(noteAdapter);
        rvTaskList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //set event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskNoteAdditionActivity.class);
                startActivityForResult(intent, ADD_TASK_NOTE_CODE);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}