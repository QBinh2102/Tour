package com.example.tourdulich.Page;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrangChuAdmin extends AppCompatActivity {

    private LinearLayout danhMucContainer;
    ArrayList<DanhMuc> danhMucList = new ArrayList<>();
    private Uri selectedImageUri; // Dùng để lưu trữ URI của ảnh được chọn
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_admin);

        danhMucContainer = findViewById(R.id.layoutDanhMuc);

        loadDanhMuc();

        ImageView imgVPopupMenu = findViewById(R.id.imgViewPopupMenu);
        imgVPopupMenu.setOnClickListener(view -> {
            Dialog dialog = new Dialog(TrangChuAdmin.this);
            dialog.setContentView(R.layout.dialog_menu_danh_muc);
            dialog.setCancelable(true);  // Cho phép đóng dialog khi bấm ra ngoài

            Button btnCong = dialog.findViewById(R.id.menu_add);
            Button btnSua = dialog.findViewById(R.id.menu_edit);

            btnCong.setOnClickListener(v -> {
                themDanhMuc();
                dialog.dismiss();
            });
            btnSua.setOnClickListener(v -> {
                try {
                    // Tạo một ImageView giả lập để truyền vào phương thức suaDanhMuc
                    ImageView imgViewToEdit = new ImageView(this);
                    imgViewToEdit.setTag("Danh mục cần sửa");  // Gắn tag với tên danh mục cần sửa

                    // Gọi phương thức suaDanhMuc với đối tượng ImageView
                    suaDanhMuc(imgViewToEdit);
                } catch (Exception e) {
                    Log.e("TrangChuAdmin", "Error in btnSua OnClickListener: " + e.getMessage(), e);
                    Toast.makeText(this, "Đã xảy ra lỗi khi mở dialog sửa danh mục!", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        });
    }

    private void suaDanhMuc(ImageView imageView) {
        String category = (String) imageView.getTag();  // Lấy category từ tag của imageView
        if (category == null || category.isEmpty()) {
            Toast.makeText(this, "Danh mục không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Truyền danh sách danh mục vào Intent
        Intent intent = new Intent(TrangChuAdmin.this, SuaVaXoaDanhMuc.class);
        ArrayList<DanhMuc> danhMucList = getDanhMucList(); // Giả sử bạn có một phương thức để lấy danh sách danh mục
        intent.putParcelableArrayListExtra("danhMucList", danhMucList);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
    }

    // Phương thức lấy danh sách danh mục từ Firebase
    private ArrayList<DanhMuc> getDanhMucList() {
        ArrayList<DanhMuc> danhMucList = new ArrayList<>();
        // Lấy dữ liệu từ Firebase và đưa vào danh sách danh mục
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Danh mục");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);
                    if (danhMuc != null) {
                        danhMucList.add(danhMuc);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching danh mục: " + error.getMessage());
            }
        });
        return danhMucList;
    }


    private void loadDanhMuc() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Danh mục");

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                if (danhMuc != null) {
                    addDanhMucToLayout(danhMuc.ten, danhMuc.hinh); // Add to layout
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Handle category update if necessary
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle category removal if necessary
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Handle child move if necessary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Cannot load categories: " + error.getMessage());
            }
        });
    }

    private void addDanhMucToLayout(String danhMuc, String hinhUrl) {
        LinearLayout container = findViewById(R.id.layoutDanhMuc);
        LinearLayout currentRow = null;

        if (container.getChildCount() == 0 || ((LinearLayout) container.getChildAt(container.getChildCount() - 1)).getChildCount() >= 3) {
            currentRow = new LinearLayout(this);
            currentRow.setOrientation(LinearLayout.HORIZONTAL);
            currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            currentRow.setPadding(5, 5, 5, 5);
            container.addView(currentRow);
        } else {
            currentRow = (LinearLayout) container.getChildAt(container.getChildCount() - 1);
        }

        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        cardParams.setMargins(10, 5, 10, 5);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(0);
        cardView.setRadius(10f);
        cardView.setPadding(0, 0, 0, 0);
        cardView.setCardBackgroundColor(Color.WHITE);

        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        frameLayout.setLayoutParams(frameParams);

        ImageView imageView = new ImageView(this);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, getResources().getDisplayMetrics())
        );
        imageParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageParams);

        // Kiểm tra nếu có URL hình ảnh, dùng Glide để tải ảnh từ URL Firebase
        if (hinhUrl != null && !hinhUrl.isEmpty()) {
            Glide.with(this)
                    .load(hinhUrl)
                    .placeholder(R.drawable.danh_muc_mac_dinh)
                    .error(R.drawable.danh_muc_mac_dinh)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.danh_muc_mac_dinh);  // Hình mặc định
        }

        frameLayout.addView(imageView);
        cardView.addView(frameLayout);

        currentRow.addView(cardView);
    }



    private void themDanhMuc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm danh mục mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText edtTenDanhMuc = new EditText(this);
        edtTenDanhMuc.setHint("Nhập tên danh mục");
        layout.addView(edtTenDanhMuc);

        Button btnChonHinh = new Button(this);
        btnChonHinh.setText("Chọn hình ảnh");
        layout.addView(btnChonHinh);

        builder.setView(layout);

        btnChonHinh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String tenDanhMuc = edtTenDanhMuc.getText().toString().trim();

            if (tenDanhMuc.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hinhUrl = selectedImageUri != null ? selectedImageUri.toString() : null;

            // Save category to Firebase
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Danh mục");
            String key = dbRef.push().getKey();
            if (key != null) {
                DanhMuc danhMuc = new DanhMuc(tenDanhMuc, hinhUrl);
                dbRef.child(key).setValue(danhMuc);
                Toast.makeText(TrangChuAdmin.this, "Danh mục đã được thêm", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Log.d("Image URI", "URI: " + selectedImageUri.toString());  // Log URI để kiểm tra
        }
    }
}
