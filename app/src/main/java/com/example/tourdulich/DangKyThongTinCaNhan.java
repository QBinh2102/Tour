package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DangKyThongTinCaNhan extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnTiepTuc;
    private RadioButton RbtNam;
    private RadioButton RbtNu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky_thong_tin_ca_nhan);

        //Quay lại THÔNG TIN CHƯA ĐĂNG NHẬP
        Intent ttcdn = new Intent(this, ThongTinChuaDangNhap.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuDangKyThongTinCaNhan);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ttcdn);
            }
        });

        //Chuyển sang ĐĂNG KÝ
        Intent dangKy = new Intent(this, TrangDangKy.class);
        btnTiepTuc = findViewById(R.id.btTiepTucDangKyTTCN);
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dangKy);
            }
        });

        RbtNam = findViewById(R.id.radioButtonNam);
        RbtNu = findViewById(R.id.radioButtonNu);
        RbtNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RbtNu.setChecked(false);
                RbtNam.setChecked(true);
            }
        });
        RbtNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RbtNam.setChecked(false);
                RbtNu.setChecked(true);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}