package com.example.ex3.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ex3.Adapter.TaskAdapter;
import com.example.ex3.Model.Task;
import com.example.ex3.R;
import com.example.ex3.ViewModel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskListFragment extends Fragment {
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    public TaskListFragment() {
    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        view.findViewById(R.id.btnSort).setOnClickListener(v -> showSortMenu(v));
        view.findViewById(R.id.btnFilter).setOnClickListener(v -> showFilterMenu(v));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton addButton = view.findViewById(R.id.addTaskButton);

        adapter = new TaskAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> adapter.setTasks(tasks));

        addButton.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, AddEditTaskFragment.newInstance())
                .addToBackStack(null)
                .commit());
        

        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Bundle bundle = new Bundle();
                bundle.putInt("taskId", task.id);

                TaskDetailsFragment fragment = TaskDetailsFragment.newInstance(task.id);
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }

            @Override
            public void onStatusChanged(Task task, boolean isCompleted) {
                taskViewModel.update(task);
            }
        });
    }

    private void showSortMenu(View v) {
        PopupMenu popup = new PopupMenu(requireContext(), v);
        popup.getMenu().add("По умолчанию");
        popup.getMenu().add("По приоритету (сначала важные)");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("По приоритету (сначала важные)")) {
                taskViewModel.setFilterMode("PRIORITY");
            } else {
                taskViewModel.setFilterMode("ALL");
            }
            return true;
        });
        popup.show();
    }

    private void showFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(requireContext(), v);
        popup.getMenu().add("Все задачи");
        popup.getMenu().add("Только выполненные");
        popup.getMenu().add("Только текущие");

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getTitle().toString()) {
                case "Только выполненные":
                    taskViewModel.setFilterMode("COMPLETED");
                    break;
                case "Только текущие":
                    taskViewModel.setFilterMode("INCOMPLETE");
                    break;
                default:
                    taskViewModel.setFilterMode("ALL");
                    break;
            }
            return true;
        });
        popup.show();
    }
}