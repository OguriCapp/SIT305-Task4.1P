package com.example.taskmanagerapp224385035;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private TaskDatabaseHelper dbHelper;
    private LinearLayout welcomeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new TaskDatabaseHelper(this);
        
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        List<Task> tasks = dbHelper.getAllTasks();
        taskAdapter = new TaskAdapter(tasks, this);
        taskRecyclerView.setAdapter(taskAdapter);

        welcomeLayout = findViewById(R.id.welcomeLayout);
        updateWelcomeTextVisibility(tasks.size());

        FloatingActionButton addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> tasks = dbHelper.getAllTasks();
        taskAdapter.updateTasks(tasks);
        updateWelcomeTextVisibility(tasks.size());
    }

    private void updateWelcomeTextVisibility(int taskCount) {
        if (welcomeLayout != null) {
            welcomeLayout.setVisibility(taskCount == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivity(intent);
    }
}