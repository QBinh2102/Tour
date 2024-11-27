package com.example.tourdulich.trang;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Consumer;

import com.example.tourdulich.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrangChuAdmin extends AppCompatActivity {
    private TextView lbWelcome;
    private LinearLayout btnToiDatVe;
    private LinearLayout btnToiHoSo;
    private LinearLayout btnToiTinTuc;
    private LinearLayout btnToiGiaoDich;
    private Button btnDangXuat;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String hoTen;
    private ImageView imgVPopupMenu;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri; // Dùng để lưu trữ URI của ảnh được chọn
    private LinearLayout danhMucContainer;
    ArrayList<ImageView> danhMucList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_admin);

        loadDanhMuc();

        ImageView imgViewMayBay = findViewById(R.id.imgVMayBay);
        ImageView imgViewTauThuy = findViewById(R.id.imgVTauThuy);
        ImageView imgViewXe = findViewById(R.id.imgVXe);
        ImageView imgViewBien = findViewById(R.id.imgVBien);
        ImageView imgViewDao = findViewById(R.id.imgVDao);
        ImageView imgViewRung = findViewById(R.id.imgVRung);

        // Gọi hàm suaDanhMuc khi người dùng click vào bất kỳ ImageView nào
        imgViewMayBay.setOnClickListener(v -> suaDanhMuc(imgViewMayBay));
        imgViewTauThuy.setOnClickListener(v -> suaDanhMuc(imgViewTauThuy));
        imgViewXe.setOnClickListener(v -> suaDanhMuc(imgViewXe));
        imgViewBien.setOnClickListener(v -> suaDanhMuc(imgViewBien));
        imgViewDao.setOnClickListener(v -> suaDanhMuc(imgViewDao));
        imgViewRung.setOnClickListener(v -> suaDanhMuc(imgViewRung));
        // Khởi tạo ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData(); // Lấy URI của ảnh được chọn
                    }
                }
        );

        // Kiểm tra trạng thái đăng nhập khi mở trang
        if (firebaseUser == null) {
            Intent intent = new Intent(TrangChuAdmin.this, ThongTinChuaDangNhap.class);
            startActivity(intent);
            finish();
        }
        //Kiểm tra vai trò của user -> chuyển đúng giao diện của từng vai trò
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String role = preferences.getString("role", "user"); // Default là "user" nếu không tìm thấy role

        if (role.equals("admin")) {
            // Mở trang chủ Admin
            setContentView(R.layout.activity_trang_chu_admin);
        } else {
            // Mở trang chủ User
            setContentView(R.layout.activity_trang_chu);
        }
        // Hiển thị thông tin người dùng
        lbWelcome = findViewById(R.id.textViewWelcome);
        hoTen = firebaseUser.getDisplayName();
        lbWelcome.setText("Chào mừng " + hoTen);

        // Chuyển sang trang thông tin cá nhân
        Intent ttcn = new Intent(this, ThongTinCaNhan.class);
        btnToiHoSo = findViewById(R.id.btTrangChuToiHoSo);
        btnToiHoSo.setOnClickListener(v -> {
            startActivity(ttcn);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });

        // Chuyển sang trang Đặt Vé
        Intent datVe = new Intent(this, DatVe.class);
        btnToiDatVe = findViewById(R.id.btTrangChuToiDatVe);
        btnToiDatVe.setOnClickListener(view -> {
            startActivity(datVe);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });

        // Chuyển sang trang Tin Tức
        Intent tinTuc = new Intent(this, TinTuc.class);
        btnToiTinTuc = findViewById(R.id.btTrangChuToiTinTuc);
        btnToiTinTuc.setOnClickListener(view -> {
            startActivity(tinTuc);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });

        // Chuyển sang trang Giao Dịch
        Intent gd = new Intent(this, GiaoDich.class);
        btnToiGiaoDich = findViewById(R.id.btTrangChuToiGiaoDich);
        btnToiGiaoDich.setOnClickListener(view -> {
            startActivity(gd);
            overridePendingTransition(R.anim.slide_2_trai_qua_phai, R.anim.slide_1_trai_qua_phai);
        });


        // Xử lý khi bấm vào thanh menu
        imgVPopupMenu = findViewById(R.id.imgViewPopupMenu);
        imgVPopupMenu.setOnClickListener(view -> {
            // Tạo một dialog với layout dialog_menu.xml
            Dialog dialog = new Dialog(TrangChuAdmin.this);
            dialog.setContentView(R.layout.dialog_menu_danh_muc);
            dialog.setCancelable(true);  // Cho phép đóng dialog khi bấm ra ngoài

            // Đặt sự kiện cho các nút trong dialog
            Button btnCong = dialog.findViewById(R.id.menu_add);
            Button btnSua = dialog.findViewById(R.id.menu_edit);
            Button btnXoa = dialog.findViewById(R.id.menu_delete);

            // Thiết lập sự kiện cho các nút
            btnCong.setOnClickListener(v -> {
                // Xử lý thêm danh mục
                themDanhMuc();
                dialog.dismiss();
            });
            btnSua.setOnClickListener(v -> {
                // Xử lý sửa danh mục
                ImageView imageView = (ImageView) v; // Lấy ImageView từ view bấm vào (v)
                suaDanhMuc(imageView);
                dialog.dismiss();
            });
            btnXoa.setOnClickListener(v -> {
                // Xử lý xóa danh mục
                    xoaDanhMuc();
               // dialog.dismiss();
            });
            // Hiển thị dialog
            dialog.show();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadDanhMuc();
    }
    private void loadDanhMuc() {
        LinearLayout container = findViewById(R.id.layoutDanhMuc);
        SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
        String danhMucList = sharedPreferences.getString("danhMucList", "");

        // Nếu có danh mục đã lưu, chia chuỗi danh mục và thêm vào layout
        if (!danhMucList.isEmpty()) {
            String[] categories = danhMucList.split(",");
            for (String category : categories) {
                addDanhMucToLayout(category);
            }
        }
    }



    private List<View> selectedItems = new ArrayList<>();

    private void xoaDanhMuc() {
        // Tạo một dialog để chọn các danh mục cần xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục cần xóa");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Lưu trữ các item đã chọn
        Set<View> selectedItems = new HashSet<>();

        // Lấy tất cả danh mục từ layout
        LinearLayout danhMucLayout = findViewById(R.id.layoutDanhMuc);
        if (danhMucLayout == null) {
            Log.e("MainActivity", "layoutDanhMuc not found.");
            return;
        }

        int childCount = danhMucLayout.getChildCount();
        Log.d("MainActivity", "Number of children in layoutDanhMuc: " + childCount);

        // Thêm checkbox cho mỗi danh mục
        for (int i = 0; i < childCount; i++) {
            View danhMuc = danhMucLayout.getChildAt(i);
            if (danhMuc instanceof CardView) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(((CardView) danhMuc).getTag().toString());  // Đặt tên danh mục vào checkbox

                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedItems.add(danhMuc);  // Thêm danh mục vào danh sách đã chọn
                    } else {
                        selectedItems.remove(danhMuc);  // Xóa danh mục khỏi danh sách
                    }
                });

                layout.addView(checkBox);
            } else {
                Log.e("MainActivity", "Child is not an instance of CardView.");
            }
        }

        builder.setView(layout);

        // Thêm nút "Xóa" và "Hủy"
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            if (!selectedItems.isEmpty()) {
                // Hỏi người dùng có chắc chắn muốn xóa không
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
                confirmBuilder.setTitle("Xóa danh mục?");
                confirmBuilder.setMessage("Bạn có chắc chắn muốn xóa các danh mục đã chọn?");

                confirmBuilder.setPositiveButton("Xóa", (confirmDialog, confirmWhich) -> {
                    // Xóa các danh mục đã chọn
                    try {
                        for (View item : selectedItems) {
                            LinearLayout parentLayout = (LinearLayout) item.getParent();
                            if (parentLayout != null) {
                                Log.d("MainActivity", "Removing view: " + item.toString());
                                parentLayout.removeView(item);  // Xóa khỏi layout
                            } else {
                                Log.e("MainActivity", "Parent layout is null.");
                            }
                        }
                        selectedItems.clear();  // Xóa danh sách đã chọn
                        Toast.makeText(this, "Danh mục đã được xóa", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error removing views: " + e.getMessage(), e);
                    }
                    confirmDialog.dismiss(); // Đóng dialog xác nhận xóa
                });

                confirmBuilder.setNegativeButton("Hủy", (confirmDialog, confirmWhich) -> {
                    confirmDialog.dismiss(); // Đóng dialog xác nhận xóa nếu hủy
                });
                confirmBuilder.show();
            } else {
                Toast.makeText(this, "Vui lòng chọn danh mục để xóa", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss(); // Đóng dialog nếu hủy
        });
        builder.show();
    }



    private static final int PICK_IMAGE_REQUEST = 1;  // Mã yêu cầu để chọn ảnh

    private void suaDanhMuc(final ImageView danhMuc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa danh mục");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText editText = new EditText(this);
        editText.setText(danhMuc.getContentDescription());
        layout.addView(editText);

        Button btnChonIcon = new Button(this);
        btnChonIcon.setText("Đổi biểu tượng");
        layout.addView(btnChonIcon);

        final Uri[] selectedIconUri = {null};
        btnChonIcon.setOnClickListener(v -> chonHinhAnh(uri -> selectedIconUri[0] = uri));

        builder.setView(layout);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newCategoryName = editText.getText().toString().trim();
            if (!newCategoryName.isEmpty()) {
                danhMuc.setContentDescription(newCategoryName);
                if (selectedIconUri[0] != null) {
                    danhMuc.setImageURI(selectedIconUri[0]);
                }
            } else {
                Toast.makeText(this, "Tên danh mục không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // Hàm chọn ảnh
    private void chonHinhAnh(Consumer<Uri> callback) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
        // Kết quả xử lý
        imagePickerCallback = callback;
    }

    // Callback ảnh
    private Consumer<Uri> imagePickerCallback;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && imagePickerCallback != null) {
            Uri selectedImageUri = data.getData();
            imagePickerCallback.accept(selectedImageUri);
        }
    }


    private void themDanhMuc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm danh mục mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        EditText editText = new EditText(this);
        editText.setHint("Nhập tên danh mục");
        layout.addView(editText);

        builder.setView(layout);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String newCategory = editText.getText().toString().trim();
            if (!newCategory.isEmpty()) {
                SharedPreferences sharedPreferences = getSharedPreferences("DanhMucPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String danhMucList = sharedPreferences.getString("danhMucList", "");
                danhMucList = danhMucList + (danhMucList.isEmpty() ? "" : ",") + newCategory;

                editor.putString("danhMucList", danhMucList);
                editor.apply();

                addDanhMucToLayout(newCategory);
            } else {
                Toast.makeText(this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void addDanhMucToLayout(String danhMuc) {
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
        cardParams.setMargins(10, 5, 10, 5); // Tạo không gian giữa các CardView
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(0);
        cardView.setRadius(10f);
        cardView.setPadding(0, 0, 0, 0);
        cardView.setCardBackgroundColor(Color.WHITE);

        // Sử dụng FrameLayout để dễ dàng căn giữa
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
        imageParams.gravity = Gravity.CENTER; // Căn giữa icon
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(R.drawable.danh_muc_mac_dinh);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        frameLayout.addView(imageView);
        cardView.addView(frameLayout);

        currentRow.addView(cardView);
    }

    @Override
        protected void onStart () {
        super.onStart();
        // Kiểm tra lại nếu người dùng chưa đăng nhập, điều hướng về trang đăng nhập
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Intent intent = new Intent(TrangChuAdmin.this, ThongTinChuaDangNhap.class);
            startActivity(intent);
            finish();
        }
    }
    }
