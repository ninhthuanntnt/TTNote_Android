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
import com.example.ttnote.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<NoteModel> notes;
    private Context context;
    private Fragment fragment;

    public static final int UPDATE_NOTE_CODE = 6515;

    public NoteAdapter(ArrayList<NoteModel> notes, Fragment fragment) {
        this.notes = notes;
        this.fragment = fragment;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final NoteModel note = notes.get(position);
        viewHolder.tvTitle.setText(note.getTitle());
        viewHolder.tvContent.setText(note.getContent());

        Date createdDate = new Date(note.getCreatedDate());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:ss a");

        viewHolder.tvCreatedDate.setText(format.format(createdDate));

        Drawable cardItemBorder = DrawableCompat.wrap(context.getResources().getDrawable(R.drawable.card_view_border));
        DrawableCompat.setTint(cardItemBorder, note.getBackground());
        viewHolder.llCard.setBackground(cardItemBorder);

        //event
        viewHolder.llCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NoteAdditionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note",note);
                intent.putExtras(bundle);
                fragment.startActivityForResult(intent,UPDATE_NOTE_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



}
