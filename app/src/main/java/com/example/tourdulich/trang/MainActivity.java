package com.example.tourdulich.trang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText edtTextEmailDangNhap;
    private EditText edtTextPasswordDangNhap;

    private ProgressBar thanhTienTrinh;
    private FirebaseAuth xacThucFirebase;

    private Button btLogin;
    private ImageButton btnAnHienMK;
    private EditText editTextPass;
    private boolean isPassShow = false;
    private Button btnQuayLai;
    private TextView textQuenPass;
    private static final String TAG = "MainActivity";
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

        //Quay lại THÔNG TIN CHƯA ĐĂNG NHẬP
        Intent ttcdn = new Intent(this, ThongTinChuaDangNhap.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuDangNhap);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ttcdn);
            }
        });

        //Chuyển sang TRANG QUÊN MẬT KHẨU
        Intent trangQuenPass = new Intent(this, TrangQuenMK.class);
        textQuenPass = findViewById(R.id.textQuenPass);
        textQuenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    //lấy thông tin của người dùng hiện tại đang đăng nhập vào ứng dụng
                    FirebaseUser firebaseUser = xacThucFirebase.getCurrentUser();

                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công",
                                Toast.LENGTH_SHORT).show();
                        String userId = firebaseUser.getUid();
                        kiemTraVaiTro(userId);
                        finish();
                    }else{
                        firebaseUser.sendEmailVerification();
                        xacThucFirebase.signOut();
                        showAlertDialog();
                    }

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(MainActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(MainActivity.this, "Thông tin đăng nhập không chính xác. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                thanhTienTrinh.setVisibility(View.GONE);
            }
        });
    }

    private void kiemTraVaiTro(String userId) {
        // Kiểm tra nếu userId không hợp lệ
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(MainActivity.this, "User ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Truy xuất vai trò từ Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Người đã đăng ký").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy giá trị role từ Firebase
                String role = snapshot.child("role").getValue(String.class);
                Log.d(TAG, "Role: " + role);
                // Kiểm tra xem role có tồn tại không
                if (role != null) {
                    // Kiểm tra vai trò và chuyển hướng tương ứng
                    if ("admin".equals(role)) {
                        // Nếu role là admin, chuyển sang giao diện Admin
                        startActivity(new Intent(MainActivity.this, TrangChuAdmin.class));
                    } else {
                        // Nếu role là user, chuyển sang giao diện User
                        startActivity(new Intent(MainActivity.this, TrangChu.class));
                    }
                    finish();
                } else {
                    // Nếu role không tồn tại, hiển thị thông báo
                    Toast.makeText(MainActivity.this, "Role không tồn tại", Toast.LENGTH_SHORT).show();
                }
                // Đóng MainActivity sau khi xử lý
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi trong quá trình truy xuất dữ liệu
                Toast.makeText(MainActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác thực email");
        builder.setMessage("Bạn chưa xác thực email");

        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Mo email o cua so khac
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
//Kiem tra neu user da log in roi -> vao thang profile
    @Override
    protected void onStart() {
        super.onStart();
        if(xacThucFirebase.getCurrentUser() != null){
            //Gán giá trị cho người dùng mặc định là user

            Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, ThongTinCaNhan.class));
            finish();
        }else{
            Toast.makeText(MainActivity.this, "Bạn có thể đăng nhập được rồi", Toast.LENGTH_SHORT).show();
        }
    }
}