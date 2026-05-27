package com.example.ex3.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ex3.Model.Task;
import com.example.ex3.R;
import com.example.ex3.ViewModel.TaskViewModel;

public class TaskDetailsFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private Task currentTask;

    private EditText titleEdit;
    private EditText descriptionEdit;
    private Spinner prioritySpinner;
    private CheckBox completedCheck;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(int taskId) {
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("taskId", taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        titleEdit = view.findViewById(R.id.detailsTitle);
        descriptionEdit = view.findViewById(R.id.detailsDescription);
        prioritySpinner = view.findViewById(R.id.detailsPriority);
        completedCheck = view.findViewById(R.id.detailsCompleted);

        String[] priorities = {"Высокий", "Средний", "Низкий"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        
        Button updateButton = view.findViewById(R.id.updateButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        int taskId = getArguments() != null ? getArguments().getInt("taskId") : -1;
        
        taskViewModel.getTaskById(taskId).observe(getViewLifecycleOwner(), task -> {
            if (task != null && currentTask == null) {
                currentTask = task;

                titleEdit.setText(task.title);
                descriptionEdit.setText(task.description);
                completedCheck.setChecked(task.isCompleted);
                prioritySpinner.setSelection(task.priority - 1);
            }
        });
        
        updateButton.setOnClickListener(v -> {
            if (currentTask != null) {
                String titleStr = titleEdit.getText().toString().trim();
                String descStr = descriptionEdit.getText().toString().trim();

                if (titleStr.isEmpty()) {
                    titleEdit.setError("Заголовок не может быть пустым");
                    return;
                }
                
                currentTask.title = titleStr;
                currentTask.description = descStr;
                currentTask.isCompleted = completedCheck.isChecked();
                currentTask.priority = prioritySpinner.getSelectedItemPosition() + 1;
                
                taskViewModel.update(currentTask);
                Toast.makeText(getContext(), "Задача обновлена", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack(); // Возврат назад после сохранения
            }
        });
        
        deleteButton.setOnClickListener(v -> {
            if (currentTask != null) {
                taskViewModel.delete(currentTask);
                Toast.makeText(getContext(), "Задача удалена", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });
    }
}