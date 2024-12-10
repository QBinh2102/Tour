package com.example.tourdulich.Page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tourdulich.Database.DanhMuc;
import com.example.tourdulich.R;
import com.google.firebase.database.*;
import java.util.HashMap;
import java.util.Map;

public class SuaVaXoaDanhMuc extends AppCompatActivity {
    private static final String DATABASE_PATH = "danhMuc";  // Đường dẫn Firebase
    private GridLayout gridDanhMuc;  // GridLayout chứa các danh mục
    private Button btnDelete;
    private Map<String, CheckBox> checkBoxMap;
    private Map<String, ImageView> categoryImagesMap;
    private DatabaseReference dbRef;  // Firebase reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_va_xoa_danh_muc);

        gridDanhMuc = findViewById(R.id.gridDanhMuc);
        btnDelete = findViewById(R.id.btnDelete);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Thiết lập số cột cho GridLayout
        gridDanhMuc.setColumnCount(3); // 3 cột trong grid

        checkBoxMap = new HashMap<>(); // Khởi tạo Map lưu CheckBox
        categoryImagesMap = new HashMap<>(); // Khởi tạo Map lưu ImageView

        // Kết nối Firebase
        dbRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        Log.d("Firebase", "Kết nối thành công!");
        // Lắng nghe thay đổi dữ liệu từ Firebase
        loadDanhMuc();

        btnDelete.setOnClickListener(v -> {
            boolean isDeleted = false;

            for (String key : checkBoxMap.keySet()) {
                CheckBox checkBox = checkBoxMap.get(key);
                if (checkBox.isChecked()) {
                    deleteDanhMuc(key); // Xóa trong Firebase
                    LinearLayout categoryLayout = (LinearLayout) checkBox.getParent();
                    gridDanhMuc.removeView(categoryLayout); // Xóa giao diện
                    isDeleted = true;
                }
            }

            if (isDeleted) {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được xóa!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Chưa chọn danh mục để xóa!", Toast.LENGTH_SHORT).show();
            }
        });


        btnSave.setOnClickListener(v -> {
            CheckBox selectedCheckBox = null;
            String selectedCategoryKey = null;

            // Xác định danh mục được chọn (chỉ một danh mục)
            for (String key : checkBoxMap.keySet()) {
                CheckBox checkBox = checkBoxMap.get(key);
                if (checkBox.isChecked()) {
                    if (selectedCheckBox != null) {
                        Toast.makeText(SuaVaXoaDanhMuc.this, "Chỉ được chọn một danh mục để sửa!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectedCheckBox = checkBox;
                    selectedCategoryKey = key;
                }
            }

            if (selectedCheckBox == null) {
                // Nếu không chọn danh mục nào, trở về trang chủ
                Intent intent = new Intent(SuaVaXoaDanhMuc.this, TrangChuAdmin.class);
                startActivity(intent);
                finish();
                return;
            }

            // Cập nhật dữ liệu nếu danh mục được chọn
            LinearLayout categoryLayout = (LinearLayout) selectedCheckBox.getParent();
            TextView categoryName = (TextView) categoryLayout.getChildAt(0);

            String newCategoryName = categoryName.getText().toString(); // Tên mới
            DatabaseReference categoryRef = dbRef.child(selectedCategoryKey);

            categoryRef.child("ten").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String currentName = task.getResult().getValue(String.class);

                    // Chỉ cập nhật nếu có thay đổi
                    if (!newCategoryName.equals(currentName)) {
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("ten", newCategoryName);

                        categoryRef.updateChildren(updatedData).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được cập nhật!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SuaVaXoaDanhMuc.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                            }
                            // Dù thành công hay thất bại, quay về trang chủ
                            Intent intent = new Intent(SuaVaXoaDanhMuc.this, TrangChuAdmin.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        // Nếu không thay đổi, chỉ trở về trang chủ
                        Intent intent = new Intent(SuaVaXoaDanhMuc.this, TrangChuAdmin.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(SuaVaXoaDanhMuc.this, "Không thể lấy dữ liệu hiện tại!", Toast.LENGTH_SHORT).show();
                }
            });
        });


        btnCancel.setOnClickListener(v -> {
            Toast.makeText(SuaVaXoaDanhMuc.this, "Không có thay đổi nào được lưu!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private void loadDanhMuc() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gridDanhMuc.removeAllViews();
                checkBoxMap.clear();

                if (!dataSnapshot.exists()) {
                    Toast.makeText(SuaVaXoaDanhMuc.this, "Không có danh mục nào!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    if (danhMuc != null) {
                        Log.d("DanhMuc", "Tên: " + danhMuc.ten + ", Hình: " + danhMuc.hinh);
                        addDanhMucToLayout(danhMuc, snapshot.getKey());
                    } else {
                        Log.d("DanhMuc", "Dữ liệu null từ Firebase");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addDanhMucToLayout(DanhMuc danhMuc, String key) {
        if (checkBoxMap.containsKey(key)) {
            Log.w("ViewDuplication", "View với Key = " + key + " đã tồn tại, bỏ qua");
            return;
        }

        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.VERTICAL);
        categoryLayout.setGravity(Gravity.CENTER);

        TextView categoryName = new TextView(this);
        categoryName.setText(danhMuc.ten != null ? danhMuc.ten : "Không tên");

        // Kiểm tra lại hình ảnh từ Firebase
        ImageView categoryImage = new ImageView(this);
        if (danhMuc.hinh != null && !danhMuc.hinh.isEmpty()) {
            // Kiểm tra nếu đường dẫn là URI hợp lệ
            if (danhMuc.hinh.startsWith("android.resource")) {
                Uri imageUri = Uri.parse(danhMuc.hinh);
                categoryImage.setImageURI(imageUri);  // Đặt hình ảnh từ resource
            } else {
                Glide.with(this).load(danhMuc.hinh).into(categoryImage);  // Nếu là URL, dùng Glide
            }
        } else {
            categoryImage.setImageResource(R.drawable.danh_muc_mac_dinh);  // Hình mặc định nếu không có ảnh
        }

        CheckBox checkBox = new CheckBox(this);
        checkBox.setTag(key);
        checkBoxMap.put(key, checkBox);

        // Thêm vào layout
        categoryLayout.addView(categoryName);
        categoryLayout.addView(categoryImage);
        categoryLayout.addView(checkBox);

        gridDanhMuc.addView(categoryLayout);
        Log.d("GridLayout", "Thêm danh mục vào GridLayout: Key = " + key);
    }


    private void deleteDanhMuc(String categoryToDelete) {
        dbRef.child(categoryToDelete).removeValue(); // Xóa danh mục khỏi Firebase
    }
}
