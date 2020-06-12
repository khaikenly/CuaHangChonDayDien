package com.example.cuahangchondaydien.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuahangchondaydien.R;
import com.example.cuahangchondaydien.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class capdienAdapter extends BaseAdapter {
    Context context;
    ArrayList<SanPham> arraycapdien;

    public capdienAdapter(Context context, ArrayList<SanPham> arraycapdien) {
        this.context = context;
        this.arraycapdien = arraycapdien;
    }

    @Override
    public int getCount() {
        return arraycapdien.size();
    }

    @Override
    public Object getItem(int i) {
        return arraycapdien.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView txttencapdien, txtgiacapdien, txtmotacapdien;
        public ImageView imgcapdien;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        capdienAdapter.ViewHolder viewHolder = null;
        if (view==null){
            viewHolder = new capdienAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_capdien,null);
            viewHolder.txttencapdien = view.findViewById(R.id.textviewcapdien);
            viewHolder.txtgiacapdien = view.findViewById(R.id.textviewgiacapdien);
            viewHolder.txtmotacapdien = view.findViewById(R.id.textviewmotacapdien);
            viewHolder.imgcapdien = view.findViewById(R.id.imageviewcapdien);
            view.setTag(viewHolder);
        }else {
            viewHolder = (capdienAdapter.ViewHolder) view.getTag();
        }
        SanPham sanPham = (SanPham) getItem(i);
        viewHolder.txttencapdien.setText(sanPham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiacapdien.setText("Giá:"+decimalFormat.format(sanPham.getGiasanpham())+"Đ");
        viewHolder.txtmotacapdien.setMaxLines(2);
        viewHolder.txtmotacapdien.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotacapdien.setText(sanPham.getMota());
        Picasso.get().load(sanPham.getHinhanhsanpham()).into(viewHolder.imgcapdien);
        return view;
    }
}
