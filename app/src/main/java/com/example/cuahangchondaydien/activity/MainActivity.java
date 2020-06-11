package com.example.cuahangchondaydien.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangchondaydien.R;
import com.example.cuahangchondaydien.adapter.SanPhamAdapter;
import com.example.cuahangchondaydien.adapter.loaisanphamAdapter;
import com.example.cuahangchondaydien.model.LoaiSanPham;
import com.example.cuahangchondaydien.model.SanPham;
import com.example.cuahangchondaydien.ultil.checkconnection;
import com.example.cuahangchondaydien.ultil.server;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerview;
    NavigationView navigationView;
    ListView listviewmanhinhchinh;
    DrawerLayout drawerLayout;
    ArrayList<LoaiSanPham> mangloaisp;
    loaisanphamAdapter loaisanphamAdapter;
    int id = 0;
    String tenloaisanpham = "";
    String hinhloaisanpham = "";
    ArrayList<SanPham> mangsanpham;
    SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        if (checkconnection.haveNetworkConnection(getApplicationContext())){
            ActionBar();
            ActionViewFlipper();
            GetDuLieuLoaiSp();
            GetDuLieuSpMoiNhat();
            CatchOnItemListView();
        }else {
            checkconnection.ShowToast_Short(getApplicationContext(),"Kiem tra ket noi internet");
            finish();
        }
    }

    private void CatchOnItemListView() {
        listviewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                switch (i){
                    case 0:
                        if (checkconnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            checkconnection.ShowToast_Short(getApplicationContext(),"Kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (checkconnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,DayDienActivity.class);
                            intent.putExtra("idloaisanpham",mangloaisp.get(i).getId());
                            startActivity(intent);
                        }else{
                            checkconnection.ShowToast_Short(getApplicationContext(),"Kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (checkconnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,CapDienActivity.class);
                            intent.putExtra("idloaisanpham",mangloaisp.get(i).getId());
                            startActivity(intent);
                        }else{
                            checkconnection.ShowToast_Short(getApplicationContext(),"Kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (checkconnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,LienHeActivity.class);
                            startActivity(intent);
                        }else{
                            checkconnection.ShowToast_Short(getApplicationContext(),"Kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if (checkconnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,ThongTinActivity.class);
                            startActivity(intent);
                        }else{
                            checkconnection.ShowToast_Short(getApplicationContext(),"Kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    private void GetDuLieuSpMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.DuongDanSpMoiNhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response!=null){
                    int ID = 0;
                    String Tensanpham="";
                    Integer Giasanpham = 0;
                    String Hinhanhsanpham = "";
                    String Mota = "";
                    int Idsanpham=0;
                    for (int i=0;i<response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensanpham = jsonObject.getString("tensanpham");
                            Giasanpham = jsonObject.getInt("giasanpham");
                            Hinhanhsanpham = jsonObject.getString("hinhanhsanpham");
                            Mota = jsonObject.getString("mota");
                            Idsanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new SanPham(ID,Tensanpham,Giasanpham,Hinhanhsanpham, Mota,Idsanpham));
                            sanPhamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDuLieuLoaiSp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.DuongDanLoaiSp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    for (int i=0;i<response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisanpham = jsonObject.getString("tenloaisanpham");
                            hinhloaisanpham = jsonObject.getString("hinhloaisanpham");
                            mangloaisp.add(new LoaiSanPham(id,tenloaisanpham,hinhloaisanpham));
                            loaisanphamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangloaisp.add(3,new LoaiSanPham(0,"Liên Hệ","https://cdn1.iconfinder.com/data/icons/e-commerce-color-line/68/Ecommerce_color-44-128.png"));
                    mangloaisp.add(4,new LoaiSanPham(0,"Thông Tin","https://cdn1.iconfinder.com/data/icons/flat-users-1/32/user-information-128.png"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkconnection.ShowToast_Short(getApplicationContext(),error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://kythuatchetao.com/wp-content/uploads/2015/01/day-dong.jpg");
        mangquangcao.add("https://i.ytimg.com/vi/VoUVLrxIuY4/maxresdefault.jpg");
        mangquangcao.add("https://maybompanasonic.net/kcfinder/upload//images/photo1529374652587-15293746525871459256669.jpg");
        mangquangcao.add("https://file1.hutech.edu.vn/file/editor/homepage1/Phoi%20canh%20co%20so%20Hutech_co%20so%20DBP%20%20.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.silde_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerview = findViewById(R.id.recyclerview);
        navigationView = findViewById(R.id.navgationview);
        listviewmanhinhchinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0,new LoaiSanPham(0,"Trang Chính","https://cdn2.iconfinder.com/data/icons/jetflat-buildings/90/008_001_home_apartment_house_building-128.png"));
        loaisanphamAdapter = new loaisanphamAdapter(mangloaisp,getApplicationContext());
        listviewmanhinhchinh.setAdapter(loaisanphamAdapter);
        mangsanpham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(),mangsanpham);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerview.setAdapter(sanPhamAdapter);
    }
}