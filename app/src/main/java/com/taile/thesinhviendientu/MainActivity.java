package com.taile.thesinhviendientu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewQRCode;
    private TextView textViewStudentName, textViewStudentId;
    private Button buttonEditInfo;
    private SharedPreferences sharedPreferences;

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

        // Initialize views
        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        textViewStudentName = findViewById(R.id.textViewStudentName);
        textViewStudentId = findViewById(R.id.textViewStudentId);
        buttonEditInfo = findViewById(R.id.buttonEditInfo);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("StudentInfo", MODE_PRIVATE);

        // Set button click listener
        buttonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentInfo();
    }

    private void loadStudentInfo() {
        String name = sharedPreferences.getString("name", "");
        String studentId = sharedPreferences.getString("studentId", "");

        // Check if information exists
        if (name.isEmpty() || studentId.isEmpty()) {
            // No info found, redirect to EditInfoActivity
            Intent intent = new Intent(this, EditInfoActivity.class);
            startActivity(intent);
            return;
        }

        // Display information
        textViewStudentName.setText("Họ và tên: " + name);
        textViewStudentId.setText("MSSV: " + studentId);

        // Generate QR Code
        generateQRCode(studentId);
    }

    private void generateQRCode(String studentId) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    studentId,
                    BarcodeFormat.QR_CODE,
                    500,
                    500
            );

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQRCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}