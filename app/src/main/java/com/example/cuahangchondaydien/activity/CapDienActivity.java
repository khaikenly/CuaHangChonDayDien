package com.example.cuahangchondaydien.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangchondaydien.R;
import com.example.cuahangchondaydien.adapter.capdienAdapter;
import com.example.cuahangchondaydien.adapter.daydienAdapter;
import com.example.cuahangchondaydien.model.SanPham;
import com.example.cuahangchondaydien.ultil.checkconnection;
import com.example.cuahangchondaydien.ultil.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CapDienActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvcd;
    capdienAdapter capdienAdapter;
    ArrayList<SanPham> mangcd;
    int idcd = 0;
    int page = 1;
    View footerview;
    boolean isLoading =false;
    mHandler mHandler;
    boolean limitdata = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_dien);
        Anhxa();
        if (checkconnection.haveNetworkConnection(getApplicationContext())){
            GetIdLoaiSp();
            ActionToolBar();
            GetData(page);
            LoadMoreData();
        }
        else {
            checkconnection.ShowToast_Short(getApplicationContext(),"Hãy kiểm tra kết nối Internet");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.cuahangchondaydien.activity.GioHang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreData() {
        lvcd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangcd.get(position));
                startActivity(intent);
            }
        });
        lvcd.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem+visibleItemCount == totalItemCount &&totalItemCount!=0 &&isLoading==false && limitdata==false){
                    isLoading=true;
                    CapDienActivity.ThreadData threadData = new CapDienActivity.ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = server.DuongDanDayDien + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String Tencapdien="";
                Integer Giacapdien= 0;
                String Hinhanhcapdien = "";
                String Mota = "";
                int Idsanphamcapdien=0;
                if (response != null && response.length() != 2){
                    lvcd.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tencapdien = jsonObject.getString("tensanpham");
                            Giacapdien = jsonObject.getInt("giasanpham");
                            Hinhanhcapdien = jsonObject.getString("hinhanhsanpham");
                            Mota = jsonObject.getString("mota");
                            Idsanphamcapdien = jsonObject.getInt("idsanpham");
                            mangcd.add(new SanPham(id,Tencapdien,Giacapdien,Hinhanhcapdien,Mota,Idsanphamcapdien));
                            capdienAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    limitdata = true;
                    lvcd.removeFooterView(footerview);
                    checkconnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("idsanpham",String.valueOf(idcd));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetIdLoaiSp() {
        idcd = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",idcd + "");
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarcapdien);
        lvcd = findViewById(R.id.listviewcapdien);
        mangcd = new ArrayList<>();
        capdienAdapter = new capdienAdapter(getApplicationContext(),mangcd);
        lvcd.setAdapter(capdienAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();
    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvcd.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}