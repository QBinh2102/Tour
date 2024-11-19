package com.example.tourdulich.trang;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.CSDL.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
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
    private TextView edtTextNgaySinhDangKy;
    private RadioGroup radioGroupGioiTinh;
    private RadioButton radioButtonDangKy;
    private DatePickerDialog picker;


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
        edtTextNgaySinhDangKy = findViewById(R.id.editTextNgaySinhDangKy);
        //Radio button
        radioGroupGioiTinh = findViewById(R.id.radioGroupGT);
        radioGroupGioiTinh.clearCheck();

        //Gạch dưới text ngày sinh
        TextView tv = findViewById(R.id.editTextNgaySinhDangKy);
        tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        edtTextNgaySinhDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(TrangDangKy.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtTextNgaySinhDangKy.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                    }
                },year, month, day);
                picker.show();
            }
        });

        Button btDangKy = findViewById(R.id.btDangKyTuDangKy);
        btDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soCuaGTDuocChon = radioGroupGioiTinh.getCheckedRadioButtonId();
                radioButtonDangKy = findViewById(soCuaGTDuocChon);
                edtTextNgaySinhDangKy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);

                        picker = new DatePickerDialog(TrangDangKy.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtTextNgaySinhDangKy.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                            }
                        },year, month, day);
                        picker.show();
                    }
                });

                String tenDangNhap = edtTextUserNameDangKy.getText().toString();
                String matKhau = edtTextPasswordDangKy.getText().toString();
                String xacNhanMK = edtTextPasswordXacThucDangKy.getText().toString();
                String diaChi = edtTextDiaChiDangKy.getText().toString();
                String soDienThoai = edtTextPhoneDangKy.getText().toString();
                String email = edtTextEmailAddressDangKy.getText().toString();
                String ngaySinh = edtTextNgaySinhDangKy.getText().toString();
                String gioiTinh;

                //Kiem tra so dien thoai co hop li khong
                //So dien thoai chi hop ly khi bat dau voi 3 so ben duoi va 7 so con lai tu 0->9
                String quyDinhSDT = "(039|038|037|036|035|034|033|032|096|097|098|086|083|084|085|081" +
                        "|088|082|070|079|077|076|078|090|093|089|052|056|058|092)[0-9]{7}";
                Matcher mauKiemTraSDT;
                Pattern sdtHopLe = Pattern.compile(quyDinhSDT);
                mauKiemTraSDT = sdtHopLe.matcher(soDienThoai);

                //Kiểm lỗi người dùng
                if(TextUtils.isEmpty(tenDangNhap)){
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextUserNameDangKy.setError("Vui lòng điền tên đăng nhập");
                    edtTextUserNameDangKy.requestFocus();
                } else if (TextUtils.isEmpty(matKhau)|| matKhau.length() < 6) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextPasswordDangKy.setError("Mật khẩu phải từ 6 ký tự trở lên");
                    edtTextPasswordDangKy.requestFocus();
                } else if (TextUtils.isEmpty(xacNhanMK)|| xacNhanMK.length() < 6) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextPasswordXacThucDangKy.setError("Mật khẩu phải từ 6 ký tự trở lên");
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
                } else if (!mauKiemTraSDT.find()) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng xem lại số điện thoại",
                            Toast.LENGTH_LONG).show();
                    edtTextPhoneDangKy.setError("Số điện thoại không hợp lệ");
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
                }else if (TextUtils.isEmpty(ngaySinh)) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTextNgaySinhDangKy.setError("Vui lòng nhập ngày sinh");
                    edtTextNgaySinhDangKy.requestFocus();
                }else if (radioGroupGioiTinh.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(TrangDangKy.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    radioButtonDangKy.setError("Vui lòng chọn giới tính");
                    radioButtonDangKy.requestFocus();
                }else{
                    gioiTinh = radioButtonDangKy.getText().toString();
                    thanhTienTrinh.setVisibility(View.VISIBLE);
                    nguoiDangKy(tenDangNhap, matKhau, xacNhanMK, diaChi,soDienThoai, email, ngaySinh, gioiTinh);
                }
            }

            //FirebaseAuth() là một lớp trong Firebase Authentication, được sử dụng để quản lý xác thực người dùng trong ứng dụng
            private void nguoiDangKy(String tenDangNhap, String matKhau, String xacNhanMK, String diaChi, String soDienThoai, String email, String ngaySinh, String gioiTinh) {
                FirebaseAuth xacThucFirebase = FirebaseAuth.getInstance();
                xacThucFirebase.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(
                        TrangDangKy.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        thanhTienTrinh.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            FirebaseUser nguoiDungFB = xacThucFirebase.getCurrentUser();
                            //Thay doi thong tin ho so nguoi dung
                            UserProfileChangeRequest thayDoiTTUser = new UserProfileChangeRequest.Builder().setDisplayName(tenDangNhap).build();
                            nguoiDungFB.updateProfile(thayDoiTTUser);

                            //Quan ly du lieu nguoi dung
                            LuuThongTinUser thongTinUser = new LuuThongTinUser(diaChi, soDienThoai, email, ngaySinh, gioiTinh);
                            DatabaseReference refDuLieu = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
                            refDuLieu.child(nguoiDungFB.getUid()).setValue(thongTinUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        nguoiDungFB.sendEmailVerification();
                                        Toast.makeText(TrangDangKy.this, "Đăng ký thành công! Vui lòng xác thực email", Toast.LENGTH_LONG).show();
                                        //Mo trang dang nhap sau khi nguoi dung dang ky thanh cong
                                        Intent intent = new Intent(TrangDangKy.this, MainActivity.class );
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(TrangDangKy.this, "Đăng ký thất bại. Vui lòng thử lại", Toast.LENGTH_LONG).show();
                                        thanhTienTrinh.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                edtTextEmailAddressDangKy.setError("Email không hợp lệ vui lòng thử lại");
                                edtTextEmailAddressDangKy.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                edtTextEmailAddressDangKy.setError("Email này đã được sử dụng vui lòng thử lại");
                                edtTextEmailAddressDangKy.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText( TrangDangKy.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            thanhTienTrinh.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        //Quay lại THÔNG TIN CHƯA ĐĂNG NHẬP
        Intent dkttcn = new Intent(this, ThongTinChuaDangNhap.class);
        btnQuayLai = findViewById(R.id.btQuayLaiTuDangKy);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(dkttcn);
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