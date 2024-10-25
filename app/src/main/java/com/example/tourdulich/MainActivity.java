package com.example.tourdulich;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnAnHienMK;
    private EditText editTextPass;
    private boolean isPassShow = false;
    private Button btnQuayLaiTrangChu;
    private TextView textQuenPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_dang_nhap);

        //Ẩn hiện password
        btnAnHienMK = findViewById(R.id.imgBtAnHienMatKhau);
        editTextPass = findViewById(R.id.editTextPasswordDangNhap);
        btnAnHienMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPassShow){
                    //Show password
                    editTextPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    btnAnHienMK.setImageResource(R.drawable.eye_24);
                }else{
                    //Hide password
                    editTextPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnAnHienMK.setImageResource(R.drawable.visibility_off_24);
                }
                isPassShow = !isPassShow;
                editTextPass.setSelection(editTextPass.length()); //Chọn vị trí cuối trong text
            }
        });

        //Quay lại TRANG CHỦ
        Intent trangChu = new Intent(this, TrangChu.class);
        btnQuayLaiTrangChu = findViewById(R.id.btQuayLaiTuDangNhap);
        btnQuayLaiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start activity TrangChu
                startActivity(trangChu);
            }
        });

        //Chuyển sang TRANG QUÊN MẬT KHẨU
        Intent trangQuenPass = new Intent(this, TrangQuenMK.class);
        textQuenPass = findViewById(R.id.textQuenPass);
        textQuenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start activity TrangQuenMK
                startActivity(trangQuenPass);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}