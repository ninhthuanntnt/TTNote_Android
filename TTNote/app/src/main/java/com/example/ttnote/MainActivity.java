package com.example.ttnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.example.ttnote.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TTNoteDatabase db = new TTNoteDatabase(this);
    ArrayList<NoteModel> remindNotes;
    ArrayList<AlarmManager> alarmManagers;
    ArrayList<PendingIntent> pendingIntents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_note, R.id.nav_task, R.id.nav_remind,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // set notification
        db = new TTNoteDatabase(this);
        remindNotes = db.getAllRemindNotes();
        alarmManagers = new ArrayList<>();
        pendingIntents = new ArrayList<>();

        for (int i = 0, j = 0; i < remindNotes.size(); i++) {
            if(remindNotes.get(i).getDate() < Calendar.getInstance().getTimeInMillis())
                continue;

            AlarmManager alarmManagerTemp = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("id", remindNotes.get(i).getId());
            intent.setAction(String.valueOf(remindNotes.get(i).getId()));
            PendingIntent pendingIntentTemp = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManagers.add(alarmManagerTemp);
            pendingIntents.add(pendingIntentTemp);
            alarmManagerTemp.setExact(AlarmManager.RTC_WAKEUP, remindNotes.get(i).getDate()
                    , pendingIntents.get(j));
            j++;
        }
        System.out.println("------ON CREATE------");
        //task
        //1. click notification then start RemindAddition activity (v)
        //2. update notification when close app
        //3. multiple notification
    }

    @Override
    protected void onStop() {
        super.onStop();

        // set notification
        db = new TTNoteDatabase(this);
        remindNotes = db.getAllRemindNotes();
        alarmManagers = new ArrayList<>();
        pendingIntents = new ArrayList<>();

        for (int i = 0, j = 0; i < remindNotes.size(); i++) {
            if(remindNotes.get(i).getDate() < Calendar.getInstance().getTimeInMillis())
                continue;

            AlarmManager alarmManagerTemp = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("id", remindNotes.get(i).getId());
            intent.setAction(String.valueOf(remindNotes.get(i).getId()));
            PendingIntent pendingIntentTemp = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManagers.add(alarmManagerTemp);
            pendingIntents.add(pendingIntentTemp);
            alarmManagerTemp.setExact(AlarmManager.RTC_WAKEUP, remindNotes.get(i).getDate()
                    , pendingIntents.get(j));
            j++;
        }

        System.out.println("------ON STOP------");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // set notification
        db = new TTNoteDatabase(this);
        remindNotes = db.getAllRemindNotes();
        alarmManagers = new ArrayList<>();
        pendingIntents = new ArrayList<>();

        for (int i = 0, j = 0; i < remindNotes.size(); i++) {
            if(remindNotes.get(i).getDate() < Calendar.getInstance().getTimeInMillis())
                continue;

            AlarmManager alarmManagerTemp = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("id", remindNotes.get(i).getId());
            intent.setAction(String.valueOf(remindNotes.get(i).getId()));
            PendingIntent pendingIntentTemp = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManagers.add(alarmManagerTemp);
            pendingIntents.add(pendingIntentTemp);
            alarmManagerTemp.setExact(AlarmManager.RTC_WAKEUP, remindNotes.get(i).getDate()
                    , pendingIntents.get(j));
            j++;
        }

        System.out.println("------ON PAUSE------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // set notification
        db = new TTNoteDatabase(this);
        remindNotes = db.getAllRemindNotes();
        alarmManagers = new ArrayList<>();
        pendingIntents = new ArrayList<>();

        for (int i = 0, j = 0; i < remindNotes.size(); i++) {
            if(remindNotes.get(i).getDate() < Calendar.getInstance().getTimeInMillis())
                continue;

            AlarmManager alarmManagerTemp = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("id", remindNotes.get(i).getId());
            intent.setAction(String.valueOf(remindNotes.get(i).getId()));
            PendingIntent pendingIntentTemp = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManagers.add(alarmManagerTemp);
            pendingIntents.add(pendingIntentTemp);
            alarmManagerTemp.setExact(AlarmManager.RTC_WAKEUP, remindNotes.get(i).getDate()
                    , pendingIntents.get(j));
            j++;
        }

        System.out.println("------ON DESTROY------");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

