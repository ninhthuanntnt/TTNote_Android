package com.example.ttnote.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttnote.Model.NoteModel;
import com.example.ttnote.Model.TaskModel;
import com.example.ttnote.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<TaskModel> tasks;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton btnClear;
        EditText edtTaskName;
        CheckBox cbTaskCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnClear = itemView.findViewById(R.id.btn_clear_task);
            edtTaskName = itemView.findViewById(R.id.edt_task_name);
            cbTaskCheck = itemView.findViewById(R.id.cb_task_check);
        }
    }

    public TaskAdapter(ArrayList<TaskModel> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.list_task_row, parent, false);

        return new TaskAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.ViewHolder viewHolder, int position) {
        final TaskModel task = tasks.get(position);
        if(task.getTaskName() != null && !task.getTaskName().isEmpty() ){
            viewHolder.edtTaskName.setText(task.getTaskName());
            if(task.isStatus()){
                viewHolder.cbTaskCheck.setChecked(true);
                viewHolder.edtTaskName.setPaintFlags(viewHolder.edtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                viewHolder.edtTaskName.setPaintFlags(viewHolder.edtTaskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.cbTaskCheck.setChecked(false);
            }
        }
        // set event on item
        //onclick btn clear
        viewHolder.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(task);
                notifyDataSetChanged();
            }
        });
        viewHolder.cbTaskCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    viewHolder.edtTaskName.setPaintFlags(viewHolder.edtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    task.setStatus(true);
                }else{
                    viewHolder.edtTaskName.setPaintFlags(viewHolder.edtTaskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    task.setStatus(false);
                }
            }
        });
        // after change task name
        viewHolder.edtTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setTaskName(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
