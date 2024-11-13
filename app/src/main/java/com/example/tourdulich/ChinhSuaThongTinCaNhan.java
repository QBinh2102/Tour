package com.example.tourdulich;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ChinhSuaThongTinCaNhan extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnCapNhat;
    private RadioButton RbtNam;
    private RadioButton RbtNu;
    private boolean gt = true;

    private FirebaseAuth xacThucFirebase;
    private FirebaseUser firebaseUser;

    private String tenHoSo, SDT, diaChi, ngaySinh, gioiTinh;
    private EditText edtTen;
    private EditText edtSDT;
    private EditText edtDiaChi;
    private TextView txtDate;

    private DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chinh_sua_thong_tin_ca_nhan);

        btnQuayLai = findViewById(R.id.btQuayLaiTuChinhSuaTTCN);
        btnCapNhat = findViewById(R.id.btCapNhatChinhSua);
        edtTen = findViewById(R.id.editTextTenChinhSua);
        edtSDT = findViewById(R.id.editTextPhoneChinhSua);
        edtDiaChi = findViewById(R.id.editTextDiaChiChinhSua);
        txtDate = findViewById(R.id.textDateChinhSua);
        RbtNam = findViewById(R.id.radioNamChinhSua);
        RbtNu = findViewById(R.id.radioNuChinhSua);
        xacThucFirebase = FirebaseAuth.getInstance();
        firebaseUser = xacThucFirebase.getCurrentUser();

        //Gạch dưới text ngày sinh
        TextView tv = findViewById(R.id.textDateChinhSua);
        tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        //Hiện thông tin người dùng
        HienThiThongTin(firebaseUser);

        //Quay lại THÔNG TIN HỒ SƠ
        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ttcn);
            }
        });

        //Chọn giới tính
        RbtNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gt=true;
                RbtNu.setChecked(false);
                RbtNam.setChecked(true);
            }
        });
        RbtNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gt=false;
                RbtNam.setChecked(false);
                RbtNu.setChecked(true);
            }
        });

        //Chọn ngày sinh
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(ChinhSuaThongTinCaNhan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDate.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                    }
                },year, month, day);
                picker.show();
            }
        });


        //Cập nhật hồ sơ người dùng
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void HienThiThongTin(FirebaseUser firebaseUser){
        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LuuThongTinUser thongTinUser = snapshot.getValue(LuuThongTinUser.class);
                tenHoSo = firebaseUser.getDisplayName();
                diaChi = thongTinUser.diaChi;
                SDT = thongTinUser.soDienThoai;
                ngaySinh = thongTinUser.ngaySinh;
                gioiTinh = thongTinUser.gioiTinh;

                edtTen.setText(tenHoSo);
                edtDiaChi.setText(diaChi);
                edtSDT.setText(SDT);
                txtDate.setText(ngaySinh);
                if (gioiTinh.equals("Nam")) {
                    gt = true;
                    RbtNu.setChecked(false);
                    RbtNam.setChecked(true);
                }else {
                    gt = false;
                    RbtNam.setChecked(false);
                    RbtNu.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChinhSuaThongTinCaNhan.this, "Có lỗi xảy ra",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}