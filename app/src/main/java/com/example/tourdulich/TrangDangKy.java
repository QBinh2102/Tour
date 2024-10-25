package com.example.tourdulich;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class TrangDangKy extends AppCompatActivity {

    private Button btnQuayLai;
    private ImageButton iBtAnHienMK;
    private ImageButton iBtAnHienXacThucMK;
    private EditText textPass;
    private EditText textConfirmPass;
    private boolean isPassShow = false;
    private boolean isConfirmPassShow = false;
    private ProgressBar thanhTienTrinh;
    private EditText edtTextUserNameDangKy;
    private EditText edtTextPasswordDangKy;
    private EditText edtTextPasswordXacThucDangKy;
    private EditText edtTextDiaChiDangKy;
    private EditText edtTextPhoneDangKy;
    private EditText edtTextEmailAddressDangKy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_dang_ky);

        //Lấy giá trị người dùng nhập vào form đăng
        thanhTienTrinh = findViewById(R.id.thanhTienTrinh);
        edtTextUserNameDangKy = findViewById(R.id.editTextUserNameDangKy);
        edtTextPasswordDangKy = findViewById(R.id.editTextPasswordDangKy);
        edtTextPasswordXacThucDangKy = findViewById(R.id.editTextPasswordXacThucDangKy);
        edtTextDiaChiDangKy = findViewById(R.id.editTextDiaChiDangKy);
        edtTextPhoneDangKy = findViewById(R.id.editTextPhoneDangKy);
        edtTextEmailAddressDangKy = findViewById(R.id.editTextEmailAddressDangKy);

        Button btDangKy = findViewById(R.id.btDangKyTuDangKy);
        btDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tenDangNhap = edtTextUserNameDangKy.getText().toString();
                String matKhau = edtTextPasswordDangKy.getText().toString();
                String xacNhanMK = edtTextPasswordXacThucDangKy.getText().toString();
                String diaChi = edtTextDiaChiDangKy.getText().toString();
                String soDienThoai = edtTextPhoneDangKy.getText().toString();
                String email = edtTextEmailAddressDangKy.getText().toString();

                //Kiểm lỗi người dùng
                if(TextUtils.isEmpty(tenDangNhap)){
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextUserNameDangKy.setError("Vui lòng điền tên đăng nhập");
                    edtTextUserNameDangKy.requestFocus();
                } else if (TextUtils.isEmpty(matKhau)) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextPasswordDangKy.setError("Vui lòng điền mật khẩu");
                    edtTextPasswordDangKy.requestFocus();
                } else if (TextUtils.isEmpty(xacNhanMK)) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextPasswordXacThucDangKy.setError("Vui lòng xác nhận mật khẩu");
                    edtTextPasswordXacThucDangKy.requestFocus();
                } else if (!matKhau.equals(xacNhanMK)) {
                    Toast.makeText(TrangDangKy.this, "Mật khẩu không khớp. Vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                    edtTextPasswordXacThucDangKy.setError("Vui lòng xem lại mật khẩu");
                    edtTextPasswordXacThucDangKy.requestFocus();
                    //xóa password đã nhập
                    edtTextPasswordDangKy.clearComposingText();
                    edtTextPasswordXacThucDangKy.clearComposingText();
                } else if (TextUtils.isEmpty(diaChi)) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextDiaChiDangKy.setError("Vui lòng điền địa chỉ");
                    edtTextDiaChiDangKy.requestFocus();
                } else if (TextUtils.isEmpty(soDienThoai)) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextPhoneDangKy.setError("Vui lòng điền số điện thoại");
                    edtTextPhoneDangKy.requestFocus();
                } else if (soDienThoai.length() != 10) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng xem lại số điện thoại",
                            Toast.LENGTH_LONG).show();
                    edtTextPhoneDangKy.setError("Số điện thoại bao gồm 10 số");
                    edtTextPhoneDangKy.requestFocus();
                }else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextEmailAddressDangKy.setError("Vui lòng điền email");
                    edtTextEmailAddressDangKy.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng nhập lại email",
                            Toast.LENGTH_LONG).show();
                    edtTextEmailAddressDangKy.setError("Vui lòng điền email hợp lệ");
                    edtTextEmailAddressDangKy.requestFocus();
                }else{
                    thanhTienTrinh.setVisibility(View.VISIBLE);
                    Toast.makeText(TrangDangKy.this, "Đăng ký thành công",
                            Toast.LENGTH_LONG).show();
                    nguoiDangKy(tenDangNhap, matKhau, xacNhanMK, diaChi,soDienThoai, email);
                }
            }

            private void nguoiDangKy(String tenDangNhap, String matKhau, String xacNhanMK, String diaChi, String soDienThoai, String email) {

            }
        });
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