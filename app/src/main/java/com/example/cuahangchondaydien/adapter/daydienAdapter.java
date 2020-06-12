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

public class daydienAdapter extends BaseAdapter {
    Context context;
    ArrayList<SanPham> arraydaydien;

    public daydienAdapter(Context context, ArrayList<SanPham> arraydaydien) {
        this.context = context;
        this.arraydaydien = arraydaydien;
    }

    @Override
    public int getCount() {
        return arraydaydien.size();
    }

    @Override
    public Object getItem(int i) {
        return arraydaydien.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView txttendaydien, txtgiadaydien, txtmotadaydien;
        public ImageView imgdaydien;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_daydien,null);
            viewHolder.txttendaydien = view.findViewById(R.id.textviewdaydien);
            viewHolder.txtgiadaydien = view.findViewById(R.id.textviewgiadaydien);
            viewHolder.txtmotadaydien = view.findViewById(R.id.textviewmotadaydien);
            viewHolder.imgdaydien = view.findViewById(R.id.imageviewdaydien);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SanPham sanPham = (SanPham) getItem(i);
        viewHolder.txttendaydien.setText(sanPham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiadaydien.setText("Giá:"+decimalFormat.format(sanPham.getGiasanpham())+"Đ");
        viewHolder.txtmotadaydien.setMaxLines(2);
        viewHolder.txtmotadaydien.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotadaydien.setText(sanPham.getMota());
        Picasso.get().load(sanPham.getHinhanhsanpham()).into(viewHolder.imgdaydien);
        return view;
    }
}
