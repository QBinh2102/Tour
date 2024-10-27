package com.example.tourdulich;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText edtTextEmailDangNhap;
    private EditText edtTextPasswordDangNhap;
    private ProgressBar thanhTienTrinh;
    private FirebaseAuth xacThucFirebase;
    private Button btLogin;
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
                if (!isPassShow) {
                    //Show password
                    editTextPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    btnAnHienMK.setImageResource(R.drawable.eye_24);
                } else {
                    //Hide password
                    editTextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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


        //Nguoi dung dang nhap

        edtTextEmailDangNhap = findViewById(R.id.editTextEmailDangNhap);
        edtTextPasswordDangNhap = findViewById(R.id.editTextPasswordDangNhap);
        xacThucFirebase = FirebaseAuth.getInstance();
        thanhTienTrinh = findViewById(R.id.thanhTienTrinh);
        
        btLogin = findViewById(R.id.btDangNhapTuDangNhap);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailLogin = edtTextEmailDangNhap.getText().toString();
                String matKhauLogin = edtTextPasswordDangNhap.getText().toString();
                if (TextUtils.isEmpty(emailLogin)) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                    edtTextEmailDangNhap.setError("Vui lòng điền tên đăng nhập");
                    edtTextEmailDangNhap.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailLogin).matches()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập lại email",
                            Toast.LENGTH_LONG).show();
                    edtTextEmailDangNhap.setError("Vui lòng điền email hợp lệ");
                    edtTextEmailDangNhap.requestFocus();
                }
                if (TextUtils.isEmpty(matKhauLogin)) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                    edtTextPasswordDangNhap.setError("Vui lòng điền mật khẩu");
                    edtTextPasswordDangNhap.requestFocus();
                } else {
                    thanhTienTrinh.setVisibility(View.VISIBLE);
                    userDangNhap(emailLogin, matKhauLogin);
                }
            }
        });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


        }
    private void userDangNhap(String email, String matKhau) {
        xacThucFirebase.signInWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Có lỗi xảy ra. Vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                }
                thanhTienTrinh.setVisibility(View.GONE);
            }
        });
    }


}