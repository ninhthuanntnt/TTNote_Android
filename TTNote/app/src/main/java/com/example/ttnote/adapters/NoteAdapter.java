package com.example.ttnote.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.NoteAdditionActivity;
import com.example.ttnote.R;
import com.example.ttnote.RemindNoteAdditionActivity;
import com.example.ttnote.TaskNoteAdditionActivity;
import com.example.ttnote.ui.home.HomeFragment;
import com.example.ttnote.ui.remind.RemindFragment;
import com.example.ttnote.ui.task.TaskFragment;

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

    public NoteAdapter(ArrayList<NoteModel> notes, Fragment fragment, int updateCode) {
        this.notes = notes;
        this.fragment = fragment;
        this.updateCode = updateCode;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvCreatedDate;
        private LinearLayout llCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCreatedDate = itemView.findViewById(R.id.tv_created_date);
            llCard = itemView.findViewById(R.id.ll_card);
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
        viewHolder.tvContent.setText(note.getContent());

        final Date createdDate = new Date(note.getCreatedDate());
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:ss a");

        viewHolder.tvCreatedDate.setText(format.format(createdDate));

        Drawable cardItemBorder = DrawableCompat.wrap(context.getResources().getDrawable(R.drawable.card_view_border));
        DrawableCompat.setTint(cardItemBorder, note.getBackground());
        viewHolder.llCard.setBackground(cardItemBorder);
        if(updateCode == UPDATE_REMIND_NOTE_CODE){
            String remindDate = format.format(new Date(note.getDate()));
            viewHolder.tvCreatedDate.setText(format.format(createdDate) + "  |  " +
                    "REMIND YOU AT "  + remindDate);
        }
        //event
        viewHolder.llCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                if (updateCode == UPDATE_NOTE_CODE)
                    intent = new Intent(context, NoteAdditionActivity.class);
                if(updateCode == UPDATE_TASK_NOTE_CODE)
                    intent = new Intent(context, TaskNoteAdditionActivity.class);
                if(updateCode == UPDATE_REMIND_NOTE_CODE)
                    intent = new Intent(context, RemindNoteAdditionActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("note",notes.get(position));
                intent.putExtras(bundle);

                if(fragment != null) {
                    if(fragment instanceof HomeFragment)
                        fragment.startActivityForResult(intent, UPDATE_NOTE_CODE);
                    if(fragment instanceof TaskFragment)
                        fragment.startActivityForResult(intent, UPDATE_TASK_NOTE_CODE);
                    if(fragment instanceof RemindFragment)
                        fragment.startActivityForResult(intent, UPDATE_REMIND_NOTE_CODE);
                }else{
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
