package com.example.tourdulich.Page;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.tourdulich.Adapter.DanhMucAdapter;
import com.example.tourdulich.Adapter.UserAdapter;
import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class QuanLyDanhMuc extends AppCompatActivity {

    private Button btnQuayLai;
    private Button btnThem;
    private ListView lvDanhMuc;
    private ArrayList<DanhMuc> arrayDanhMuc;
    private DanhMucAdapter danhMucAdapter;
    private ProgressBar progressBar;
    private ImageView imgHinh;

    private Uri hinh;
    private String img = "https://firebasestorage.googleapis.com/v0/b/tourdulich-ae976.firebasestorage.app/o/imagesDanhMuc%2F1734511298957.jpg?alt=media&token=d0ab7cdd-cc98-4a5f-9db0-c2e4defe0d39";
    private String imgDef ="https://firebasestorage.googleapis.com/v0/b/tourdulich-ae976.firebasestorage.app/o/imagesDanhMuc%2F1734511298957.jpg?alt=media&token=d0ab7cdd-cc98-4a5f-9db0-c2e4defe0d39";

    private DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference("Danh mục");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        btnQuayLai = findViewById(R.id.btnQuayLaiTuQuanLyDanhMuc);
        btnThem = findViewById(R.id.btnThemDanhMuc);
        lvDanhMuc = findViewById(R.id.listDanhMuc);
        arrayDanhMuc = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar7);
        progressBar.setVisibility(View.VISIBLE);

        showDanhSach();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(QuanLyDanhMuc.this, TrangChuAdmin.class);
                startActivity(ql);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDanhMuc.this);
                builder.setTitle("Thêm danh mục");
                View view = getLayoutInflater().inflate(R.layout.add_danh_muc, null);
                builder.setView(view);
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();

                Button btnHuy = view.findViewById(R.id.btnHuyDanhMuc);
                Button btnDongY = view.findViewById(R.id.btnXacNhanDanhMuc);
                imgHinh = view.findViewById(R.id.imgVChonAnhDanhMuc);
                EditText edtTen = view.findViewById(R.id.edtTenDanhMuc);

                imgHinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // Sử dụng ActivityResultLauncher thay cho startActivityForResult
                        selectImage.launch(intent);
                    }
                });

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                btnDongY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog pd = new ProgressDialog(QuanLyDanhMuc.this);
                        pd.setTitle("Đang cập nhật...");
                        pd.show();
                        String id;
                        String tenDanhMuc = edtTen.getText().toString();
                        String name;
                        if(TextUtils.isEmpty(tenDanhMuc)){
                            Toast.makeText(QuanLyDanhMuc.this, "Chưa đặt tên danh mục", Toast.LENGTH_SHORT).show();
                            edtTen.setError("Vui lòng điền tên đăng nhập");
                            edtTen.requestFocus();
                            pd.dismiss();
                        }else{
                            id = danhMucRef.push().getKey();
                            if(img!=imgDef) {
                                FirebaseStorage reference = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
                                name = System.currentTimeMillis() + "." + getFileExtension(Uri.parse(img));
                                StorageReference imgRef = reference.getReference().child("imagesDanhMuc/" + tenDanhMuc + "/" + name);
                                imgRef.putFile(Uri.parse(img)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                DanhMuc danhMuc = new DanhMuc(id, tenDanhMuc, String.valueOf(uri));

                                                danhMucRef.child(id).setValue(danhMuc);

                                            }
                                        });
                                        Toast.makeText(QuanLyDanhMuc.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        dialog.dismiss();
                                        //
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                                    }
                                });
                            }else{
                                DanhMuc danhMuc = new DanhMuc(id, tenDanhMuc, imgDef);
                                danhMucRef.child(id).setValue(danhMuc);
                                Toast.makeText(QuanLyDanhMuc.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                dialog.dismiss();
                            }
                        }
                    }
                });

            }
        });

        lvDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DanhMuc danhMuc = arrayDanhMuc.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDanhMuc.this);
                builder.setTitle("Chỉnh sửa danh mục");
                View dialogView = getLayoutInflater().inflate(R.layout.info_danh_muc, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();

                Button btnHuy = dialogView.findViewById(R.id.btnHuyCapNhatDanhMuc);
                Button btnXacNhan = dialogView.findViewById(R.id.btnXacNhanCapNhatDanhMuc);
                Button btnXoa = dialogView.findViewById(R.id.btnXoaDanhMuc);
                imgHinh = dialogView.findViewById(R.id.imgVCapNhatAnhDanhMuc);
                EditText edtTen = dialogView.findViewById(R.id.edtCapNhatTenDanhMuc);

                Glide.with(QuanLyDanhMuc.this)
                        .load(Uri.parse(danhMuc.hinh))
                        .into(imgHinh);
                img=danhMuc.hinh;
                imgDef=danhMuc.hinh;
                edtTen.setText(danhMuc.ten);

                imgHinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // Sử dụng ActivityResultLauncher thay cho startActivityForResult
                        selectImage.launch(intent);
                    }
                });

                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder build = new AlertDialog.Builder(QuanLyDanhMuc.this);
                        build.setTitle("Thông báo!!!");
                        build.setMessage("Bạn có chắc muốn xóa danh mục này?");
                        build.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                Toast.makeText(QuanLyDanhMuc.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
                                danhMucRef.child(danhMuc.id).removeValue();
                                dialog.dismiss();
                            }
                        });
                        build.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog.dismiss();
                            }
                        });
                        build.show();
                    }
                });

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();  // Đóng dialog khi nhấn nút Hủy
                    }
                });

                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog pd = new ProgressDialog(QuanLyDanhMuc.this);
                        pd.setTitle("Đang cập nhật...");
                        pd.show();
                        String id = danhMuc.id;
                        String tenDanhMuc = edtTen.getText().toString();
                        String name;
                        if(TextUtils.isEmpty(tenDanhMuc)){
                            Toast.makeText(QuanLyDanhMuc.this, "Chưa đặt tên danh mục", Toast.LENGTH_SHORT).show();
                            edtTen.setError("Vui lòng điền tên đăng nhập");
                            edtTen.requestFocus();
                            pd.dismiss();
                        }else{
                            if(img.equals(imgDef)){
                                danhMucRef.child(id).child("ten").setValue(tenDanhMuc);
                                Toast.makeText(QuanLyDanhMuc.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                dialog.dismiss();
                            }else {
                                FirebaseStorage reference = FirebaseStorage.getInstance("gs://tourdulich-ae976.firebasestorage.app");
                                name = System.currentTimeMillis() + "." + getFileExtension(Uri.parse(img));
                                StorageReference imgRef = reference.getReference().child("imagesDanhMuc/" + tenDanhMuc + "/" + name);
                                imgRef.putFile(Uri.parse(img)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                DanhMuc danhmuc = new DanhMuc(id, tenDanhMuc, String.valueOf(uri));

                                                danhMucRef.child(id).setValue(danhmuc);

                                            }
                                        });
                                        Toast.makeText(QuanLyDanhMuc.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        dialog.dismiss();
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
                        }
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDanhSach() {
        danhMucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayDanhMuc.clear(); // Xóa danh sách cũ trước khi tải mới

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy từng tour từ Firebase
                        DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                        if (danhMuc != null) {
                            arrayDanhMuc.add(danhMuc); // Thêm vào danh sách nếu hợp lệ
                        }
                    }

                    // Kiểm tra danh sách sau khi tải
                    if (!arrayDanhMuc.isEmpty()) {
                        // Gắn adapter và hiển thị dữ liệu
                        if (danhMucAdapter == null) {
                            danhMucAdapter = new DanhMucAdapter(QuanLyDanhMuc.this, arrayDanhMuc);
                            lvDanhMuc.setAdapter(danhMucAdapter);
                        } else {
                            danhMucAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(QuanLyDanhMuc.this, "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuanLyDanhMuc.this, "Không có danh mục nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }

                // Ẩn progress bar sau khi tải xong
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE); // Ẩn progress bar nếu xảy ra lỗi
                Toast.makeText(QuanLyDanhMuc.this, "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}