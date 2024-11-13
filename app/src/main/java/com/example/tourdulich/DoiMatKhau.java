package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DoiMatKhau extends AppCompatActivity {

    private Button btnQuayLai;
    private ImageButton ibtAnHienMK;
    private ImageButton ibtAnHienXacThucMK;
    private EditText textPass;
    private EditText textConfirmPass;
    private boolean isPassShow = false;
    private boolean isConfirmPassShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doi_mat_khau);

        btnQuayLai = findViewById(R.id.btQuayLaiTuThayDoiMK);
        ibtAnHienMK = findViewById(R.id.imgBtAnHienPassThayDoi);
        ibtAnHienXacThucMK = findViewById(R.id.imgBtAnHienXacThucPassThayDoi);
        textPass = findViewById(R.id.editTextPassThayDoi);
        textConfirmPass = findViewById(R.id.editTextPassXacThucThayDoi);

        //Quay lai THÔNG TIN CÁ NHÂN
        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ttcn);
            }
        });

        //Ẩn hiện password
        ibtAnHienMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPassShow){
                    textPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    ibtAnHienMK.setImageResource(R.drawable.eye_24);
                }else{
                    textPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ibtAnHienMK.setImageResource(R.drawable.visibility_off_24);
                }
                isPassShow=!isPassShow;
                textPass.setSelection(textPass.length());
            }
        });

        //Ẩn hiện xác thực password
        ibtAnHienXacThucMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConfirmPassShow){
                    textConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    ibtAnHienXacThucMK.setImageResource(R.drawable.eye_24);
                }else{
                    textConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ibtAnHienXacThucMK.setImageResource(R.drawable.visibility_off_24);
                }
                isConfirmPassShow=!isConfirmPassShow;
                textConfirmPass.setSelection(textConfirmPass.length());
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}