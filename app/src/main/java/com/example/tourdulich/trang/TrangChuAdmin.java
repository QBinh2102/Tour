package com.example.tourdulich.trang;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrangChuAdmin extends AppCompatActivity {
    private TextView lbWelcome;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;
    private Button btnCong, btnSua, btnXoa;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String hoTen;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Tour");
    private ImageView imgVPopupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu_admin);
        if(firebaseUser != null) {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu đã đăng nhập thành công
            Intent ttcn = new Intent(this, ThongTinCaNhan.class);
            btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
            btnToiHoSo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ttcn);
                    overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
                }
            });
        }else {
            //Chuyển sang TRANG THÔNG TIN CÁ NHÂN nếu chưa đăng nhập
            Intent ttcncdn = new Intent(this, ThongTinChuaDangNhap.class);
            btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
            btnToiHoSo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ttcncdn);
                    overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
                }
            });
        }

        //Xử lý khi bấm vào thanh menu
        imgVPopupMenu = findViewById(R.id.imgViewPopupMenu);
        // Bắt sự kiện khi người dùng bấm vào icon ba chấm
        imgVPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một dialog với layout dialog_menu.xml
                Dialog dialog = new Dialog(TrangChuAdmin.this);
                dialog.setContentView(R.layout.dialog_menu_danh_muc);  // Đặt layout của dialog
                dialog.setCancelable(true);  // Cho phép đóng dialog khi bấm ra ngoài

                // Đặt sự kiện cho các nút trong dialog
                Button btnCong = dialog.findViewById(R.id.menu_add);
                Button btnSua = dialog.findViewById(R.id.menu_edit);
                Button btnXoa = dialog.findViewById(R.id.menu_delete);

                // Thiết lập sự kiện cho các nút
                btnCong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý thêm
                    //    themDanhMuc();
                        dialog.dismiss();  // Đóng dialog sau khi thao tác
                    }
                });
                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý sửa
                    //    suaDanhMuc();
                        dialog.dismiss();
                    }
                });
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý xóa
                    //    xoaDanhMuc();
                        dialog.dismiss();
                    }
                });
                // Hiển thị dialog
                dialog.show();
            }
        });

        //Chuyển sang trang ĐẶT VÉ
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btTrangChuToiDatVe);
        btnToiDatVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(datVe);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Chuyển sang trang TIN TỨC
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btTrangChuToiTinTuc);
        btnToiTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(tinTuc);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Chuyển sang trang GIAO DỊCH
        Intent gd = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btTrangChuToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(gd);
                overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
            }
        });

        //Hiển thị chào mừng người dùng trên trang chủ
        lbWelcome = findViewById(R.id.textViewWelcome);
        if(firebaseUser!=null){
            hoTen = firebaseUser.getDisplayName();
            lbWelcome.setText("Chào mừng "+hoTen);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
}