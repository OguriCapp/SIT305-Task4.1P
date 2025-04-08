package com.example.taskmanagerapp224385035;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {
    // All the CRUD operations
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button dueDateButton;
    private Button saveButton;
    private Button deleteButton;

    private Task task;

    private TaskDatabaseHelper dbHelper;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        task = (Task) getIntent().getSerializableExtra("task");
        dbHelper = new TaskDatabaseHelper(this);
        calendar = Calendar.getInstance();

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateButton = findViewById(R.id.dueDateButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Old task's information
        if (task != null) {
            titleEditText.setText(task.getTitle());
            descriptionEditText.setText(task.getDescription());
            calendar.setTime(task.getDueDate());
            updateDueDateButton();
        }

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
    }

    // Show the detaiiled date
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDueDateButton();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Update the date
    private void updateDueDateButton() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        dueDateButton.setText(dateFormat.format(calendar.getTime()));
    }

    // To save the task
    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (task == null) {
            task = new Task();
        }

        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(calendar.getTime());

        // Save tasks to database
        if (task.getId() == 0) {
            dbHelper.addTask(task);
        } else {
            dbHelper.updateTask(task);
        }

        finish();
    }

    // Remove the task from the database
    private void deleteTask() {
        if (task != null && task.getId() != 0) {
            dbHelper.deleteTask(task.getId());
        }
        finish();
    }
} 