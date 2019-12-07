package com.example.ttnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageView btnSearch;
    private RecyclerView rv;
    private ArrayList<NoteModel> notes;
    private NoteAdapter noteAdapter;
    private TTNoteDatabase db;
    private Context context;

    private int requestCode;
    private static final int UPDATE_NOTE_CODE = 6515;
    private static final int UPDATE_TASK_NOTE_CODE = 9541;
    private static final int UPDATE_REMIND_NOTE_CODE = 6012;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        this.context = this;
        //init
        requestCode = getIntent().getIntExtra("requestCode", UPDATE_NOTE_CODE);
        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes, null, requestCode);
        btnSearch = toolbar.findViewById(R.id.btn_search);
        edtSearch = toolbar.findViewById(R.id.edt_search);
        rv = findViewById(R.id.recycler_view);
        edtSearch.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        rv.setAdapter(noteAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = new TTNoteDatabase(this);
        // Event
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNote();
            }
        });
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchNote();
//                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == UPDATE_NOTE_CODE || requestCode == UPDATE_REMIND_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");
                db.updateNote(note);
                for (NoteModel noteTemp : notes) {
                    if (note.getId() == noteTemp.getId()) {
                        noteTemp.setTitle(note.getTitle());
                        noteTemp.setContent(note.getContent());
                        noteTemp.setCreatedDate(note.getCreatedDate());
                        noteTemp.setDate(note.getDate());
                        noteTemp.setBackground(note.getBackground());
                        noteTemp.setStatus(note.isStatus());
                        break;
                    }
                }
                noteAdapter.notifyDataSetChanged();
            }
            if(requestCode == UPDATE_TASK_NOTE_CODE){
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");
                db.updateTaskNote(note);
                searchNote();
                noteAdapter.notifyDataSetChanged();
            }
        }
    }

    // funtion definition
    public void searchNote(){
        String value = edtSearch.getText().toString();
        notes.clear();
        if(requestCode == UPDATE_NOTE_CODE)
            notes.addAll(db.searchNote(value));
        if(requestCode == UPDATE_TASK_NOTE_CODE)
            notes.addAll(db.searchTaskNote(value));
        if(requestCode == UPDATE_REMIND_NOTE_CODE)
            notes.addAll(db.searchRemindNote(value));
        noteAdapter.notifyDataSetChanged();
    }
}
