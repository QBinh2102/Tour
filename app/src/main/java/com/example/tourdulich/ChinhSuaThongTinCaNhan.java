package com.example.tourdulich;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChinhSuaThongTinCaNhan extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnCapNhat;
    private RadioButton RbtNam;
    private RadioButton RbtNu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chinh_sua_thong_tin_ca_nhan);

        TextView tv = findViewById(R.id.textDate);
        tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        //Quay lại THÔNG TIN CHƯA ĐĂNG NHẬP
        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuChinhSuaThongTinCaNhan);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ttcn);
            }
        });

        //Chọn giới tính
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