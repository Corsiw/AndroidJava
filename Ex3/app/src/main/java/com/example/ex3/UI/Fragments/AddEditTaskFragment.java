package com.example.ex3.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter; // Добавлено
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner; // Добавлено
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ex3.Model.Task;
import com.example.ex3.R;
import com.example.ex3.ViewModel.TaskViewModel;

public class AddEditTaskFragment extends Fragment {

    private TaskViewModel taskViewModel;

    private EditText titleInput;
    private EditText descriptionInput;
    private Spinner prioritySpinner;
    private CheckBox completedCheck;

    public AddEditTaskFragment() {
        // Required empty public constructor
    }

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edt_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        titleInput = view.findViewById(R.id.inputTitle);
        descriptionInput = view.findViewById(R.id.inputDescription);
        prioritySpinner = view.findViewById(R.id.inputPriority);
        completedCheck = view.findViewById(R.id.inputCompleted);
        Button saveButton = view.findViewById(R.id.saveButton);
        
        String[] priorities = {"Высокий", "Средний", "Низкий"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        // По умолчанию ставим "Средний" (индекс 1)
        prioritySpinner.setSelection(1);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (title.isEmpty()) {
                titleInput.setError("Введите заголовок");
                return;
            }
            
            int priority = prioritySpinner.getSelectedItemPosition() + 1;
            boolean isCompleted = completedCheck.isChecked();

            Task task = new Task(title, description, priority, isCompleted);
            
            taskViewModel.insert(task);

            Toast.makeText(getContext(), "Задача сохранена", Toast.LENGTH_SHORT).show();
            
            getParentFragmentManager().popBackStack();
        });
    }
}