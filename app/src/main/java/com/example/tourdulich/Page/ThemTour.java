package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;

import java.util.Calendar;

public class ThemTour extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnTao;
    private TextView link1;
    private TextView link2;
    private TextView link3;
    private TextView txtNKH;
    private TextView txtNKT;
    private EditText edtTen;
    private EditText edtGia;
    private EditText edtSoVe;
    private EditText edtGioiThieu;
    private Spinner spinnerDanhMuc;
    private Spinner spinnerPhuongTien;

    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_tour);

        btnQuayLai = findViewById(R.id.btnQuayLaiQLTour);
        btnTao = findViewById(R.id.btnTaoTour);
        link1 = findViewById(R.id.linkHinh1);
        link2 = findViewById(R.id.linkHinh2);
        link3 = findViewById(R.id.linkHinh3);
        txtNKH = findViewById(R.id.txtNKHTour);
        txtNKT = findViewById(R.id.txtNKTTour);
        edtTen = findViewById(R.id.edtTenTour);
        edtGia = findViewById(R.id.edtGiaTienTour);
        edtSoVe = findViewById(R.id.edtSoLuongVe);
        edtGioiThieu = findViewById(R.id.edtGioiThieuTour);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        spinnerPhuongTien = findViewById(R.id.spinnerPhuongTien);

        txtNKH.setPaintFlags(txtNKH.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txtNKT.setPaintFlags(txtNKT.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        txtNKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(ThemTour.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtNKH.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                    }
                },year, month, day);
                picker.show();
            }
        });

        txtNKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(ThemTour.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtNKT.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                    }
                },year, month, day);
                picker.show();
            }
        });

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(ThemTour.this,
                R.array.cars, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhuongTien.setAdapter(roleAdapter);

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(ThemTour.this,QuanLyTour.class);
                startActivity(ql);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}