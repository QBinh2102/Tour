package com.example.tourdulich.Page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
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

import com.example.tourdulich.Adapter.UserAdapter;
import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuanLyUser extends AppCompatActivity {

    private Button btnQuaiLai;
    private Button btnThem;
    private ListView lvUser;
    private ArrayList<LuuThongTinUser> arrayUser;
    private UserAdapter userAdapter;

    private ProgressBar progressBar;
    boolean gt = true;
    boolean isPassShow = false;
    String vaiTro="user";
    private SearchView TimKiemUser;

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnQuaiLai = findViewById(R.id.btnQuayLaiTuQuanLyUser);
        btnThem = findViewById(R.id.btnThemUser);
        lvUser = findViewById(R.id.listUser);
        arrayUser = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);

        TimKiemUser = findViewById(R.id.txtTimKiemUser);
        TimKiemUser.clearFocus();
        TimKiemUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        showDanhSach();

        btnQuaiLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tcAdmin = new Intent(QuanLyUser.this, TrangChuAdmin.class);
                startActivity(tcAdmin);
            }
        });

        //Chuyển sang thông tin đặt vé
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LuuThongTinUser user = arrayUser.get(position);
                Intent intent = new Intent(QuanLyUser.this, CapNhatUser.class);
                intent.putExtra("user_item", user);
                startActivity(intent);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyUser.this);
                builder.setTitle("Thêm người dùng!");
                View view = getLayoutInflater().inflate(R.layout.add_user, null);
                builder.setView(view);
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();

                EditText userName = view.findViewById(R.id.edtUserName);
                EditText edtUserPass = view.findViewById(R.id.edtUserPass);
                EditText edtUserAddress = view.findViewById(R.id.edtUserAddress);
                EditText edtUserPhone = view.findViewById(R.id.edtUserPhone);
                EditText edtUserMail = view.findViewById(R.id.edtUserEmail);
                TextView edtUserBirth = view.findViewById(R.id.edtUserBirth);
                Spinner role = view.findViewById(R.id.spinnerRole);
                RadioButton rbtNam = view.findViewById(R.id.radioNam);
                RadioButton rbtNu = view.findViewById(R.id.radioNu);
                ImageButton iBtAnHienMK = view.findViewById(R.id.imgBtAnHienPassUser);
                Button btnQL = view.findViewById(R.id.btnQuayLaiQLUser);
                Button btnDangKy = view.findViewById(R.id.btnDangKyUser);
                ProgressBar thanhTienTrinh = view.findViewById(R.id.progressBar5);

                ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(QuanLyUser.this,
                        R.array.role, android.R.layout.simple_spinner_item);
                roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                role.setAdapter(roleAdapter);

                edtUserBirth.setPaintFlags(edtUserBirth.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                edtUserBirth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);

                        DatePickerDialog picker = new DatePickerDialog(QuanLyUser.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtUserBirth.setText(dayOfMonth + "/" + (month+1) +  "/" + year);
                            }
                        },year, month, day);
                        picker.show();

                    }
                });

                iBtAnHienMK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isPassShow){
                            //Show password
                            edtUserPass.setInputType(InputType.TYPE_CLASS_TEXT);
                            iBtAnHienMK.setImageResource(R.drawable.eye_24);
                        }else{
                            //Hide password
                            edtUserPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            iBtAnHienMK.setImageResource(R.drawable.visibility_off_24);
                        }
                        isPassShow = !isPassShow;
                        edtUserPass.setSelection(edtUserPass.length());
                    }
                });

                rbtNam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gt=true;
                        rbtNam.setChecked(true);
                        rbtNu.setChecked(false);
                    }
                });

                rbtNu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gt=false;
                        rbtNam.setChecked(false);
                        rbtNu.setChecked(true);
                    }
                });

                btnQL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

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

                btnDangKy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tenDangNhap = userName.getText().toString();
                        String matKhau = edtUserPass.getText().toString();
                        String diaChi = edtUserAddress.getText().toString();
                        String soDienThoai = edtUserPhone.getText().toString();
                        String email = edtUserMail.getText().toString();
                        String ngaySinh = edtUserBirth.getText().toString();
                        String gioiTinh;



                        //Kiem tra so dien thoai co hop li khong
                        //So dien thoai chi hop ly khi bat dau voi 3 so ben duoi va 7 so con lai tu 0->9
                        String quyDinhSDT = "(039|038|037|036|035|034|033|032|096|097|098|086|083|084|085|081" +
                                "|088|082|070|079|077|076|078|090|093|089|052|056|058|092)[0-9]{7}";
                        Matcher mauKiemTraSDT;
                        Pattern sdtHopLe = Pattern.compile(quyDinhSDT);
                        mauKiemTraSDT = sdtHopLe.matcher(soDienThoai);

                        //Kiểm lỗi người dùng
                        if(TextUtils.isEmpty(tenDangNhap)){
                            Toast.makeText(QuanLyUser.this, "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_LONG).show();
                            userName.setError("Vui lòng điền tên đăng nhập");
                            userName.requestFocus();
                        } else if (TextUtils.isEmpty(matKhau)|| matKhau.length() < 6) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_LONG).show();
                            edtUserPass.setError("Mật khẩu phải từ 6 ký tự trở lên");
                            edtUserPass.requestFocus();
                        } else if (TextUtils.isEmpty(diaChi)) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_LONG).show();
                            edtUserAddress.setError("Vui lòng điền địa chỉ");
                            edtUserAddress.requestFocus();
                        } else if (TextUtils.isEmpty(soDienThoai)) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_LONG).show();
                            edtUserPhone.setError("Vui lòng điền số điện thoại");
                            edtUserPhone.requestFocus();
                        } else if (!mauKiemTraSDT.find()) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng xem lại số điện thoại",
                                    Toast.LENGTH_LONG).show();
                            edtUserPhone.setError("Số điện thoại không hợp lệ");
                            edtUserPhone.requestFocus();
                        } else if (soDienThoai.length() != 10) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng xem lại số điện thoại",
                                    Toast.LENGTH_LONG).show();
                            edtUserPhone.setError("Số điện thoại bao gồm 10 số");
                            edtUserPhone.requestFocus();
                        } else if (TextUtils.isEmpty(email)) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_LONG).show();
                            edtUserMail.setError("Vui lòng điền email");
                            edtUserMail.requestFocus();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng nhập lại email",
                                    Toast.LENGTH_LONG).show();
                            edtUserMail.setError("Vui lòng điền email hợp lệ");
                            edtUserMail.requestFocus();
                        } else if (TextUtils.isEmpty(ngaySinh)) {
                            Toast.makeText(QuanLyUser.this, "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_LONG).show();
                            edtUserBirth.setError("Vui lòng nhập ngày sinh");
                            edtUserBirth.requestFocus();
                        } else{
                            if(gt) {
                                gioiTinh = "Nam";
                            }else{
                                gioiTinh = "Nữ";
                            }
                            thanhTienTrinh.setVisibility(View.VISIBLE);
                            nguoiDangKy(tenDangNhap, matKhau, diaChi, soDienThoai, email, ngaySinh, vaiTro, gioiTinh, edtUserMail, thanhTienTrinh);
                            dialog.dismiss();
                        }
                    }
                });
            }

            private void nguoiDangKy(String tenDangNhap, String matKhau, String diaChi, String soDienThoai, String email, String ngaySinh, String role, String gioiTinh, EditText edtUserMail, ProgressBar thanhTienTrinh) {
                FirebaseAuth xacThucFirebase = FirebaseAuth.getInstance();
                xacThucFirebase.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(
                        QuanLyUser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser nguoiDungFB = xacThucFirebase.getCurrentUser();

                                    //Thay doi thong tin ho so nguoi dung
                                    UserProfileChangeRequest thayDoiTTUser = new UserProfileChangeRequest.Builder().setDisplayName(tenDangNhap).build();
                                    nguoiDungFB.updateProfile(thayDoiTTUser);

                                    //Quan ly du lieu nguoi dung
                                    String id = nguoiDungFB.getUid();
                                    //Hình mặc định của user
                                    String hinh = "https://firebasestorage.googleapis.com/v0/b/tourdulich-ae976.firebasestorage.app/o/imagesUser%2F1732333793835.jpg?alt=media&token=054bb7bf-9135-415c-bc16-abaceff9d8ff";
                                    LuuThongTinUser thongTinUser = new LuuThongTinUser(id,tenDangNhap,hinh,diaChi, soDienThoai, email, ngaySinh, gioiTinh, role);
                                    DatabaseReference refDuLieu = FirebaseDatabase.getInstance().getReference("Người đã đăng ký");
                                    refDuLieu.child(nguoiDungFB.getUid()).setValue(thongTinUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                nguoiDungFB.sendEmailVerification();
                                                userRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            arrayUser.clear(); // Xóa danh sách cũ trước khi tải mới

                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                // Lấy từng tour từ Firebase
                                                                LuuThongTinUser user = snapshot.getValue(LuuThongTinUser.class);
                                                                if (user != null) {
                                                                    arrayUser.add(user); // Thêm vào danh sách nếu hợp lệ
                                                                }
                                                            }

                                                            // Kiểm tra danh sách sau khi tải
                                                            if (!arrayUser.isEmpty()) {
                                                                // Gắn adapter và hiển thị dữ liệu
                                                                if (userAdapter == null) {
                                                                    userAdapter = new UserAdapter(QuanLyUser.this, arrayUser);
                                                                    lvUser.setAdapter(userAdapter);
                                                                } else {
                                                                    userAdapter.notifyDataSetChanged();
                                                                }
                                                            } else {
                                                                Toast.makeText(QuanLyUser.this, "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(QuanLyUser.this, "Không có user nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                                                        }

                                                        // Ẩn progress bar sau khi tải xong
                                                        thanhTienTrinh.setVisibility(View.INVISIBLE);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        thanhTienTrinh.setVisibility(View.INVISIBLE); // Ẩn progress bar nếu xảy ra lỗi
                                                        Toast.makeText(QuanLyUser.this, "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                Toast.makeText(QuanLyUser.this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(QuanLyUser.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        edtUserMail.setError("Email không hợp lệ vui lòng thử lại");
                                        edtUserMail.requestFocus();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        edtUserMail.setError("Email này đã được sử dụng vui lòng thử lại");
                                        edtUserMail.requestFocus();
                                    } catch (Exception e) {
                                        Toast.makeText( QuanLyUser.this,"Đã xảy ra lỗi: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
//                                    thanhTienTrinh.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }

    private void showDanhSach(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayUser.clear(); // Xóa danh sách cũ trước khi tải mới

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy từng tour từ Firebase
                        LuuThongTinUser user = snapshot.getValue(LuuThongTinUser.class);
                        if (user != null) {
                            arrayUser.add(user); // Thêm vào danh sách nếu hợp lệ
                        }
                    }

                    // Kiểm tra danh sách sau khi tải
                    if (!arrayUser.isEmpty()) {
                        // Gắn adapter và hiển thị dữ liệu
                        if (userAdapter == null) {
                            userAdapter = new UserAdapter(QuanLyUser.this, arrayUser);
                            lvUser.setAdapter(userAdapter);
                        } else {
                            userAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(QuanLyUser.this, "Không có dữ liệu để hiển thị", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuanLyUser.this, "Không có user nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }

                // Ẩn progress bar sau khi tải xong
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE); // Ẩn progress bar nếu xảy ra lỗi
                Toast.makeText(QuanLyUser.this, "Lỗi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String removeAccent(String s) {
        if (s == null) return ""; // Trả về chuỗi rỗng nếu input null
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", ""); // Loại bỏ các ký tự dấu
    }

    // Lọc danh sách dựa trên từ khóa tìm kiếm
    private void filterUsers(String query) {
        List<LuuThongTinUser> filteredList = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            query = ""; // Gán giá trị rỗng nếu null hoặc trống
        }
        String queryNormalized = removeAccent(query.toLowerCase());

        for (LuuThongTinUser user : arrayUser) {
            // Chuyển đổi tên người dùng thành không dấu
            String userNameNormalized = removeAccent(user.tenNguoiDung.toLowerCase());

            // So sánh tên người dùng với query
            if (userNameNormalized.contains(queryNormalized)) {
                filteredList.add(user);
            }
        }

        // Cập nhật danh sách hiển thị
        userAdapter.searchUserList(filteredList);
        lvUser.setAdapter(userAdapter);
    }
}