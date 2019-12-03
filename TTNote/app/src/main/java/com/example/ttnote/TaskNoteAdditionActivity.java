package com.example.ttnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.example.ttnote.adapters.TaskAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskNoteAdditionActivity extends AppCompatActivity {

    private BottomNavigationView navBottom;
    private MaterialButton btnAdd;
    private MaterialButton btnAddTask;
    private ScrollView llCardContainer;
    private EditText edtContent;
    private EditText edtTitle;
    private NoteModel note;
    private Calendar scheduleNote;
    private RecyclerView rvTaskList;

    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_note_addition);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init
        navBottom = findViewById(R.id.nav_bottom);
        btnAdd = findViewById(R.id.btn_add);
        btnAddTask = findViewById(R.id.btn_add_task);
        llCardContainer = findViewById(R.id.ll_card_container);
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        note = new NoteModel();
        scheduleNote = new GregorianCalendar();
        rvTaskList = findViewById(R.id.rv_task_list);

        tasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(tasks);
        rvTaskList.setAdapter(taskAdapter);
        rvTaskList.setLayoutManager(new LinearLayoutManager(this));
        //set event
        //add a task
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskModel task = new TaskModel();
                tasks.add(task);
                taskAdapter.notifyDataSetChanged();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtTitle.getText().toString().isEmpty() && !edtContent.getText().toString().isEmpty()) {
                    note.setTitle(edtTitle.getText().toString());
                    note.setContent(edtContent.getText().toString());
                    note.setTasks(tasks);
//                    TTNoteDatabase db = new TTNoteDatabase(NoteAdditionActivity.this);
//                    db.addNote(note);

                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", note);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(TaskNoteAdditionActivity.this, "Please fill complete", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
