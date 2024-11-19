package com.example.tourdulich.trang;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhau extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnCapNhat;
    private ImageButton ibtAnHienMK;
    private ImageButton ibtAnHienXacThucMK;
    private EditText textNewPass;
    private EditText textConfirmPass;
    private boolean isNewPassShow = false;
    private boolean isConfirmPassShow = false;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doi_mat_khau);

        btnQuayLai = findViewById(R.id.btQuayLaiTuThayDoiMK);
        btnCapNhat = findViewById(R.id.btCapNhatMK);
        ibtAnHienMK = findViewById(R.id.imgBtAnHienPassThayDoi);
        ibtAnHienXacThucMK = findViewById(R.id.imgBtAnHienXacThucPassThayDoi);
        textNewPass = findViewById(R.id.editTextPassThayDoi);
        textConfirmPass = findViewById(R.id.editTextPassXacThucThayDoi);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent ttcn = new Intent(this, ThongTinCaNhan.class);

        //Quay lai THÔNG TIN CÁ NHÂN
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ttcn);
            }
        });

        //Ẩn hiện new password
        ibtAnHienMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNewPassShow){
                    textNewPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    ibtAnHienMK.setImageResource(R.drawable.eye_24);
                }else{
                    textNewPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ibtAnHienMK.setImageResource(R.drawable.visibility_off_24);
                }
                isNewPassShow=!isNewPassShow;
                textNewPass.setSelection(textNewPass.length());
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

        //Cập nhật mật khẩu
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(textNewPass.getText().toString())|| textNewPass.getText().toString().length() < 6) {
                    Toast.makeText(DoiMatKhau.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    textNewPass.setError("Mật khẩu phải từ 6 ký tự trở lên");
                    textNewPass.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPass.getText().toString())|| textConfirmPass.getText().toString().length() < 6) {
                    Toast.makeText(DoiMatKhau.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    textConfirmPass.setError("Mật khẩu phải từ 6 ký tự trở lên");
                    textConfirmPass.requestFocus();
                } else if (!textNewPass.getText().toString().equals(textConfirmPass.getText().toString())) {
                    Toast.makeText(DoiMatKhau.this, "Mật khẩu không khớp. Vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                    textConfirmPass.setError("Vui lòng nhập lại mật khẩu");
                    textConfirmPass.requestFocus();
                    //xóa password đã nhập
                    textNewPass.clearComposingText();
                    textConfirmPass.clearComposingText();
                }else{
                    user.updatePassword(textConfirmPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(DoiMatKhau.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            startActivity(ttcn);
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}