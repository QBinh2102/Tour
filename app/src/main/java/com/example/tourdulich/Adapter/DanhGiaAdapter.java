package com.example.tourdulich.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tourdulich.CSDL.BaiDanhGia;
import com.example.tourdulich.R;

import java.util.ArrayList;

public class DanhGiaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BaiDanhGia> baiDanhGias;
    private LayoutInflater inflater;
    private int count;

    public DanhGiaAdapter(Context context, ArrayList<BaiDanhGia> baiDanhGias, int count) {
        this.context = context;
        this.baiDanhGias = baiDanhGias;
        this.inflater = LayoutInflater.from(context);
        this.count = count;
    }

    @Override
    public int getCount() {
        return this.count;
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
        convertView = inflater.inflate(R.layout.bai_danh_gia_items,null);


        return null;
    }
}
