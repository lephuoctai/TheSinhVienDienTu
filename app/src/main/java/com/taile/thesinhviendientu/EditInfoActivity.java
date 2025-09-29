package com.taile.thesinhviendientu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class EditInfoActivity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextStudentId;
    private Button buttonSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_info_layout), (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("StudentInfo", MODE_PRIVATE);

        // Load existing data if available
        String name = sharedPreferences.getString("name", "");
        String studentId = sharedPreferences.getString("studentId", "");

        editTextName.setText(name);
        editTextStudentId.setText(studentId);

        // Save button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudentInfo();
            }
        });
    }

    private void saveStudentInfo() {
        String name = editTextName.getText().toString().trim();
        String studentId = editTextStudentId.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || studentId.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("studentId", studentId);
        editor.apply();

        // Navigate back to MainActivity
        Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
