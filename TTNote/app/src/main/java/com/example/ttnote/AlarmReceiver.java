package com.example.ttnote;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.database.TTNoteDatabase;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 1);
        TTNoteDatabase db = new TTNoteDatabase(context);
        NoteModel remindNote = db.getNoteById(id);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(remindNote);
        nb.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notificationHelper.getManager().notify( (int) System.currentTimeMillis(), nb.build());

        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        Bundle alarmBundle = new Bundle();
        alarmBundle.putSerializable("remindNote", remindNote);
        alarmIntent.putExtras(alarmBundle);
        context.startActivity(alarmIntent);
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null) {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//        ringtone.play();
    }
}
