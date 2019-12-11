package com.example.ttnote.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.NoteAdditionActivity;
import com.example.ttnote.R;
import com.example.ttnote.SearchActivity;
import com.example.ttnote.SwipeToDeleteCallback;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Context context;
    private ArrayList<NoteModel> notes;
    private NoteAdapter noteAdapter;
    private RecyclerView rvNoteList;
    private TTNoteDatabase db;
    private FloatingActionButton btnAdd;
    private CoordinatorLayout clContainerSnackbar;

    public static final int ADD_NOTE_CODE = 1002;
    public static final int UPDATE_NOTE_CODE = 6515;
    public static final int SEARCH_NOTE_CODE = 5621;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        //init
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        db = new TTNoteDatabase(getContext());
        clContainerSnackbar = root.findViewById(R.id.cl_container_snackbar);
        rvNoteList = root.findViewById(R.id.rv_note_list);
        btnAdd = root.findViewById(R.id.btn_add);
        notes = db.getAllNotes();

        //set adapter to recycler view
        noteAdapter = new NoteAdapter(notes, this, UPDATE_NOTE_CODE);
        rvNoteList.setAdapter(noteAdapter);
        rvNoteList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        enableSwipeToDeleteAndUndo();
        // event
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteAdditionActivity.class);
                startActivityForResult(intent, ADD_NOTE_CODE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("requestCode", UPDATE_NOTE_CODE);
                startActivityForResult(intent, SEARCH_NOTE_CODE);
                return false;
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");

                TTNoteDatabase db = new TTNoteDatabase(getContext());
                db.addNote(note);
                notes.clear();
                notes.addAll(db.getAllNotes());
                noteAdapter.notifyDataSetChanged();
            }
            if (requestCode == UPDATE_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");

                TTNoteDatabase db = new TTNoteDatabase(getContext());
                db.updateNote(note);

                // remove when status is false
                int position = notes.indexOf(note);
                notes.remove(note);
                if (note.isStatus())
                    notes.add(position, note);
                else
                    showSnackBar(position, note);
                noteAdapter.notifyDataSetChanged();
            }

        }
        if (requestCode == SEARCH_NOTE_CODE) {
            notes.clear();
            notes.addAll(db.getAllNotes());
            noteAdapter.notifyDataSetChanged();
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(noteAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final NoteModel note = notes.get(position);

                //remove note
                note.setStatus(false);
                db.updateNoteStatus(note);
                notes.remove(note);
                noteAdapter.notifyItemRemoved(position);
                //show snackbar
                showSnackBar(position, note);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(rvNoteList);
    }

    private void showSnackBar(final int position, final NoteModel note) {
        Snackbar snackbar = Snackbar.make(clContainerSnackbar, "Note was removed from the list.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //restore note
                note.setStatus(true);
                db.updateNoteStatus(note);
                notes.add(position, note);
                noteAdapter.notifyItemInserted(position);
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}