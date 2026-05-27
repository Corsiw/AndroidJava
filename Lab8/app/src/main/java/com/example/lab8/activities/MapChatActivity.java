package com.example.lab8.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab8.R;
import com.example.lab8.adapters.MessageAdapter;
import com.example.lab8.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class MapChatActivity extends AppCompatActivity {

    private MapView map;

    private RecyclerView recycler;
    private EditText input;
    private Button send;

    private DatabaseReference chatRef;
    private FirebaseAuth auth;

    private final List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
        
        setContentView(R.layout.activity_map_chat);

        map = findViewById(R.id.mapView);
        map.setMultiTouchControls(true);

        if (savedInstanceState != null) {
            double lat = savedInstanceState.getDouble("map_lat", 55.7558);
            double lon = savedInstanceState.getDouble("map_lon", 37.6173);
            double zoom = savedInstanceState.getDouble("map_zoom", 10.0);

            GeoPoint savedPoint = new GeoPoint(lat, lon);
            map.getController().setCenter(savedPoint);
            map.getController().setZoom(zoom);
        } else {
            GeoPoint startPoint = new GeoPoint(55.7558, 37.6173);
            map.getController().setCenter(startPoint);
            map.getController().setZoom(10.0);
        }
                
        recycler = findViewById(R.id.chatRecycler);
        input = findViewById(R.id.messageInput);
        send = findViewById(R.id.sendButton);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(messages);
        recycler.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        chatRef = FirebaseDatabase.getInstance().getReference("chat");
        
        send.setOnClickListener(v -> sendMessage());

        listenMessages();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            outState.putDouble("map_lat", map.getMapCenter().getLatitude());
            outState.putDouble("map_lon", map.getMapCenter().getLongitude());
            outState.putDouble("map_zoom", map.getZoomLevelDouble());
        }
    }
    
    private void sendMessage() {

        String text = input.getText().toString();
        String user = auth.getCurrentUser().getEmail();

        Message msg = new Message(user, text);

        chatRef.push().setValue(msg);

        input.setText("");
    }

    private void listenMessages() {

        chatRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {

                Message msg = snapshot.getValue(Message.class);

                messages.add(msg);
                adapter.notifyDataSetChanged();
            }

            public void onChildChanged(@NonNull DataSnapshot d, String s) {
            }

            public void onChildRemoved(@NonNull DataSnapshot d) {
            }

            public void onChildMoved(@NonNull DataSnapshot d, String s) {
            }

            public void onCancelled(@NonNull DatabaseError e) {
            }
        });
    }
}