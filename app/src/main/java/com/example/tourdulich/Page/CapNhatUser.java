package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapNhatUser extends AppCompatActivity {

    private Button btnQuayLai;
    private ImageView imgHinh;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtPhone;
    private TextView txtBirth;
    private Spinner role;
    private RadioButton rdbNam;
    private RadioButton rdbNu;
    private Button btnDel;
    private Button btnUpload;

    private String tenHoSo, SDT, diaChi, ngaySinh, gioiTinh;
    private boolean gt=true;
    private String name;
    private String vaiTro;
    private Uri hinh;
    private String img;
    private DatePickerDialog picker;
    private ProgressBar progressBar;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cap_nhat_user);

        btnQuayLai = findViewById(R.id.btQuayLaiTuCapNhatUser);
        imgHinh = findViewById(R.id.imageViewHinhCapNhat);
        txtName = findViewById(R.id.editTextTenCapNhat);
        txtAddress = findViewById(R.id.editTextDiaChiCapNhat);
        txtPhone = findViewById(R.id.editTextPhoneCapNhat);
        txtBirth = findViewById(R.id.textDateCapNhat);
        role = findViewById(R.id.spinnerCapNhatUser);
        rdbNam = findViewById(R.id.radioNamCapNhat);
        rdbNu = findViewById(R.id.radioNuCapNhat);
        btnDel = findViewById(R.id.btnDeleteUser);
        btnUpload = findViewById(R.id.btnUploadUser);
        progressBar = findViewById(R.id.progressBar6);

        Intent intent = getIntent();
        LuuThongTinUser user = (LuuThongTinUser) intent.getSerializableExtra("user_item");

        showTT(user);

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    vaiTro = "user";
                } else if (position == 1) {
                    vaiTro = "admin";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Chọn hình ảnh
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                // Sử dụng ActivityResultLauncher thay cho startActivityForResult
                selectImage.launch(intent);
            }
        });

        txtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(CapNhatUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtBirth.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                    }
                },year, month, day);
                picker.show();
            }
        });

        //Chọn giới tính
        rdbNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gt=true;
                rdbNu.setChecked(false);
                rdbNam.setChecked(true);
            }
        });
        rdbNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gt=false;
                rdbNam.setChecked(false);
                rdbNu.setChecked(true);
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(CapNhatUser.this, QuanLyUser.class);
                startActivity(ql);
            }
        });

        //Nhấn nút CHỈNH SỬA HỒ SƠ
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tenHoSo = txtName.getText().toString();
                diaChi = txtAddress.getText().toString();
                SDT = txtPhone.getText().toString();
                ngaySinh = txtBirth.getText().toString();

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
                    Toast.makeText(CapNhatUser.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    txtName.setError("Vui lòng điền tên đăng nhập");
                    txtName.requestFocus();
                } else if (TextUtils.isEmpty(diaChi)) {
                    Toast.makeText(CapNhatUser.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    txtAddress.setError("Vui lòng điền địa chỉ");
                    txtAddress.requestFocus();
                } else if (TextUtils.isEmpty(SDT)) {
                    Toast.makeText(CapNhatUser.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    txtPhone.setError("Vui lòng điền số điện thoại");
                    txtPhone.requestFocus();
                } else if (SDT.length() != 10) {
                    Toast.makeText(CapNhatUser.this, "Vui lòng xem lại số điện thoại",
                            Toast.LENGTH_LONG).show();
                    txtPhone.setError("Số điện thoại bao gồm 10 số");
                    txtPhone.requestFocus();
                } else if (TextUtils.isEmpty(ngaySinh)) {
                    Toast.makeText(CapNhatUser.this, "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_LONG).show();
                    txtBirth.setError("Vui lòng nhập ngày sinh");
                    txtBirth.requestFocus();
                } else{

                    UpdateUser(user,img,tenHoSo,diaChi,SDT,ngaySinh,gioiTinh,vaiTro);
                }
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CapNhatUser.this);
                dialog.setTitle("Thông báo!!!");
                dialog.setMessage("Bạn có chắc muốn xóa tài khoản này?");
                dialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child(user.id).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(CapNhatUser.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                    Intent ql = new Intent(CapNhatUser.this, QuanLyUser.class);
                                    startActivity(ql);
                                })
                                .addOnFailureListener(e -> {

                                });
                    }
                });
                dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Cập nhật hồ sơ người dùng
    private void UpdateUser(LuuThongTinUser nguoiDung, String hinh, String username, String diaChi, String dienThoai, String ngaySinh, String gioiTinh, String vaiTro){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang cập nhật...");
        pd.show();

        if(nguoiDung.hinhDaiDien.equals(hinh)){
            mDatabase.child(nguoiDung.id).child("diaChi").setValue(diaChi);
            mDatabase.child(nguoiDung.id).child("gioiTinh").setValue(gioiTinh);
            mDatabase.child(nguoiDung.id).child("ngaySinh").setValue(ngaySinh);
            mDatabase.child(nguoiDung.id).child("soDienThoai").setValue(dienThoai);
            mDatabase.child(nguoiDung.id).child("tenNguoiDung").setValue(username);
            mDatabase.child(nguoiDung.id).child("role").setValue(vaiTro);
            Toast.makeText(CapNhatUser.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            Intent ttcn = new Intent(CapNhatUser.this, QuanLyUser.class);
            startActivity(ttcn);
        }else {
            FirebaseStorage reference = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
            //Đặt tên hình trên storage
            name = System.currentTimeMillis() + "." + getFileExtension(Uri.parse(hinh));
            StorageReference imgRef = reference.getReference().child("imagesUser/" + nguoiDung.id + "/" + name);
            imgRef.putFile(Uri.parse(hinh)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDatabase.child(nguoiDung.id).child("hinhDaiDien").setValue(String.valueOf(uri));
                            mDatabase.child(nguoiDung.id).child("diaChi").setValue(diaChi);
                            mDatabase.child(nguoiDung.id).child("gioiTinh").setValue(gioiTinh);
                            mDatabase.child(nguoiDung.id).child("ngaySinh").setValue(ngaySinh);
                            mDatabase.child(nguoiDung.id).child("soDienThoai").setValue(dienThoai);
                            mDatabase.child(nguoiDung.id).child("tenNguoiDung").setValue(username);
                            mDatabase.child(nguoiDung.id).child("role").setValue(vaiTro);
                        }
                    });

                    Toast.makeText(CapNhatUser.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent ttcn = new Intent(CapNhatUser.this, QuanLyUser.class);
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


//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
//        mDatabase.child(nguoiDung.id).child("diaChi").setValue(diaChi);
//        mDatabase.child(nguoiDung.id).child("gioiTinh").setValue(gioiTinh);
//        mDatabase.child(nguoiDung.id).child("ngaySinh").setValue(ngaySinh);
//        mDatabase.child(nguoiDung.id).child("soDienThoai").setValue(dienThoai);
//        mDatabase.child(nguoiDung.id).child("tenNguoiDung").setValue(username);
//        mDatabase.child(nguoiDung.id).child("role").setValue(vaiTro);

    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void showTT(LuuThongTinUser user) {
        Glide.with(CapNhatUser.this)
                .load(user.hinhDaiDien)
                .into(imgHinh);
        img = user.hinhDaiDien;
        txtName.setText(user.tenNguoiDung);
        txtBirth.setText(user.ngaySinh);
        txtAddress.setText(user.diaChi);
        txtPhone.setText(user.soDienThoai);
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(CapNhatUser.this,
                R.array.role, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(roleAdapter);
        if(user.role.equals("user")){
            role.setSelection(0);
        }else
            role.setSelection(1);
        vaiTro=user.role;
        if(user.gioiTinh.equals("Nam")){
            rdbNam.setChecked(true);
            rdbNu.setChecked(false);
        }else{
            rdbNam.setChecked(false);
            rdbNu.setChecked(true);
        }
    }


    // Khai báo ActivityResultLauncher
    private final ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK){
                        if(o.getData()!=null) {
                            hinh = o.getData().getData();
                            img = String.valueOf(hinh);
                            imgHinh.setImageURI(hinh);
                        }
                    }
                }
            }
    );
}