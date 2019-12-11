package com.example.ttnote;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ttnote.Model.NoteModel;
import com.google.android.material.button.MaterialButton;

public class AlarmActivity extends AppCompatActivity {

    private MaterialButton btnStop;
    private MaterialButton btnOk;
    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }
        //init
        btnStop = findViewById(R.id.btn_stop);
        btnOk = findViewById(R.id.btn_ok);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        llContainer = findViewById(R.id.ll_container);
        NoteModel remindNote = (NoteModel) getIntent().getExtras().getSerializable("remindNote");

        tvTitle.setText(remindNote.getTitle());
        tvContent.setText(remindNote.getContent());
        llContainer.setBackgroundColor(remindNote.getBackground());

        // ringtone
        if (getIntent().getBooleanExtra("isFromNotification", false)) {
            btnStop.setVisibility(View.GONE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (alarmUri == null) {
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            final Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
            ringtone.play();
            // event
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ringtone.stop();
                }
            }, 40000);

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ringtone.stop();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ringtone.stop();
                    finish();
                }
            });
        }
    }

}
