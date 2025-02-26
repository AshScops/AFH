package com.example.finalwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.finalwork.R;
import com.example.finalwork.RecfindAdapter_Collect;
import com.example.finalwork.Utils.App;
import com.example.finalwork.bean.FindBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class myCollect extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView mRecyclerView;
    RecfindAdapter_Collect recfindAdapter;
    private MediaType MEDIA_TYPE_JSON;
    String userId;
    private Headers headers;
    App app = new App();
    private ArrayList<FindBean.DataBean.RecordsBean> recordslocalList = new ArrayList<>();
    List<FindBean.DataBean.RecordsBean> list;
    FindBean findBean;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            recfindAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        headers = new Headers.Builder()
                .add("appId", app.getAppId())
                .add("appSecret", app.getAppSecret())
                .add("Content-Type", "application/json")
                .build();
        GetUserId();
        browse();
        floatingActionButton = findViewById(R.id.floatingActionButton_collect);
        recfindAdapter = new RecfindAdapter_Collect(this, recordslocalList, userId);
        mRecyclerView = findViewById(R.id.recylerview_collect);
        mRecyclerView.setAdapter(recfindAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                browse();
            }
        });

    }

    private void browse() {
        Log.d("try", "zxczxcc");
        OkHttpClient client = new OkHttpClient();//鍒涘缓OkHttpClient瀵硅薄銆�
        Request request = new Request.Builder()//鍒涘缓Request 瀵硅薄銆�
                .url("http://47.107.52.7:88/member/photo/collect?userId=" + userId)
                .headers(headers)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("777", "21938120398120938");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//鍥炶皟鐨勬柟娉曟墽琛屽湪瀛愮嚎绋嬨€�
                    Gson gson2 = new Gson();
                    //Log.d("6666", "onResponse: " + response.body().string());
                    findBean = gson2.fromJson(response.body().string(), FindBean.class);
                    list = findBean.getData().getRecords();
                    recordslocalList.clear();
                    recordslocalList.addAll(list);
                    System.out.println("list=" + findBean.getData().getRecords().get(0).getCollectNum());
                    Message msg = new Message();
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            }
        });

    }

    private void GetUserId() {
        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String userIdKey = getResources()
                .getString(R.string.user_id);
        SharedPreferences spFile = this.getSharedPreferences(
                spFileName,
                MODE_PRIVATE);

        userId = spFile.getString(userIdKey, null);
        Log.d("6666", "onActivityCreated: " + userId);

    }

}