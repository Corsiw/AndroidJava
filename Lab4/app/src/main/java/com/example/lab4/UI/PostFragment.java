package com.example.lab4.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab4.Model.Item;
import com.example.lab4.R;
import com.example.lab4.ViewModel.ItemViewModel;

public class PostFragment extends Fragment {

    private EditText userIdInput;
    private EditText titleInput;
    private EditText bodyInput;
    private Button sendButton;
    private TextView resultText;

    private ItemViewModel viewModel;

    public PostFragment() {
    }
    
    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        userIdInput = view.findViewById(R.id.userIdInput);
        titleInput = view.findViewById(R.id.titleInput);
        bodyInput = view.findViewById(R.id.bodyInput);
        sendButton = view.findViewById(R.id.sendPostButton);
        resultText = view.findViewById(R.id.resultText);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        sendButton.setOnClickListener(v -> sendPost());

        viewModel.getPostItemLiveData().observe(getViewLifecycleOwner(), post -> {

            if (post != null) {

                String result =
                        "Created post:\n\n" +
                                "User ID: " + post.getUserId() + "\n" +
                                "ID: " + post.getId() + "\n" +
                                "Title: " + post.getTitle() + "\n" +
                                "Body: " + post.getBody();

                resultText.setText(result);

                Toast.makeText(getContext(),
                        "POST success",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPost() {

        String userIdStr = userIdInput.getText().toString();
        String title = titleInput.getText().toString();
        String body = bodyInput.getText().toString();

        if (TextUtils.isEmpty(userIdStr) || TextUtils.isEmpty(title) || TextUtils.isEmpty(body)) {
            Toast.makeText(getContext(),
                    "Fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            userIdInput.setError("Year must be a number");
            userIdInput.requestFocus();
            return;
        }

        Item post = new Item();
        post.setUserId(userId);
        post.setTitle(title);
        post.setBody(body);

        viewModel.postItem(post);
    }
}