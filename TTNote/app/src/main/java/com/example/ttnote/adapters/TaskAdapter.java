package com.example.ttnote.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnClear = itemView.findViewById(R.id.btn_clear_task);
            edtTaskName = itemView.findViewById(R.id.edt_task_name);
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
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder viewHolder, int position) {
        final TaskModel task = tasks.get(position);
        if(task.getTaskName() != null && !task.getTaskName().isEmpty() ){
            viewHolder.edtTaskName.setText(task.getTaskName());
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
