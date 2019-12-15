package com.example.ttnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //init
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setTitle("About us");
        textView = findViewById(R.id.name1);
        imageView = findViewById(R.id.image);

        String htmlcontent = ("<ul style='list-style-type: none;'><h2>Member</h2><li>Nguyễn Thái Ninh Thuận</li><li>Nguyễn Sỹ Tuấn Thành</li><li>Class: 17T2 - 17.11</li></ul>" +"<span>Link Github:<a href=\"https://github.com/ninhthuanntnt/Android_CND-\">https://github.com/ninhthuanntnt/Android_CND-</a></span>");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(htmlcontent,Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(htmlcontent));
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        imageView.setImageResource(R.drawable.borntoshine);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
