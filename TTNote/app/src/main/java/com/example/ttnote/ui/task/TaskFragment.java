package com.example.ttnote.ui.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.example.ttnote.R;
import com.example.ttnote.SearchActivity;
import com.example.ttnote.SwipeToDeleteCallback;
import com.example.ttnote.TaskNoteAdditionActivity;
import com.example.ttnote.adapters.NoteAdapter;
import com.example.ttnote.database.TTNoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private RecyclerView rvTaskList;
    private FloatingActionButton btnAdd;
    private NoteAdapter noteAdapter;
    private ArrayList<NoteModel> notes;
    private TTNoteDatabase db;
    private CoordinatorLayout clContainerSnackbar;

    private static final int ADD_TASK_NOTE_CODE = 1003;
    private static final int UPDATE_TASK_NOTE_CODE = 9541;
    private static final int SEARCH_TASK_NOTE_CODE = 8912;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        //init
        btnAdd = root.findViewById(R.id.btn_add);
        clContainerSnackbar = root.findViewById(R.id.cl_container_snackbar);
        rvTaskList = root.findViewById(R.id.rv_task_note_list);
        db = new TTNoteDatabase(getContext());
        notes = db.getAllTaskNotes();

        // set adapter
        noteAdapter = new NoteAdapter(notes, this, UPDATE_TASK_NOTE_CODE);
        rvTaskList.setAdapter(noteAdapter);
        rvTaskList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        //set event
        enableSwipeToDeleteAndUndo();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskNoteAdditionActivity.class);
                startActivityForResult(intent, ADD_TASK_NOTE_CODE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("requestCode", UPDATE_TASK_NOTE_CODE);
                startActivityForResult(intent, SEARCH_TASK_NOTE_CODE);
                return false;
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_TASK_NOTE_CODE) {
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");
                db.addTaskNote(note);
                notes.clear();
                notes.addAll(db.getAllTaskNotes());
                noteAdapter.notifyDataSetChanged();
            }
            if (requestCode == UPDATE_TASK_NOTE_CODE){
                Bundle bundle = data.getExtras();
                NoteModel note = (NoteModel) bundle.getSerializable("note");
                db.updateTaskNote(note);

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
        if(requestCode == SEARCH_TASK_NOTE_CODE){
            notes.clear();
            notes.addAll(db.getAllTaskNotes());
            noteAdapter.notifyDataSetChanged();
        }
    }

    private void enableSwipeToDeleteAndUndo(){
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
        itemTouchHelper.attachToRecyclerView(rvTaskList);
    }

    private void showSnackBar(final int position,final NoteModel note){
        Snackbar snackbar = Snackbar
                .make(clContainerSnackbar, "Task note was removed from the list.", Snackbar.LENGTH_LONG);
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