package com.example.ttnote.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.NoteAdditionActivity;
import com.example.ttnote.R;
import com.example.ttnote.SearchActivity;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Context context;
    private ArrayList<NoteModel> notes;
    private NoteAdapter noteAdapter;
    private RecyclerView rvNoteList;
    private TTNoteDatabase db;
    private FloatingActionButton btnAdd;
    public static final int ADD_NOTE_CODE = 1002;
    public static final int UPDATE_NOTE_CODE = 6515;
    public static final int SEARCH_NOTE_CODE = 5621;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        //init
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        db = new TTNoteDatabase(getContext());
        rvNoteList = root.findViewById(R.id.rv_note_list);
        btnAdd = root.findViewById(R.id.btn_add);
        notes = db.getAllNotes();
        //note filter
        ArrayList<NoteModel> notesTemp = new ArrayList<>();
        for(int i=0 ; i < notes.size(); i++){
            if(notes.get(i).getTasks().isEmpty()){
                notesTemp.add(notes.get(i));
            }
        }
        notes = notesTemp;

        //set adapter to list view
        noteAdapter = new NoteAdapter(notes, this, UPDATE_NOTE_CODE);
        rvNoteList.setAdapter(noteAdapter);
        rvNoteList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        // event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteAdditionActivity.class);
//                getActivity().startActivityForResult(intent, ADD_NOTE_CODE);
                startActivityForResult(intent, ADD_NOTE_CODE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("requestCode", UPDATE_NOTE_CODE);
                startActivityForResult(intent, SEARCH_NOTE_CODE);
                return false;
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");

                TTNoteDatabase db = new TTNoteDatabase(getContext());
                db.addNote(note);
                notes.clear();
                notes.addAll(db.getAllNotes());
                noteAdapter.notifyDataSetChanged();
            }
            if (requestCode == UPDATE_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");

                TTNoteDatabase db = new TTNoteDatabase(getContext());
                db.updateNote(note);
//                for (NoteModel noteTemp : notes) {
//                    if (note.getId() == noteTemp.getId()) {
//                        noteTemp.setTitle(note.getTitle());
//                        noteTemp.setContent(note.getContent());
//                        noteTemp.setCreatedDate(note.getCreatedDate());
//                        noteTemp.setDate(note.getDate());
//                        noteTemp.setBackground(note.getBackground());
//                        noteTemp.setStatus(note.isStatus());
//                        break;
//                    }
//                }
                notes.clear();
                notes.addAll(db.getAllNotes());
                noteAdapter.notifyDataSetChanged();
            }

        }
        if(requestCode == SEARCH_NOTE_CODE){
            notes.clear();
            notes.addAll(db.getAllNotes());
            noteAdapter.notifyDataSetChanged();
        }
    }
}