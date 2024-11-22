package com.example.tourdulich.trang;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TrangChuAdmin extends AppCompatActivity {
    private TextView lbWelcome;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;
    private Button btnDangXuat;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String hoTen;
    private ImageView imgVPopupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_admin);

        // Kiểm tra trạng thái đăng nhập khi mở trang
        if (firebaseUser == null) {
            Intent intent = new Intent(TrangChuAdmin.this, ThongTinChuaDangNhap.class);
            startActivity(intent);
            finish();
        }
        //Kiểm tra vai trò của user -> chuyển đúng giao diện của từng vai trò
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String role = preferences.getString("role", "user"); // Default là "user" nếu không tìm thấy role

        if (role.equals("admin")) {
            // Mở trang chủ Admin
            setContentView(R.layout.activity_trang_chu_admin);
        } else {
            // Mở trang chủ User
            setContentView(R.layout.activity_trang_chu);
        }

        // Hiển thị thông tin người dùng
        lbWelcome = findViewById(R.id.textViewWelcome);
        hoTen = firebaseUser.getDisplayName();
        lbWelcome.setText("Chào mừng " + hoTen);

        // Chuyển sang trang thông tin cá nhân
        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
        btnToiHoSo.setOnClickListener(v -> {
            startActivity(ttcn);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });

        // Chuyển sang trang Đặt Vé
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btTrangChuToiDatVe);
        btnToiDatVe.setOnClickListener(view -> {
            startActivity(datVe);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });

        // Chuyển sang trang Tin Tức
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btTrangChuToiTinTuc);
        btnToiTinTuc.setOnClickListener(view -> {
            startActivity(tinTuc);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });

        // Chuyển sang trang Giao Dịch
        Intent gd = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btTrangChuToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(view -> {
            startActivity(gd);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });


        // Xử lý khi bấm vào thanh menu
        imgVPopupMenu = findViewById(R.id.imgViewPopupMenu);
        imgVPopupMenu.setOnClickListener(view -> {
            // Tạo một dialog với layout dialog_menu.xml
            Dialog dialog = new Dialog(TrangChuAdmin.this);
            dialog.setContentView(R.layout.dialog_menu_danh_muc);
            dialog.setCancelable(true);  // Cho phép đóng dialog khi bấm ra ngoài

            // Đặt sự kiện cho các nút trong dialog
            Button btnCong = dialog.findViewById(R.id.menu_add);
            Button btnSua = dialog.findViewById(R.id.menu_edit);
            Button btnXoa = dialog.findViewById(R.id.menu_delete);

            // Thiết lập sự kiện cho các nút
            btnCong.setOnClickListener(v -> {
                // Xử lý thêm tour
                dialog.dismiss();
            });
            btnSua.setOnClickListener(v -> {
                // Xử lý sửa tour
                dialog.dismiss();
            });
            btnXoa.setOnClickListener(v -> {
                // Xử lý xóa tour
                dialog.dismiss();
            });

            // Hiển thị dialog
            dialog.show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra lại nếu người dùng chưa đăng nhập, điều hướng về trang đăng nhập
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(TrangChuAdmin.this, ThongTinChuaDangNhap.class);
            startActivity(intent);
            finish();
        }
    }
}
