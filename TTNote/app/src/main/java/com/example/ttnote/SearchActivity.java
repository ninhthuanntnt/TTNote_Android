package com.example.ttnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageButton btnVoice;
    private RecyclerView rv;
    private ArrayList<NoteModel> notes;
    private NoteAdapter noteAdapter;
    private TTNoteDatabase db;
    private Context context;

    private int requestCode;
    private static final int UPDATE_NOTE_CODE = 6515;
    private static final int UPDATE_TASK_NOTE_CODE = 9541;
    private static final int UPDATE_REMIND_NOTE_CODE = 6012;
    private static final int SPEECH_INPUT_CODE = 1111;

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
        edtSearch = toolbar.findViewById(R.id.edt_search);
        btnVoice = toolbar.findViewById(R.id.btn_voice);
        rv = findViewById(R.id.recycler_view);
        edtSearch.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        rv.setAdapter(noteAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = new TTNoteDatabase(this);
        // Event
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
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNote();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
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
            if(requestCode == SPEECH_INPUT_CODE){
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                edtSearch.setText(result.get(0));
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

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say something…");
        try {
            startActivityForResult(intent, SPEECH_INPUT_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
