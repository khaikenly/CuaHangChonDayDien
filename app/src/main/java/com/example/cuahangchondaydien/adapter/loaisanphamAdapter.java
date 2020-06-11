package com.example.cuahangchondaydien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangchondaydien.R;
import com.example.cuahangchondaydien.model.LoaiSanPham;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class loaisanphamAdapter extends BaseAdapter {
    ArrayList <LoaiSanPham> arraylistLoaiSanpham ;
    Context context;

    public loaisanphamAdapter(ArrayList<LoaiSanPham> arraylistLoaiSanpham, Context context) {
        this.arraylistLoaiSanpham = arraylistLoaiSanpham;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arraylistLoaiSanpham.size();
    }

    @Override
    public Object getItem(int i) {
        return arraylistLoaiSanpham.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHodler{
        TextView txtTenloaisp;
        ImageView imgLoaisp;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler viewHodler = null;
        if (view ==null){
            viewHodler = new ViewHodler();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_listview_loaisp,null);
            viewHodler.txtTenloaisp = view.findViewById(R.id.textviewloaisp);
            viewHodler.imgLoaisp = view.findViewById(R.id.imageviewloaisp);
            view.setTag(viewHodler);
        }else {
            viewHodler = (ViewHodler) view.getTag();
        }
        LoaiSanPham loaiSp = (LoaiSanPham) getItem(i);
        viewHodler.txtTenloaisp.setText(loaiSp.getTenloaisanpham());
        Picasso.get().load(loaiSp.getHinhloaisanpham()).into(viewHodler.imgLoaisp);
        return view;
    }
}
