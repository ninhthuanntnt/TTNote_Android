package com.example.ttnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.example.ttnote.adapters.TaskAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class TaskNoteAdditionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView navBottom;
    private MaterialButton btnAddTask;
    private LinearLayout llCardContainer;
    private EditText edtContent;
    private EditText edtTitle;
    private NoteModel note;
    private Calendar scheduleNote;
    private RecyclerView rvTaskList;

    private TaskAdapter taskAdapter;
    private ArrayList<TaskModel> tasks;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_note_addition);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init
        navBottom = findViewById(R.id.nav_bottom);
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

        intent = getIntent();
        //get note to update if can
        try{
            NoteModel currentNote = (NoteModel) intent.getExtras().getSerializable("note");
            note = currentNote;

            tasks.clear();
            tasks.addAll(currentNote.getTasks());

            edtTitle.setText(currentNote.getTitle());
            edtContent.setText(currentNote.getContent());
            llCardContainer.setBackgroundColor(currentNote.getBackground());
            toolbar.setBackgroundColor(currentNote.getBackground());
        }catch (NullPointerException ex){

        }
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                note.setCreatedDate((new Date()).getTime());

                if (!edtTitle.getText().toString().isEmpty() && !edtContent.getText().toString().isEmpty()) {
                    note.setTitle(edtTitle.getText().toString());
                    note.setContent(edtContent.getText().toString());
                    note.setTasks(tasks);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", note);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(TaskNoteAdditionActivity.this, "Please fill complete", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        navBottom.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_color:
                        openColorPicker();
                        return true;
                    case R.id.navigation_remove:
                        note.setStatus(false);
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", note);
                        intent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addition_toolbar, menu);
        return true;
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#ffff80");
        colors.add("#ffbf80");
        colors.add("#ff8080");
        colors.add("#ff80ff");
        colors.add("#bf80ff");
        colors.add("#809fff");
        colors.add("#80d4ff");
        colors.add("#80ff9f");
        colors.add("#bfff80");
        colors.add("#dfff80");
        colors.add("#bd8e8e");
        colors.add("#ffffff");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setDefaultColorButton(-1)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        if(color == 0)
                            color = -1;
                        note.setBackground(color);
                        llCardContainer.setBackgroundColor(color);
                        toolbar.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();

    }
}
