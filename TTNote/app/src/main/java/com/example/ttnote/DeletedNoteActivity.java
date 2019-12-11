package com.example.ttnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeletedNoteActivity extends AppCompatActivity {

    private Context context;

    private Toolbar toolbar;
    private MaterialButton btnClear;
    private MaterialButton btnRestore;
    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout llContainer;

    private Intent intent;
    private NoteModel note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_note);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init
        context = this;
        btnClear = findViewById(R.id.btn_clear);
        btnRestore = findViewById(R.id.btn_restore);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        llContainer = findViewById(R.id.ll_container);

        intent = getIntent();
        note = (NoteModel) intent.getExtras().getSerializable("note");

        tvTitle.setText(note.getTitle());
        llContainer.setBackgroundColor(note.getBackground());
        toolbar.setBackgroundColor(note.getBackground());
        final StringBuilder content = new StringBuilder();
        if (note.getDate() == 0)
            if(note.getTasks() != null && note.getTasks().size() > 0){
                String[] noteContentSplit = note.getContent().split("\n");
                for (int i = 0; i < noteContentSplit.length; i++) {
                    content.append("<span>" + noteContentSplit[i] + "</span><br/>");
                }
                content.append("<br/><u><b>Your tasks:</b></u><br/>");
                for (TaskModel taskTemp : note.getTasks()) {
                    if (taskTemp.isStatus()) {
                        content.append("<span>" + "- " + "<span style='text-decoration:line-through'>" + taskTemp.getTaskName() + "</span>" + "</span>" + "<br/>");
                    } else {
                        content.append("<span>" + "- " + taskTemp.getTaskName() + "</span><br/>");
                    }
                }

                tvContent.setText(Html.fromHtml(content.substring(0, content.length() - 5)));
            }else {
                content.append(note.getContent());
                tvContent.setText(content);
            }
        else{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            content.append("<span>" +note.getContent()+ "</span><br/>" + "<b> REMIND YOU AT: " + format.format(new Date(note.getDate())) + "</b>");
            tvContent.setText(Html.fromHtml(content.toString()));
        }

        //Event
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                intent.putExtras(bundle);
                note.setStatus(true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Please Confirm!");
                builder.setMessage("You are about to delete this note in database. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", note);
                        intent.putExtras(bundle);
                        intent.putExtra("isCleared", true);
                        setResult(Activity.RESULT_OK, intent);
                        Toast.makeText(context.getApplicationContext(), "You've chosen to delete this note", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "You've changed your mind to delete this note", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
