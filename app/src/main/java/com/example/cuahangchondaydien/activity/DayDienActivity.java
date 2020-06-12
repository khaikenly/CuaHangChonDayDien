package com.example.cuahangchondaydien.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cuahangchondaydien.R;
import com.example.cuahangchondaydien.adapter.daydienAdapter;
import com.example.cuahangchondaydien.model.SanPham;
import com.example.cuahangchondaydien.ultil.server;
import com.example.cuahangchondaydien.ultil.checkconnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DayDienActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvdd;
    daydienAdapter daydienAdapter;
    ArrayList<SanPham> mangdd;
    int iddd = 0;
    int page = 1;
    View footerview;
    boolean isLoading =false;
    mHandler mHandler;
    boolean limitdata = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daydien);
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

    private void LoadMoreData() {
        lvdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangdd.get(position));
                startActivity(intent);
            }
        });
        lvdd.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem+visibleItemCount == totalItemCount &&totalItemCount!=0 &&isLoading==false && limitdata==false){
                    isLoading=true;
                    ThreadData threadData = new ThreadData();
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
                String Tendaydien="";
                Integer Giadaydien= 0;
                String Hinhanhdaydien = "";
                String Mota = "";
                int Idsanphamdaydien=0;
                if (response != null && response.length() != 2){
                    lvdd.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tendaydien = jsonObject.getString("tensanpham");
                            Giadaydien = jsonObject.getInt("giasanpham");
                            Hinhanhdaydien = jsonObject.getString("hinhanhsanpham");
                            Mota = jsonObject.getString("mota");
                            Idsanphamdaydien = jsonObject.getInt("idsanpham");
                            mangdd.add(new SanPham(id,Tendaydien,Giadaydien,Hinhanhdaydien,Mota,Idsanphamdaydien));
                            daydienAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    limitdata = true;
                    lvdd.removeFooterView(footerview);
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
                param.put("idsanpham",String.valueOf(iddd));
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
        iddd = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",iddd + "");
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardaydien);
        lvdd = findViewById(R.id.listviewdaydien);
        mangdd = new ArrayList<>();
        daydienAdapter = new daydienAdapter(getApplicationContext(),mangdd);
        lvdd.setAdapter(daydienAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();
    }

    public class mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvdd.addFooterView(footerview);
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