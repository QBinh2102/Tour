package com.example.tourdulich.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tourdulich.Database.LuuThongTinUser;
import com.example.tourdulich.R;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<LuuThongTinUser> userList;
    private LayoutInflater inflater;

    public UserAdapter(Context context, List<LuuThongTinUser> userList) {
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.user_list,null);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtNameUser);
        TextView txtRole = (TextView) convertView.findViewById(R.id.txtRoleUser);

        LuuThongTinUser user = userList.get(position);
        txtName.setText(user.tenNguoiDung);
        txtRole.setText(user.role);

        return convertView;
    }

    //TimKiemUser
    public void searchUserList(List<LuuThongTinUser> searchList){
        userList = searchList;
        notifyDataSetChanged();
    }
}
