package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrangChu extends AppCompatActivity {

    private Button btnDangNhap;
    private Button btnDangKy;
    private LinearLayout btnToiHoSo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        //Chuyển sang TRANG ĐĂNG NHẬP
        Intent dangNhap = new Intent(this, MainActivity.class);
        btnDangNhap = findViewById(R.id.btDangNhapTuTrangChu);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dangNhap);
            }
        });

        //Chuyển sang TRANG ĐĂNG KÝ
        Intent dangKy = new Intent(this, TrangDangKy.class);
        btnDangKy = findViewById(R.id.btDangKyTuTrangChu);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dangKy);
            }
        });

        //Chuyển sang TRANG THÔNG TIN CÁ NHÂN
        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
        btnToiHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ttcn);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}