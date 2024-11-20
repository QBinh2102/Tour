package com.example.tourdulich.trang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.example.tourdulich.CSDL.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongTinCaNhan extends AppCompatActivity {

    private LinearLayout btnToiTrangChu;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;

    private Button btnChinhSua;
    private Button btnThayDoiMK;
    private Button btnDangXuat;

    private TextView txtEmail;
    private TextView txtNgaySinh;
    private TextView txtGioiTinh;
    private TextView txtTenHoSo;
    private TextView txtDienThoai;
    private TextView txtDiaChi;
    private ProgressBar thanhTienTrinh;
    private String email, dienThoai, diaChi, ngaySinh, gioiTinh, tenHoSo;
    private ImageView imageView;
    private FirebaseAuth xacThucFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_ca_nhan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Chuyển sang TRANG CHỦ
        Intent trangChu = new Intent(this, TrangChu.class);
        btnToiTrangChu = findViewById(R.id.btHoSoToiTrangChu);
        btnToiTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(trangChu);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang TIN TỨC
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btHoSoToiTinTuc);
        btnToiTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tinTuc);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang ĐẶT VÉ
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btHoSoToiDatVe);
        btnToiDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(datVe);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang trang GIAO DỊCH
        Intent giaoDich = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btHoSoToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(giaoDich);
                overridePendingTransition(R.anim.slide_1_phai_qua_trai, R.anim.slide_2_phai_qua_trai);
            }
        });

        //Chuyển sang CHỈNH SỬA THÔNG TIN CÁ NHÂN
        Intent chinhSua = new Intent(this, ChinhSuaThongTinCaNhan.class);
        btnChinhSua = findViewById(R.id.btChinhSuaHoSo);
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(chinhSua);
            }
        });

        //Chuyển sang THAY ĐỔI MẬT KHẨU
        Intent thayMK = new Intent(this, DoiMatKhau.class);
        btnThayDoiMK = findViewById(R.id.btThayDoiMK);
        btnThayDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(thayMK);
            }
        });

        //Đăng xuất tài khoản
        btnDangXuat = findViewById(R.id.btDangXuat);
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(trangChu);
            }
        });

        txtTenHoSo = findViewById(R.id.txtTenHoSo);
        txtEmail = findViewById(R.id.txtEmailHoSo);
        txtDienThoai = findViewById(R.id.txtDienThoaiHoSo);
        txtDiaChi = findViewById(R.id.txtDiaChiHoSo);
        txtNgaySinh = findViewById(R.id.txtNgaySinhHoSo);
        txtGioiTinh = findViewById(R.id.txtGioiTinhHoSo);
        xacThucFirebase = FirebaseAuth.getInstance();
        thanhTienTrinh = findViewById(R.id.thanhTienTrinh);

        FirebaseUser firebaseUser = xacThucFirebase.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(ThongTinCaNhan.this, "Thông tin người dùng không tồn tại",
                    Toast.LENGTH_SHORT).show();
        }else {
            kiemTraXacNhanEmail(firebaseUser);
            thanhTienTrinh.setVisibility(View.VISIBLE);
            HienThiThongTinUser(firebaseUser);
        }
    }

    private void kiemTraXacNhanEmail(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            firebaseUser.reload()// Reload lại thông tin user để cập nhật trạng thái mới
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (!firebaseUser.isEmailVerified()) {
                                    showAlertDialog();
                                }
                            } else {
                                // Nếu reload không thành công
                                Toast.makeText(ThongTinCaNhan.this,
                                            "Lỗi khi tải lại thông tin người dùng",
                                                  Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void showAlertDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinCaNhan.this);
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

    private void HienThiThongTinUser(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        Log.d("TAG", "User ID: " + userID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LuuThongTinUser thongTinUser = snapshot.getValue(LuuThongTinUser.class);
                if(thongTinUser != null){
                    //Gán giá trị cho người mới đăng ký mặc định là user
                    if(thongTinUser.role == null)
                    {
                        thongTinUser.role = "user";
                        databaseReference.child(userID).setValue(thongTinUser);
                    }
                    tenHoSo = firebaseUser.getDisplayName();
                    email = thongTinUser.email;
                    diaChi = thongTinUser.diaChi;
                    dienThoai = thongTinUser.soDienThoai;
                    ngaySinh = thongTinUser.ngaySinh;
                    gioiTinh = thongTinUser.gioiTinh;
                    String role = thongTinUser.role;

                    txtTenHoSo.setText(tenHoSo);
                    txtEmail.setText(email);
                    txtDiaChi.setText(diaChi);
                    txtDienThoai.setText(dienThoai);
                    txtNgaySinh.setText(ngaySinh);
                    txtGioiTinh.setText(gioiTinh);
                }else{
                    Log.d("TAG", "thongTinUser is null");
                }
                thanhTienTrinh.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongTinCaNhan.this, "Có lỗi xảy ra",
                        Toast.LENGTH_SHORT).show();
                thanhTienTrinh.setVisibility(View.GONE);
            }
        });
    }
}