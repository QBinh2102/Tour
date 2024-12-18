package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChinhSuaThongTinCaNhan extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnCapNhat;
    private RadioButton RbtNam;
    private RadioButton RbtNu;
    private boolean gt = true;

    private FirebaseAuth xacThucFirebase;
    private FirebaseUser firebaseUser;
    private Uri imageUri;


    private Uri hinh;
    String name;
    String img;
    String imgChange;
    private String tenHoSo, SDT, diaChi, ngaySinh, gioiTinh, email;
    private ImageView imgHinhDaiDien;
    private EditText edtTen;
    private EditText edtSDT;
    private EditText edtDiaChi;
    private TextView txtDate;

    private DatePickerDialog picker;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chinh_sua_thong_tin_ca_nhan);

        btnQuayLai = findViewById(R.id.btQuayLaiTuChinhSuaTTCN);
        btnCapNhat = findViewById(R.id.btCapNhatChinhSua);
        edtTen = findViewById(R.id.editTextTenChinhSua);
        imgHinhDaiDien = findViewById(R.id.imageViewHinhChinhSua);
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

        //Chọn hình ảnh
        imgHinhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                // Sử dụng ActivityResultLauncher thay cho startActivityForResult
                selectImage.launch(intent);
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

        //Nhấn nút CHỈNH SỬA HỒ SƠ
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenHoSo = edtTen.getText().toString();
                diaChi = edtDiaChi.getText().toString();
                SDT = edtSDT.getText().toString();
                ngaySinh = txtDate.getText().toString();
                if(gt){
                    gioiTinh = "Nam";
                }else{
                    gioiTinh = "Nữ";
                }

                //Kiem tra so dien thoai co hop li khong
                //So dien thoai chi hop ly khi bat dau voi 3 so ben duoi va 7 so con lai tu 0->9
                String quyDinhSDT = "(039|038|037|036|035|034|033|032|096|097|098|086|083|084|085|081" +
                        "|088|082|070|079|077|076|078|090|093|089|052|056|058|092)[0-9]{7}";
                Matcher mauKiemTraSDT;
                Pattern sdtHopLe = Pattern.compile(quyDinhSDT);
                mauKiemTraSDT = sdtHopLe.matcher(SDT);

                //Kiểm lỗi người dùng
                if(TextUtils.isEmpty(tenHoSo)) {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtTen.setError("Vui lòng điền tên đăng nhập");
                    edtTen.requestFocus();
                } else if (TextUtils.isEmpty(diaChi)) {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtDiaChi.setError("Vui lòng điền địa chỉ");
                    edtDiaChi.requestFocus();
                } else if (TextUtils.isEmpty(SDT)) {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    edtSDT.setError("Vui lòng điền số điện thoại");
                    edtSDT.requestFocus();
                } else if (SDT.length() != 10) {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Vui lòng xem lại số điện thoại",
                            Toast.LENGTH_LONG).show();
                    edtSDT.setError("Số điện thoại bao gồm 10 số");
                    edtSDT.requestFocus();
                } else if (TextUtils.isEmpty(ngaySinh)) {
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    txtDate.setError("Vui lòng nhập ngày sinh");
                    txtDate.requestFocus();
                } else{
                    UpdateUser(imgChange,tenHoSo,email,diaChi,SDT,ngaySinh,gioiTinh);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Hiện thông tin người dùng
    public void HienThiThongTin(FirebaseUser firebaseUser){
        String userID = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LuuThongTinUser thongTinUser = snapshot.getValue(LuuThongTinUser.class);
                tenHoSo = thongTinUser.tenNguoiDung;
                hinh = Uri.parse(thongTinUser.hinhDaiDien);
                diaChi = thongTinUser.diaChi;
                SDT = thongTinUser.soDienThoai;
                ngaySinh = thongTinUser.ngaySinh;
                gioiTinh = thongTinUser.gioiTinh;
                email = thongTinUser.email;

                edtTen.setText(tenHoSo);
                Glide.with(ChinhSuaThongTinCaNhan.this)
                        .load(hinh)
                        .into(imgHinhDaiDien);
                img = thongTinUser.hinhDaiDien;
                imgChange = thongTinUser.hinhDaiDien;
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

    //Cập nhật hồ sơ người dùng
    private void UpdateUser(String hinh, String username, String email, String diaChi, String dienThoai, String ngaySinh, String gioiTinh){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang cập nhật...");
        pd.show();

        if(img.equals(imgChange)){
            mDatabase.child(firebaseUser.getUid()).child("diaChi").setValue(diaChi);
            mDatabase.child(firebaseUser.getUid()).child("gioiTinh").setValue(gioiTinh);
            mDatabase.child(firebaseUser.getUid()).child("ngaySinh").setValue(ngaySinh);
            mDatabase.child(firebaseUser.getUid()).child("soDienThoai").setValue(dienThoai);
            mDatabase.child(firebaseUser.getUid()).child("tenNguoiDung").setValue(username);
            Toast.makeText(ChinhSuaThongTinCaNhan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            Intent ttcn = new Intent(ChinhSuaThongTinCaNhan.this, QuanLyUser.class);
            startActivity(ttcn);
        }else {
            FirebaseStorage reference = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
            //Đặt tên hình trên storage
            name = System.currentTimeMillis() + "." + getFileExtension(Uri.parse(hinh));
            StorageReference imgRef = reference.getReference().child("imagesUser/" + firebaseUser.getUid() + "/" + name);
            imgRef.putFile(Uri.parse(hinh)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    firebaseUser.updateProfile(profileUpdates);
                    String userID = firebaseUser.getUid();
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            LuuThongTinUser user = new LuuThongTinUser(userID, username, String.valueOf(uri), diaChi, dienThoai, email, ngaySinh, gioiTinh, "user");

                            mDatabase.child(userID).setValue(user);

                        }
                    });
                    Toast.makeText(ChinhSuaThongTinCaNhan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent ttcn = new Intent(ChinhSuaThongTinCaNhan.this, TrangChu.class);
                    startActivity(ttcn);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    //
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });

        }
//        mDatabase.child(userID).child("hinhDaiDien").setValue(hinh);
//        mDatabase.child(userID).child("diaChi").setValue(diaChi);
//        mDatabase.child(userID).child("gioiTinh").setValue(gioiTinh);
//        mDatabase.child(userID).child("ngaySinh").setValue(ngaySinh);
//        mDatabase.child(userID).child("soDienThoai").setValue(dienThoai);
    }

    // Khai báo ActivityResultLauncher
    private final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK){
                        if(o.getData()!=null) {
                            hinh = o.getData().getData();
                            imgChange = String.valueOf(hinh);
                            imgHinhDaiDien.setImageURI(hinh);
                        }
                    }
                }
            }
    );

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}