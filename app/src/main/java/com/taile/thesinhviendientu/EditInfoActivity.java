package com.taile.thesinhviendientu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class EditInfoActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutName, textInputLayoutStudentId;
    private TextInputEditText editTextName, editTextStudentId;
    private Button buttonSave;
    private SharedPreferences sharedPreferences;

    // Regular expression pattern for MSSV validation (8 digits only)
    private static final Pattern MSSV_PATTERN = Pattern.compile("^\\d{8}$");

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
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutStudentId = findViewById(R.id.textInputLayoutStudentId);
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

        // Add text change listeners for real-time validation
        setupInputValidation();

        // Save button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveStudentInfo();
                }
            }
        });
    }

    private void setupInputValidation() {
        // Validate MSSV as user types
        editTextStudentId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateStudentId(s.toString());
            }
        });

        // Validate name as user types
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateName(s.toString());
            }
        });
    }

    private boolean validateName(String name) {
        if (name.trim().isEmpty()) {
            textInputLayoutName.setError("Vui lòng nhập họ tên");
            return false;
        }

        if (name.trim().length() < 3) {
            textInputLayoutName.setError("Họ tên quá ngắn");
            return false;
        }

        textInputLayoutName.setError(null);
        return true;
    }

    private boolean validateStudentId(String studentId) {
        if (studentId.trim().isEmpty()) {
            textInputLayoutStudentId.setError("Vui lòng nhập MSSV");
            return false;
        }

        if (!MSSV_PATTERN.matcher(studentId).matches()) {
            textInputLayoutStudentId.setError("MSSV phải có đúng 8 chữ số");
            return false;
        }

        textInputLayoutStudentId.setError(null);
        return true;
    }

    private boolean validateInputs() {
        boolean isNameValid = validateName(editTextName.getText().toString());
        boolean isStudentIdValid = validateStudentId(editTextStudentId.getText().toString());

        return isNameValid && isStudentIdValid;
    }

    private void saveStudentInfo() {
        String name = editTextName.getText().toString().trim();
        String studentId = editTextStudentId.getText().toString().trim();

        // Save to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("studentId", studentId);
        editor.apply();

        Toast.makeText(this, "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show();

        // Navigate back to MainActivity
        Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
