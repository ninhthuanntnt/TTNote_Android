package com.example.ttnote.ui.remind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.NoteAdditionActivity;
import com.example.ttnote.R;
import com.example.ttnote.RemindNoteAdditionActivity;
import com.example.ttnote.SearchActivity;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RemindFragment extends Fragment {

    private Context context;
    private ArrayList<NoteModel> notes;
    private NoteAdapter noteAdapter;
    private RecyclerView rvRemindNoteList;
    private TTNoteDatabase db;
    private FloatingActionButton btnAdd;

    public static final int ADD_REMIND_NOTE_CODE = 1004;
    public static final int UPDATE_REMIND_NOTE_CODE = 6012;
    public static final int SEARCH_REMIND_NOTE_CODE = 9823;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remind, container, false);

        //init
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        db = new TTNoteDatabase(getContext());
        rvRemindNoteList = root.findViewById(R.id.rv_remind_note_list);
        btnAdd = root.findViewById(R.id.btn_add);
        notes = db.getAllRemindNotes();

        //set adapter to recycler view
        noteAdapter = new NoteAdapter(notes, this, UPDATE_REMIND_NOTE_CODE);
        rvRemindNoteList.setAdapter(noteAdapter);
        rvRemindNoteList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //set event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RemindNoteAdditionActivity.class);
                startActivityForResult(intent, ADD_REMIND_NOTE_CODE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("requestCode", UPDATE_REMIND_NOTE_CODE);
                startActivityForResult(intent, SEARCH_REMIND_NOTE_CODE);
                return false;
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_REMIND_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");

                TTNoteDatabase db = new TTNoteDatabase(getContext());
                db.addNote(note);
                notes.clear();
                notes.addAll(db.getAllNotes());
                noteAdapter.notifyDataSetChanged();
            }
            if (requestCode == UPDATE_REMIND_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");

                TTNoteDatabase db = new TTNoteDatabase(getContext());
                db.updateNote(note);
                notes.clear();
                notes.addAll(db.getAllRemindNotes());
                noteAdapter.notifyDataSetChanged();
            }

        }
        if (requestCode == SEARCH_REMIND_NOTE_CODE) {
            notes.clear();
            notes.addAll(db.getAllRemindNotes());
            noteAdapter.notifyDataSetChanged();
        }
    }
}