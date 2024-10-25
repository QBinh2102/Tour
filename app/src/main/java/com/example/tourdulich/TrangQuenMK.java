package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrangQuenMK extends AppCompatActivity {

    private Button btnQuayLai;
    private TextView linkFindByMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_quen_mk);

        //Quay lại TRANG ĐĂNG NHẬP
        Intent trangDangNhap = new Intent(this, MainActivity.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuQuenMatKhau);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start activity MainActivity
                startActivity(trangDangNhap);
            }
        });

        //Chuyển sang TRANG QUÊN MẬT KHẨU BẰNG MAIL
        Intent trangQuenPassBangMail = new Intent(this, TrangQuenMKBangEmail.class);
        linkFindByMail = findViewById(R.id.textViewLinkTimBangMail);
        linkFindByMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start activity MainActivity
                startActivity(trangQuenPassBangMail);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}