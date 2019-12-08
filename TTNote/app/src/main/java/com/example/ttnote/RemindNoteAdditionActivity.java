package com.example.ttnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class RemindNoteAdditionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView navBottom;
    private LinearLayout llCardContainer;
    private EditText edtContent;
    private EditText edtTitle;
    private NoteModel note;
    private Calendar scheduleNote;
    private Calendar calendar;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_note_addition);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init
        navBottom = findViewById(R.id.nav_bottom);
        llCardContainer = findViewById(R.id.ll_card_container);
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        note = new NoteModel();
        scheduleNote = Calendar.getInstance();
        scheduleNote.add(Calendar.DAY_OF_MONTH, 1);
        intent = getIntent();
        calendar = Calendar.getInstance();

        //get remind note to update if can
        try{
            NoteModel currentNote = (NoteModel) intent.getExtras().getSerializable("note");
            scheduleNote.setTimeInMillis(currentNote.getDate());
            calendar.setTimeInMillis(currentNote.getDate());
            note = currentNote;

            edtTitle.setText(currentNote.getTitle());
            edtContent.setText(currentNote.getContent());
            llCardContainer.setBackgroundColor(currentNote.getBackground());
            toolbar.setBackgroundColor(currentNote.getBackground());
        }catch (NullPointerException ex){

        }

        navBottom.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // choose date
                    case R.id.navigation_calender:
                        int year = calendar.get(Calendar.YEAR);
                        final int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(RemindNoteAdditionActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                scheduleNote.set(year, monthOfYear, dayOfMonth);
                                calendar.set(year, monthOfYear, dayOfMonth);
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                        return true;
//                    // choose time
                    case R.id.navigation_time:
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        final int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(RemindNoteAdditionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                                scheduleNote.set(Calendar.HOUR_OF_DAY, hour);
                                scheduleNote.set(Calendar.MINUTE, minutes);
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minutes);
                            }
                        }, hour, minute, true);
                        timePickerDialog.show();
                        return true;
                    // choose color
                    case R.id.navigation_color:
                        openColorPicker();
                        return true;
                    // remove diary
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                note.setCreatedDate((new Date()).getTime());

                if (!edtTitle.getText().toString().isEmpty() && !edtContent.getText().toString().isEmpty()) {
                    note.setTitle(edtTitle.getText().toString());
                    note.setContent(edtContent.getText().toString());
                    note.setDate(scheduleNote.getTimeInMillis());

                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", note);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(RemindNoteAdditionActivity.this, "Please fill complete", Toast.LENGTH_LONG).show();
                }
                return true;
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        try{
            NoteModel currentNote = (NoteModel) intent.getExtras().getSerializable("note");
            scheduleNote.setTimeInMillis(currentNote.getDate());
            calendar.setTimeInMillis(currentNote.getDate());
            note = currentNote;

            edtTitle.setText(currentNote.getTitle());
            edtContent.setText(currentNote.getContent());
            llCardContainer.setBackgroundColor(currentNote.getBackground());
            toolbar.setBackgroundColor(currentNote.getBackground());
        }catch (NullPointerException ex){

        }
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
