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

public class TrangDangKy extends AppCompatActivity {

    private Button btnQuayLai;
    private ImageButton iBtAnHienMK;
    private ImageButton iBtAnHienXacThucMK;
    private EditText textPass;
    private EditText textConfirmPass;
    private boolean isPassShow = false;
    private boolean isConfirmPassShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_dang_ky);

        //Quay lại TRANG CHỦ
        Intent trangChu = new Intent(this, TrangChu.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuDangKy);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(trangChu);
            }
        });

        //Ẩn hiện password
        iBtAnHienMK = findViewById(R.id.imgBtAnHienPassDangKy);
        textPass = findViewById(R.id.editTextPasswordDangKy);
        iBtAnHienMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPassShow){
                    //Show password
                    textPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    iBtAnHienMK.setImageResource(R.drawable.eye_24);
                }else{
                    //Hide password
                    textPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iBtAnHienMK.setImageResource(R.drawable.visibility_off_24);
                }
                isPassShow = !isPassShow;
                textPass.setSelection(textPass.length()); //Chọn vị trí cuối trong text
            }
        });

        //Ẩn hiện xác thực password
        iBtAnHienXacThucMK = findViewById(R.id.imgBtAnHienXacThucPassDangKy);
        textConfirmPass = findViewById(R.id.editTextPasswordXacThucDangKy);
        iBtAnHienXacThucMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConfirmPassShow){
                    //Show password
                    textConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    iBtAnHienXacThucMK.setImageResource(R.drawable.eye_24);
                }else{
                    //Hide password
                    textConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iBtAnHienXacThucMK.setImageResource(R.drawable.visibility_off_24);
                }
                isConfirmPassShow = !isConfirmPassShow;
                textConfirmPass.setSelection(textConfirmPass.length()); //Chọn vị trí cuối trong text
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}