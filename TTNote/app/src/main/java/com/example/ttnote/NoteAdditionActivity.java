package com.example.ttnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import petrov.kristiyan.colorpicker.ColorPicker;
import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteAdditionActivity extends AppCompatActivity {

    private BottomNavigationView navBottom;
    private MaterialButton btnAdd;
    private ScrollView llCardContainer;
    private EditText edtContent;
    private EditText edtTitle;
    private NoteModel note;
    private Calendar scheduleNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_addition);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init
        navBottom = findViewById(R.id.nav_bottom);
        btnAdd = findViewById(R.id.btn_add);
        llCardContainer = findViewById(R.id.ll_card_container);
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        note = new NoteModel();
        scheduleNote = new GregorianCalendar();

        Intent intent = getIntent();

        try{
            NoteModel currentNote = (NoteModel) intent.getExtras().getSerializable("note");
            note = currentNote;

            edtTitle.setText(currentNote.getTitle());
            edtContent.setText(currentNote.getContent());
            btnAdd.setText("SAVE");
            llCardContainer.setBackgroundColor(currentNote.getBackground());
            toolbar.setBackgroundColor(currentNote.getBackground());
        }catch (NullPointerException ex){

        }



        navBottom.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Calendar calendar = Calendar.getInstance();
                switch (menuItem.getItemId()) {
                    // choose date
//                    case R.id.navigation_calender:
//                        int year = calendar.get(Calendar.YEAR);
//                        final int month = calendar.get(Calendar.MONTH);
//                        int day = calendar.get(Calendar.DAY_OF_MONTH);
//                        DatePickerDialog datePickerDialog = new DatePickerDialog(NoteAdditionActivity.this, new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                scheduleNote.set(year, monthOfYear, dayOfMonth);
//                            }
//                        }, year, month, day);
//                        datePickerDialog.show();
//                        return true;
//                    // choose time
//                    case R.id.navigation_time:
//                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                        final int minute = calendar.get(Calendar.MINUTE);
//                        TimePickerDialog timePickerDialog = new TimePickerDialog(NoteAdditionActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
//                                scheduleNote.set(Calendar.HOUR_OF_DAY, hour);
//                                scheduleNote.set(Calendar.MINUTE, minutes);
//                            }
//                        }, hour, minute, true);
//                        timePickerDialog.show();
//                        return true;
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setCreatedDate((new Date()).getTime());

                if (!edtTitle.getText().toString().isEmpty() && !edtContent.getText().toString().isEmpty()) {
                    note.setTitle(edtTitle.getText().toString());
                    note.setContent(edtContent.getText().toString());

                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", note);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(NoteAdditionActivity.this, "Please fill complete", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#eb8360");
        colors.add("#eba660");
        colors.add("#eeee77");
        colors.add("#71e949");
        colors.add("#49e9e9");
        colors.add("#4949e9");
        colors.add("#ee77b3");
        colors.add("#ffffff");

        colorPicker.setColors(colors)
                .setColumns(4)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        note.setBackground(color);
                        llCardContainer.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();

    }
}
