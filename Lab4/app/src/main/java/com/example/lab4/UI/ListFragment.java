package com.example.lab4.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab4.Model.Item;
import com.example.lab4.R;
import com.example.lab4.ViewModel.ItemAdapter;
import com.example.lab4.ViewModel.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListFragment extends Fragment {

    private ItemViewModel viewModel;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;


    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
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
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        TextView lastUpdateText = view.findViewById(R.id.lastUpdateText);
        FloatingActionButton button = view.findViewById(R.id.postFloatingButton);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        viewModel.fetchItems();

        if (viewModel.getIsLoadingLiveData().getValue() == null || !viewModel.getIsLoadingLiveData().getValue()) {
            viewModel.fetchItems();
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
        
        viewModel.getItemsLiveData().observe(getViewLifecycleOwner(), items -> {
            swipeRefreshLayout.setRefreshing(false);
            viewModel.setIsLoading(false);

            if (items != null) {
                adapter.setItems(items);

                String time = new SimpleDateFormat("HH:mm:ss",
                        Locale.getDefault()).format(new Date());

                lastUpdateText.setText(getContext().getString(R.string.last_update_text, time));
            }
        });

        adapter.setOnItemClickListener(item -> {
            Log.i("ItemClicked", String.format("Clicked Item id: %d", item.getId()));
        });

        button.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, PostFragment.newInstance())
                .addToBackStack(null)
                .commit());
    }

    private void updateList(List<Item> items) {
        if (items != null) {
            adapter.setItems(items);
        }
    }
}