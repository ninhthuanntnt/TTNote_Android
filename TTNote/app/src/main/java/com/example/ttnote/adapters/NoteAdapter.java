package com.example.ttnote.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.DeletedNoteActivity;
import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.example.ttnote.NoteAdditionActivity;
import com.example.ttnote.R;
import com.example.ttnote.RemindNoteAdditionActivity;
import com.example.ttnote.TaskNoteAdditionActivity;
import com.example.ttnote.ui.home.HomeFragment;
import com.example.ttnote.ui.recycle_bin.RecycleBinFragment;
import com.example.ttnote.ui.remind.RemindFragment;
import com.example.ttnote.ui.task.TaskFragment;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<NoteModel> notes;
    private Context context;
    private Fragment fragment;
    private int updateCode = 0;

    private static final int UPDATE_NOTE_CODE = 6515;
    private static final int UPDATE_TASK_NOTE_CODE = 9541;
    private static final int UPDATE_REMIND_NOTE_CODE = 6012;
    private static final int UPDATE_DELETED_NOTE_CODE = 3030;

    public NoteAdapter(ArrayList<NoteModel> notes, Fragment fragment, int updateCode) {
        this.notes = notes;
        this.fragment = fragment;
        this.updateCode = updateCode;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvCreatedDate;
        private MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_date);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.list_row, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final NoteModel note = notes.get(position);
        viewHolder.tvTitle.setText(note.getTitle());
        if (note.getContent().length() > 100)
            viewHolder.tvContent.setText(note.getContent().substring(0, 99) + "...");
        else
            viewHolder.tvContent.setText(note.getContent());
        final Date createdDate = new Date(note.getCreatedDate());
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        viewHolder.tvCreatedDate.setText(format.format(createdDate));

        viewHolder.cardView.setCardBackgroundColor(note.getBackground());
        if (note.getDate() != 0) {
            String remindDate = format.format(new Date(note.getDate()));
            viewHolder.tvCreatedDate.setText(Html.fromHtml("<p>" + format.format(createdDate) + "</p>" + "<b>" + "REMIND YOU AT " + remindDate + "</b>"));
        }
        if (note.getTasks() != null && note.getTasks().size() > 0) {

            StringBuilder content = new StringBuilder();

            if (note.getContent().length() > 50) {
                String noteContent = note.getContent().substring(0, 50) + "...";
                String[] noteContentSplit = noteContent.split("\n");
                for (int i = 0; i < noteContentSplit.length; i++) {
                    content.append("<span>" + noteContentSplit[i] + "</span><br/>");
                }
            } else {
                String[] noteContentSplit = note.getContent().split("\n");
                for (int i = 0; i < noteContentSplit.length; i++) {
                    content.append("<span>" + noteContentSplit[i] + "</span><br/>");
                }
            }
            content.append("<br/><u><b>Your tasks:</b></u><br/>");
            for (TaskModel taskTemp : note.getTasks()) {
                if (taskTemp.isStatus()) {
                    content.append("<span>" + "- " + "<span style='text-decoration:line-through'>" + taskTemp.getTaskName() + "</span>" + "</span>" + "<br/>");
                } else {
                    content.append("<span>" + "- " + taskTemp.getTaskName() + "</span><br/>");
                }
            }

            viewHolder.tvContent.setText(Html.fromHtml(content.substring(0, content.length() - 5)));
        }
        //event
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                if (note.getDate() == 0)
                    intent = new Intent(context, NoteAdditionActivity.class);
                if (note.getTasks() != null && note.getTasks().size() > 0)
                    intent = new Intent(context, TaskNoteAdditionActivity.class);
                if (note.getDate() != 0)
                    intent = new Intent(context, RemindNoteAdditionActivity.class);
                if (!note.isStatus())
                    intent = new Intent(context, DeletedNoteActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("note", notes.get(position));
                intent.putExtras(bundle);

                if (fragment != null) {
                    if (fragment instanceof HomeFragment)
                        fragment.startActivityForResult(intent, UPDATE_NOTE_CODE);
                    if (fragment instanceof TaskFragment)
                        fragment.startActivityForResult(intent, UPDATE_TASK_NOTE_CODE);
                    if (fragment instanceof RemindFragment)
                        fragment.startActivityForResult(intent, UPDATE_REMIND_NOTE_CODE);
                    if (fragment instanceof RecycleBinFragment)
                        fragment.startActivityForResult(intent, UPDATE_DELETED_NOTE_CODE);
                } else {
                    // start activity for in SearchActivity
                    ((Activity) context).startActivityForResult(intent, updateCode);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}
