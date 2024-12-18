package com.example.tourdulich.Page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.tourdulich.R;

public class ThongBaoThanhToan3 extends AppCompatActivity {

    private TextView txtThongBao;
    private Button btnQuayLaiTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao_thanh_toan3);

        btnQuayLaiTC = findViewById(R.id.btn_back_home);
        txtThongBao = findViewById(R.id.tv_success_title);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        txtThongBao.setText(result);


        //Quay lại trang chủ
        btnQuayLaiTC.setOnClickListener(v -> {
            Intent intentBack = new Intent(ThongBaoThanhToan3.this, TrangChu.class);
            startActivity(intentBack);
        });
    }
}
