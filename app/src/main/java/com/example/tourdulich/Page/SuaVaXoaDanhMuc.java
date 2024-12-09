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

        // Lắng nghe thay đổi dữ liệu từ Firebase
        loadDanhMuc();

        btnDelete.setOnClickListener(v -> {
            boolean isDeleted = false; // Biến kiểm tra xem có danh mục nào được xóa hay không

            for (int i = 0; i < gridDanhMuc.getChildCount(); i++) {
                LinearLayout categoryLayout = (LinearLayout) gridDanhMuc.getChildAt(i);
                CheckBox checkBox = (CheckBox) categoryLayout.getChildAt(2); // CheckBox là phần tử thứ 3

                if (checkBox != null && checkBox.isChecked()) {
                    String category = (String) checkBox.getTag(); // Lấy tag của checkbox, là tên danh mục
                    deleteDanhMuc(category);  // Xóa danh mục khỏi Firebase
                    gridDanhMuc.removeViewAt(i);  // Xóa phần tử trong GridLayout
                    isDeleted = true;
                    i--;  // Giảm chỉ số để tránh bỏ sót phần tử sau khi xóa
                }
            }

            if (isDeleted) {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được xóa!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Chưa chọn danh mục để xóa!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> {
            StringBuilder danhMucMoi = new StringBuilder();

            for (int i = 0; i < gridDanhMuc.getChildCount(); i++) {
                LinearLayout categoryLayout = (LinearLayout) gridDanhMuc.getChildAt(i);
                CheckBox checkBox = (CheckBox) categoryLayout.getChildAt(2); // CheckBox là phần tử thứ 3
                String category = (String) checkBox.getTag(); // Lấy tên danh mục từ tag
                danhMucMoi.append(category).append(","); // Thêm vào danh sách mới
            }

            // Ghi đè danh sách mới lên danh sách cũ trong Firebase
            dbRef.setValue(danhMucMoi.toString());

            Toast.makeText(SuaVaXoaDanhMuc.this, "Danh mục đã được cập nhật!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SuaVaXoaDanhMuc.this, TrangChuAdmin.class);
            startActivity(intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadDanhMuc() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("danhmuc");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(SuaVaXoaDanhMuc.this, "Không có danh mục nào!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    if (danhMuc != null) {
                        // Log để kiểm tra dữ liệu
                        Log.d("DanhMuc", "Tên danh mục: " + danhMuc.ten);
                        addDanhMucToLayout(danhMuc);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SuaVaXoaDanhMuc.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void addDanhMucToLayout(DanhMuc danhMuc) {
        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.VERTICAL);
        categoryLayout.setGravity(Gravity.CENTER);

        // Tạo TextView hiển thị tên danh mục
        TextView categoryName = new TextView(this);
        categoryName.setText(danhMuc.ten);

        // Tạo CheckBox
        CheckBox checkBox = new CheckBox(this);
        checkBox.setTag(danhMuc.ten);  // Lưu tên danh mục vào tag
        checkBoxMap.put(danhMuc.ten, checkBox);

        // Tạo ImageView và hiển thị ảnh từ URL
        ImageView categoryImage = new ImageView(this);
        if (danhMuc.hinh != null) {
            categoryImage.setImageURI(Uri.parse(danhMuc.hinh));  // Sử dụng Uri cho hình ảnh
        } else {
            categoryImage.setImageResource(R.drawable.danh_muc_mac_dinh); // Ảnh mặc định nếu không có
        }

        categoryImagesMap.put(danhMuc.ten, categoryImage);
        categoryLayout.addView(categoryName);
        categoryLayout.addView(categoryImage);
        categoryLayout.addView(checkBox);

        gridDanhMuc.addView(categoryLayout);  // Thêm vào GridLayout
    }


    private void deleteDanhMuc(String categoryToDelete) {
        dbRef.child(categoryToDelete).removeValue(); // Xóa danh mục khỏi Firebase
    }
}
