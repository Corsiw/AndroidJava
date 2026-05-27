package com.example.ex3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex3.Model.Task;
import com.example.ex3.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Task task);

        void onStatusChanged(Task task, boolean isCompleted);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return tasks.size();
            }

            @Override
            public int getNewListSize() {
                return newTasks.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return tasks.get(oldItemPosition).id == newTasks.get(newItemPosition).id;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Task oldTask = tasks.get(oldItemPosition);
                Task newTask = newTasks.get(newItemPosition);
                return oldTask.title.equals(newTask.title) && oldTask.description.equals(newTask.description) && oldTask.priority == newTask.priority && oldTask.isCompleted == newTask.isCompleted;
            }
        });

        tasks.clear();
        tasks.addAll(newTasks);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.titleText.setText(task.title);
        holder.descriptionText.setText(task.description);
        holder.priorityText.setText("Приоритет: " + task.priority);

        holder.completedCheck.setOnClickListener(null);
        holder.completedCheck.setChecked(task.isCompleted);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(task);
            }
        });

        holder.completedCheck.setOnClickListener(v -> {
            boolean isChecked = holder.completedCheck.isChecked();
            task.isCompleted = isChecked; // Обновляем локальную модель
            if (listener != null) {
                listener.onStatusChanged(task, isChecked); // Передаем событие во фрагмент
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView descriptionText;
        TextView priorityText;
        CheckBox completedCheck;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.taskTitle);
            descriptionText = itemView.findViewById(R.id.taskDescription);
            priorityText = itemView.findViewById(R.id.taskPriority);
            completedCheck = itemView.findViewById(R.id.taskCompleted);
        }
    }
}