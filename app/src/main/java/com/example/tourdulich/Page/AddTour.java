package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.Database.Tour;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class AddTour extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnTao;
    private TextView link1;
    private TextView link2;
    private TextView link3;
    private TextView txtNKH;
    private TextView txtNKT;
    private EditText edtTen;
    private EditText edtGia;
    private EditText edtSoVe;
    private EditText edtGioiThieu;
    private Spinner spinnerDanhMuc;
    private Spinner spinnerPhuongTien;

    private TextView currentLink;
    private DatePickerDialog picker;
    private DecimalFormat df;
    private long giaTien =0;
    private List<String> tenDanhMuc = new ArrayList<>();
    private String loaiDanhMuc;
    private String loaiPhuongTien;
    private String ngayKhoiHanh;
    private String ngayKetThuc;
    private boolean flag;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference("Danh mục");
    private DatabaseReference tourRef = FirebaseDatabase.getInstance().getReference("Tour");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_tour);

        btnQuayLai = findViewById(R.id.btnQuayLaiQLTour);
        btnTao = findViewById(R.id.btnTaoTour);
        link1 = findViewById(R.id.linkHinh1);
        link2 = findViewById(R.id.linkHinh2);
        link3 = findViewById(R.id.linkHinh3);
        txtNKH = findViewById(R.id.txtNKHTour);
        txtNKT = findViewById(R.id.txtNKTTour);
        edtTen = findViewById(R.id.edtTenTour);
        edtGia = findViewById(R.id.edtGiaTienTour);
        edtSoVe = findViewById(R.id.edtSoLuongVe);
        edtGioiThieu = findViewById(R.id.edtGioiThieuTour);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        spinnerPhuongTien = findViewById(R.id.spinnerPhuongTien);

        txtNKH.setPaintFlags(txtNKH.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        txtNKT.setPaintFlags(txtNKT.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        formatGiaTien();
        showTTSpinner(spinnerDanhMuc,spinnerPhuongTien);

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

        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(AddTour.this);
                pd.setTitle("Đang cập nhật...");
                pd.show();
                String hinh1 = link1.getText().toString();
                String hinh2 = link2.getText().toString();
                String hinh3 = link3.getText().toString();
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

                if (hinh1==""){
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa gán hình cho tour", Toast.LENGTH_SHORT).show();
                    link1.setError("Vui lòng gán hình");
                    link1.requestFocus();
                }else if (TextUtils.isEmpty(tenTour)) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa đặt tên cho tour", Toast.LENGTH_SHORT).show();
                    edtTen.setError("Vui lòng đặt tên tour");
                    edtTen.requestFocus();
                } else if (TextUtils.isEmpty(ngayKhoiHanh)) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa chọn ngày khởi hành", Toast.LENGTH_SHORT).show();
                    txtNKH.setError("Vui lòng chọn ngày khởi hành");
                    txtNKH.requestFocus();
                } else if (TextUtils.isEmpty(ngayKetThuc)) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa chọn ngày kết thúc", Toast.LENGTH_SHORT).show();
                    txtNKT.setError("Vui lòng chọn ngày kết thúc");
                    txtNKT.requestFocus();
                } else if (giaTien==0) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa nhập giá tiền", Toast.LENGTH_SHORT).show();
                    edtGia.setError("Vui lòng nhập giá tiền");
                    edtGia.requestFocus();
                } else if (TextUtils.isEmpty(soVe)) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa nhập số vé", Toast.LENGTH_SHORT).show();
                    edtSoVe.setError("Vui lòng nhập số vé");
                    edtSoVe.requestFocus();
                } else if (TextUtils.isEmpty(gioiThieu)) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Chưa điền giới thiệu", Toast.LENGTH_SHORT).show();
                    edtGioiThieu.setError("Vui lòng điền giới thiệu");
                    edtGioiThieu.requestFocus();
                } else if (!flag) {
                    pd.dismiss();
                    Toast.makeText(AddTour.this, "Lỗi dữ liệu ngày kết thúc", Toast.LENGTH_SHORT).show();
                    txtNKT.setError("Vui lòng chọn lại ngày kết thúc");
                    txtNKT.requestFocus();
                } else{
                    taoTourMoi(pd,hinh1,hinh2,hinh3,tenTour,danhMuc,phuongTien,gioiThieu,ngayKhoiHanh,ngayKetThuc,tien,soVe);
                }
            }
        });

        link1.setOnClickListener(v -> {
            currentLink = link1;
            openFileChooser(); // Gọi hàm mở file chooser
        });

        link2.setOnClickListener(v -> {
            if(link1.getText().toString()==""){
                currentLink = link1;
            }else {
                currentLink = link2;
            }
            openFileChooser(); // Gọi hàm mở file chooser
        });

        link3.setOnClickListener(v -> {
            if(link1.getText().toString()==""){
                currentLink = link1;
            }else if(link2.getText().toString()==""){
                currentLink = link2;
            }else {
                currentLink = link3;
            }
            openFileChooser(); // Gọi hàm mở file chooser
        });

        txtNKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(AddTour.this, new DatePickerDialog.OnDateSetListener() {
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

                picker = new DatePickerDialog(AddTour.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ngayKetThuc = dayOfMonth + "/" + (month+1) +  "/" + year;
                        txtNKT.setText(ngayKetThuc);
                    }
                },year, month, day);
                picker.show();
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(AddTour.this,QuanLyTour.class);
                startActivity(ql);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void taoTourMoi(ProgressDialog pd, String hinh1, String hinh2, String hinh3, String tenTour,String danhMuc,
                            String phuongTien,String gioiThieu,String khoiHanh,String ketThuc,String gia,String soVe){
        FirebaseStorage reference = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
        String id;
        id = tourRef.push().getKey();
        if(hinh1!=""&&hinh2!=""&&hinh3!=""){ //hình 1, 2, 3
            Uri uri1 = Uri.parse(hinh1);
            Uri uri2 = Uri.parse(hinh2);
            Uri uri3 = Uri.parse(hinh3);
            uploadImage(pd,tenTour, uri2);
            uploadImage(pd,tenTour, uri3);
            StorageReference imgRef = reference.getReference().child("imagesTour/"+tenTour+"/"+System.currentTimeMillis()+"."+getFileExtension(uri1));
            imgRef.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Tour newTour = new Tour(id,tenTour,String.valueOf(uri),danhMuc,phuongTien,gioiThieu,khoiHanh,ketThuc,gia,Integer.parseInt(soVe));
                            tourRef.child(id).setValue(newTour);
                        }
                    });
                    Toast.makeText(AddTour.this,"Tạo tour thành công",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    Intent ql = new Intent(AddTour.this,QuanLyTour.class);
                    startActivity(ql);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });

        } else if (hinh1!=""&&hinh2!="") { //hình 1, 2
            Uri uri1 = Uri.parse(hinh1);
            Uri uri2 = Uri.parse(hinh2);
            uploadImage(pd,tenTour, uri2);
            StorageReference imgRef = reference.getReference().child("imagesTour/"+tenTour+"/"+System.currentTimeMillis()+"."+getFileExtension(uri1));
            imgRef.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Tour newTour = new Tour(id,tenTour,String.valueOf(uri),danhMuc,phuongTien,gioiThieu,khoiHanh,ketThuc,gia,Integer.parseInt(soVe));
                            tourRef.child(id).setValue(newTour);
                        }
                    });
                    Toast.makeText(AddTour.this,"Tạo tour thành công",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    Intent ql = new Intent(AddTour.this,QuanLyTour.class);
                    startActivity(ql);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });
        } else{ //hình 1
            Uri uri1 = Uri.parse(hinh1);
            StorageReference imgRef = reference.getReference().child("imagesTour/"+tenTour+"/"+System.currentTimeMillis()+"."+getFileExtension(uri1));
            imgRef.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Tour newTour = new Tour(id,tenTour,String.valueOf(uri),danhMuc,phuongTien,gioiThieu,khoiHanh,ketThuc,gia,Integer.parseInt(soVe));
                            tourRef.child(id).setValue(newTour);
                        }
                    });
                    Toast.makeText(AddTour.this,"Tạo tour thành công",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    Intent ql = new Intent(AddTour.this,QuanLyTour.class);
                    startActivity(ql);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });
        }
    }

    private void uploadImage(ProgressDialog pd, String tenTour, Uri uri){

        FirebaseStorage reference = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
        StorageReference imgRef = reference.getReference().child("imagesTour/"+tenTour+"/"+System.currentTimeMillis()+"."+getFileExtension(uri));
        imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTour.this,"Thất bại",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void formatGiaTien(){
        Locale localeVN = new Locale("vi", "VN");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(localeVN);
        dfs.setCurrencySymbol("₫");
        dfs.setGroupingSeparator('.');
        df = new DecimalFormat("#,###", dfs);

        edtGia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtGia.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    if (originalString.isEmpty()) {
                        giaTien = 0; // Cập nhật biến khi EditText rỗng
                        edtGia.setText("");
                        edtGia.setSelection(0);
                        edtGia.addTextChangedListener(this);
                        return;
                    }

                    String cleanString = originalString.replaceAll("[.,₫]", ""); // Loại bỏ dấu . và ₫
                    giaTien = Long.parseLong(cleanString); // Lưu giá trị chưa định dạng
                    String formatted = df.format(giaTien);
                    edtGia.setText(formatted);
                    edtGia.setSelection(formatted.length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    giaTien = 0; // Xử lý khi nhập không phải số
                    edtGia.setText("");
                }

                edtGia.addTextChangedListener(this);
            }
        });
    }

    private void showTTSpinner(Spinner spinnerDanhMuc, Spinner spinnerPhuongTien){
        //Spinner Danh mục
        danhMucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);
                    if(danhMuc!=null) {
                        tenDanhMuc.add(danhMuc.ten);
                    }
                }
                ArrayAdapter<String> danhMucAdapter = new ArrayAdapter<String>(AddTour.this,
                        android.R.layout.simple_spinner_item, tenDanhMuc);
                danhMucAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDanhMuc.setAdapter(danhMucAdapter);
                loaiDanhMuc = tenDanhMuc.get(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Spinner phương tiện
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(AddTour.this,
                R.array.phuongTien, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhuongTien.setAdapter(roleAdapter);
        loaiPhuongTien = "Xe";
    }

    //Chọn mở thư mục
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        selectImage.launch(intent);
    }

    //Chọn ảnh
    private final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String imageUriString = String.valueOf(uri);

                            if (currentLink != null) { // Kiểm tra biến tạm thời
                                currentLink.setText(imageUriString);
                                currentLink = null; // Reset biến tạm thời
                            }
                        } else {
                            Log.e("onActivityResult", "Uri is null");
                        }
                    } else {
                        Log.e("onActivityResult", "Data is null");
                    }
                }
            });

    //Kiểm tra ngày khởi hành-kết thúc
    private void kiemTraNgayThang() {
        try {
            Date dateKhoiHanh = dateFormat.parse(ngayKhoiHanh);
            Date dateKetThuc = dateFormat.parse(ngayKetThuc);

            if (dateKetThuc.before(dateKhoiHanh)) {
//                txtNKT.setError("Ngày kết thúc phải sau ngày khởi hành");
//                txtNKT.requestFocus();
//                Toast.makeText(AddTour.this, "Ngày kết thúc phải sau ngày khởi hành", Toast.LENGTH_SHORT).show();
//                ngayKetThuc = null; // Reset ngày kết thúc nếu không hợp lệ
//                txtNKT.setText("");
                flag = false;
            } else {
//                txtNKT.setError(null); // Xóa lỗi nếu hợp lệ
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddTour.this, "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}