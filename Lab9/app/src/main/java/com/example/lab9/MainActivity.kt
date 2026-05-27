package com.example.lab9

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.lab9.adapters.UserAdapter
import com.example.lab9.api.RetrofitClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var lastUpdateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        lastUpdateText = findViewById(R.id.lastUpdateText)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        swipeRefresh.setOnRefreshListener {
            loadUsers()
        }

        loadUsers()
    }

    private fun loadUsers() {

        lifecycleScope.launch {

            swipeRefresh.isRefreshing = true

            try {

                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getUsers()
                }

                if (response.isSuccessful) {

                    val users = response.body() ?: emptyList()

                    recyclerView.adapter = UserAdapter(users)

                    updateLastUpdateTime()

                } else {

                    lastUpdateText.text = "Server error: ${response.code()}"
                }

            } catch (e: Exception) {

                lastUpdateText.text = "Network error: ${e.message}"
            }

            swipeRefresh.isRefreshing = false
        }
    }

    private fun updateLastUpdateTime() {

        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val time = formatter.format(Date())

        lastUpdateText.text = "Last update: $time"
    }
}
