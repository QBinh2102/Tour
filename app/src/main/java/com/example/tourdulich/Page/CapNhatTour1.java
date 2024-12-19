package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CapNhatTour1 extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnXoa;
    private Button btnCapNhat;
    private EditText edtTen;
    private EditText edtGiaTien;
    private EditText edtSoVe;
    private EditText edtGioiThieu;
    private Spinner spinnerDanhMuc;
    private Spinner spinnerPhuongTien;
    private Spinner spinnerActive;
    private TextView txtNKH;
    private TextView txtNKT;

    private List<String> tenDanhMuc = new ArrayList<>();
    private DatePickerDialog picker;
    private String loaiDanhMuc;
    private String loaiPhuongTien;
    private String ngayKhoiHanh;
    private String ngayKetThuc;
    private long giaTien =0;
    private DecimalFormat df;
    private boolean flag;
    private boolean active;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");
    private DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference("Danh mục");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cap_nhat_tour1);

        btnCapNhat = findViewById(R.id.btnUploadTour);
        btnXoa = findViewById(R.id.btnDeleteTour);
        btnQuayLai = findViewById(R.id.btnQuayLaiQLTourTuCapNhatTour);
        edtTen = findViewById(R.id.edtCapNhatTenTour);
        edtGiaTien = findViewById(R.id.edtCapNhatGiaTienTour);
        edtSoVe = findViewById(R.id.edtCapNhatSoLuongVe);
        edtGioiThieu = findViewById(R.id.edtCapNhatGioiThieuTour);
        spinnerDanhMuc = findViewById(R.id.spinnerCapNhatDanhMuc);
        spinnerPhuongTien = findViewById(R.id.spinnerCapNhatPhuongTien);
        spinnerActive = findViewById(R.id.spinnerActiveTour);
        txtNKH = findViewById(R.id.txtCapNhatNKHTour);
        txtNKT = findViewById(R.id.txtCapNhatNKTTour);

        txtNKH.setPaintFlags(txtNKH.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txtNKT.setPaintFlags(txtNKT.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");

        formatGiaTien();
        showThongTin();

        spinnerDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loaiDanhMuc = tenDanhMuc.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPhuongTien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    loaiPhuongTien = "Xe";
                } else if (position==1) {
                    loaiPhuongTien = "Máy bay";
                } else if (position==2) {
                    loaiPhuongTien = "Thuyền";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerActive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    active = true;
                } else if (position==1) {
                    active = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtNKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(CapNhatTour1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngayKhoiHanh = dayOfMonth + "/" + (month+1) +  "/" + year;
                        txtNKH.setText(ngayKhoiHanh);
                    }
                },year, month, day);
                picker.show();
            }
        });

        txtNKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(CapNhatTour1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngayKetThuc = dayOfMonth + "/" + (month+1) +  "/" + year;
                        txtNKT.setText(ngayKetThuc);
                    }
                },year, month, day);
                picker.show();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(CapNhatTour1.this);
                pd.setTitle("Đang cập nhật...");
                pd.show();
                String tenTour = edtTen.getText().toString();
                String danhMuc = loaiDanhMuc;
                String ngayKhoiHanh = txtNKH.getText().toString();
                String ngayKetThuc = txtNKT.getText().toString();
                String phuongTien = loaiPhuongTien;
                String tien = String.valueOf(giaTien);
                String soVe = edtSoVe.getText().toString();
                String gioiThieu = edtGioiThieu.getText().toString();

                if(ngayKhoiHanh!=""&&ngayKetThuc!="") {
                    kiemTraNgayThang();
                }

                if (TextUtils.isEmpty(tenTour)) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Chưa đặt tên cho tour", Toast.LENGTH_SHORT).show();
                    edtTen.setError("Vui lòng đặt tên tour");
                    edtTen.requestFocus();
                } else if (TextUtils.isEmpty(ngayKhoiHanh)) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Chưa chọn ngày khởi hành", Toast.LENGTH_SHORT).show();
                    txtNKH.setError("Vui lòng chọn ngày khởi hành");
                    txtNKH.requestFocus();
                } else if (TextUtils.isEmpty(ngayKetThuc)) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Chưa chọn ngày kết thúc", Toast.LENGTH_SHORT).show();
                    txtNKT.setError("Vui lòng chọn ngày kết thúc");
                    txtNKT.requestFocus();
                } else if (giaTien==0) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Chưa nhập giá tiền", Toast.LENGTH_SHORT).show();
                    edtGiaTien.setError("Vui lòng nhập giá tiền");
                    edtGiaTien.requestFocus();
                } else if (TextUtils.isEmpty(soVe)) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Chưa nhập số vé", Toast.LENGTH_SHORT).show();
                    edtSoVe.setError("Vui lòng nhập số vé");
                    edtSoVe.requestFocus();
                } else if (TextUtils.isEmpty(gioiThieu)) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Chưa điền giới thiệu", Toast.LENGTH_SHORT).show();
                    edtGioiThieu.setError("Vui lòng điền giới thiệu");
                    edtGioiThieu.requestFocus();
                } else if (!flag) {
                    pd.dismiss();
                    Toast.makeText(CapNhatTour1.this, "Lỗi dữ liệu ngày kết thúc", Toast.LENGTH_SHORT).show();
                    txtNKT.setError("Vui lòng chọn lại ngày kết thúc");
                    txtNKT.requestFocus();
                } else{
                    tourRef.child(tour.idTour).child("tenTour").setValue(tenTour);
                    tourRef.child(tour.idTour).child("name").setValue(tenTour);
                    tourRef.child(tour.idTour).child("danhMuc").setValue(danhMuc);
                    tourRef.child(tour.idTour).child("giaTien").setValue(tien);
                    tourRef.child(tour.idTour).child("gioiThieu").setValue(gioiThieu);
                    tourRef.child(tour.idTour).child("ngayKetThuc").setValue(ngayKetThuc);
                    tourRef.child(tour.idTour).child("ngayKhoiHanh").setValue(ngayKhoiHanh);
                    tourRef.child(tour.idTour).child("phuongTien").setValue(phuongTien);
                    tourRef.child(tour.idTour).child("soLuongVe").setValue(Integer.parseInt(soVe));
                    tourRef.child(tour.idTour).child("active").setValue(active);
                    Toast.makeText(CapNhatTour1.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    Intent ql = new Intent(CapNhatTour1.this,QuanLyTour.class);
                    startActivity(ql);
                }
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(CapNhatTour1.this);
                build.setTitle("Thông báo!!!");
                build.setMessage("Bạn có chắc muốn xóa danh mục này?");
//                AlertDialog dialog = builder.create();
                build.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        Toast.makeText(CapNhatTour1.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
                        tourRef.child(tour.idTour).removeValue();
                        Intent ql = new Intent(CapNhatTour1.this,QuanLyTour.class);
                        startActivity(ql);
                    }
                });
                build.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                    }
                });
                build.show();
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(CapNhatTour1.this, QuanLyTour.class);
                startActivity(ql);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showThongTin(){
        Intent intent = getIntent();
        Tour tour = (Tour) intent.getSerializableExtra("tour");
        tourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Tour currentTour = dataSnapshot.getValue(Tour.class);
                    if(tour.idTour.equals(currentTour.idTour)) {
                        edtTen.setText(tour.tenTour);
                        txtNKH.setText(tour.ngayKhoiHanh);
                        ngayKhoiHanh = tour.ngayKhoiHanh;
                        txtNKT.setText(tour.ngayKetThuc);
                        ngayKetThuc = tour.ngayKetThuc;
                        edtGiaTien.setText(tour.giaTien);
                        edtSoVe.setText(String.valueOf(tour.soLuongVe));
                        edtGioiThieu.setText(tour.gioiThieu);
                        danhMucRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);
                                    if(danhMuc!=null) {
                                        tenDanhMuc.add(danhMuc.ten);
                                    }
                                }
                                ArrayAdapter<String> danhMucAdapter = new ArrayAdapter<String>(CapNhatTour1.this,
                                        android.R.layout.simple_spinner_item, tenDanhMuc);
                                danhMucAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerDanhMuc.setAdapter(danhMucAdapter);
                                for(int i = 0;i<tenDanhMuc.size();i++){
                                    if(tour.danhMuc.equals(tenDanhMuc.get(i).toString()))
                                        spinnerDanhMuc.setSelection(i);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //Spinner phương tiện
                        ArrayAdapter<CharSequence> phuongTienAdapter = ArrayAdapter.createFromResource(CapNhatTour1.this,
                                R.array.phuongTien, android.R.layout.simple_spinner_item);
                        phuongTienAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerPhuongTien.setAdapter(phuongTienAdapter);
                        if(tour.phuongTien.equals("Xe")){
                            spinnerPhuongTien.setSelection(0);
                        }else if (tour.phuongTien.equals("Máy bay")) {
                            spinnerPhuongTien.setSelection(1);
                        } else if (tour.phuongTien.equals("Thuyền")) {
                            spinnerPhuongTien.setSelection(2);
                        }

                        //Spinner active
                        ArrayAdapter<CharSequence> activeAdapter = ArrayAdapter.createFromResource(CapNhatTour1.this,
                                R.array.active, android.R.layout.simple_spinner_item);
                        activeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerActive.setAdapter(activeAdapter);
                        if(tour.active){
                            spinnerActive.setSelection(0);
                        }else {
                            spinnerActive.setSelection(1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void formatGiaTien(){
        Locale localeVN = new Locale("vi", "VN");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(localeVN);
        dfs.setCurrencySymbol("₫");
        dfs.setGroupingSeparator('.');
        df = new DecimalFormat("#,###", dfs);

        edtGiaTien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtGiaTien.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    if (originalString.isEmpty()) {
                        giaTien = 0; // Cập nhật biến khi EditText rỗng
                        edtGiaTien.setText("");
                        edtGiaTien.setSelection(0);
                        edtGiaTien.addTextChangedListener(this);
                        return;
                    }

                    String cleanString = originalString.replaceAll("[.,₫]", ""); // Loại bỏ dấu . và ₫
                    giaTien = Long.parseLong(cleanString); // Lưu giá trị chưa định dạng
                    String formatted = df.format(giaTien);
                    edtGiaTien.setText(formatted);
                    edtGiaTien.setSelection(formatted.length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    giaTien = 0; // Xử lý khi nhập không phải số
                    edtGiaTien.setText("");
                }

                edtGiaTien.addTextChangedListener(this);
            }
        });
    }

    //Kiểm tra ngày khởi hành-kết thúc
    private void kiemTraNgayThang() {
        try {
            Date dateKhoiHanh = dateFormat.parse(ngayKhoiHanh);
            Date dateKetThuc = dateFormat.parse(ngayKetThuc);

            if (dateKetThuc.before(dateKhoiHanh)) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(CapNhatTour1.this, "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}