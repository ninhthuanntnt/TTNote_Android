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

    private Toolbar toolbar;
    private BottomNavigationView navBottom;
    private MaterialButton btnAdd;
    private ScrollView llCardContainer;
    private EditText edtContent;
    private EditText edtTitle;
    private NoteModel note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_addition);
        toolbar = findViewById(R.id.tool_bar);
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
                switch (menuItem.getItemId()) {
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
