package com.example.ttnote.ui.recycle_bin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.R;
import com.example.ttnote.SearchActivity;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecycleBinFragment extends Fragment {

    private RecyclerView rvRecycleBinList;
    private FloatingActionButton btnClearAll;
    private ArrayList<NoteModel> notes;
    private NoteAdapter noteAdapter;
    private TTNoteDatabase db;

    public static final int UPDATE_DELETED_NOTE_CODE = 3030;
    public static final int SEARCH_DELETED_NOTE_CODE = 4040;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycle_bin, container, false);

        //init
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        rvRecycleBinList = root.findViewById(R.id.rv_deleted_note_list);
        btnClearAll = root.findViewById(R.id.btn_clear_all);
        db = new TTNoteDatabase(getContext());
        notes = db.getAllDeletedNote();
        noteAdapter = new NoteAdapter(notes, this, UPDATE_DELETED_NOTE_CODE);

        rvRecycleBinList.setAdapter(noteAdapter);
        rvRecycleBinList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //Event
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("requestCode", UPDATE_DELETED_NOTE_CODE);
                startActivityForResult(intent, SEARCH_DELETED_NOTE_CODE);
                return false;
            }
        });
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Confirm!");
                builder.setMessage("You are about to delete all this notes of database. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (NoteModel noteTemp : notes){
                            db.deleteNote(noteTemp);
                        }
                        notes.clear();
                        noteAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity().getApplicationContext(), "You've chosen to delete all this notes", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "You've changed your mind to delete all this notes", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();

            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UPDATE_DELETED_NOTE_CODE) {
                NoteModel note = (NoteModel) data.getExtras().getSerializable("note");
                if (data.getBooleanExtra("isCleared", false)) {
                    db.deleteNote(note);
                }else{
                    db.updateNoteStatus(note);
                }
                notes.remove(note);
                noteAdapter.notifyDataSetChanged();
            }
        }
    }
}