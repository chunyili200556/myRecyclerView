package com.example.chunyili.myrecyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chunyili.myrecyclerview.Model.Magazine;
import com.example.chunyili.myrecyclerview.Response.ItemsObjectData;
import com.example.chunyili.myrecyclerview.Response.MagazineInfoData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextView selectYearBtn;
    RecyclerView recyclerView;
    List<Magazine> magazineList;
    List<Magazine> selectYearMagazineList;
    RelativeLayout relativeLayout;
    TextView yearModeCancelBtn;
    TextView publishYear;
    MagazineAdapter magazineAdapter;

    private static final String TAG = "MainActivity";
    private boolean isYearMode;
    private boolean isLoading;
    private int loadPage = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        findView();
        magazineList = new ArrayList<Magazine>();
        requestMagazineList(0,null);
    }

    private void findView() {
        recyclerView = findViewById(R.id.recyclerView);
        selectYearBtn = findViewById(R.id.year_button);
        recyclerView = findViewById(R.id.recyclerView);
        relativeLayout = findViewById(R.id.year_selected_mode_view);
        yearModeCancelBtn = findViewById(R.id.year_mode_cancel_button);
        publishYear = findViewById(R.id.magazine_publish_year);

        // Set Button ClickListener
        yearModeCancelBtnClick();
        yearBtnClick();
    }

    private void yearModeCancelBtnClick() {
        yearModeCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchYearSelectedMode(false);
                magazineList.clear();
                magazineAdapter.notifyDataSetChanged();
                requestMagazineList(1,null);
            }
        });
    }

    private void yearBtnClick() {
        selectYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(year);
        numberPicker.setMinValue(2014);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(numberPicker);
        builder.setPositiveButton("確認年份", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"這次選擇得年份為："+numberPicker.getValue());

                String yearStr = String.valueOf(numberPicker.getValue());
                publishYear.setText(yearStr);
                switchYearSelectedMode(true);
                requestMagazineList(0,yearStr);
            }
        });
        builder.show();
    }

    private void switchYearSelectedMode(boolean isYearSelectMode) {
        isYearMode = isYearSelectMode;
        isLoading = false;
        if(isYearSelectMode){
            relativeLayout.setVisibility(View.VISIBLE);
        }else {
            relativeLayout.setVisibility(View.GONE);
        }
    }

    private void requestMagazineList(int request,String year){
        String url = "https://api-app.cw.com.tw/cw-app/magazine/list/";

        if(year!=null){
            url += "1?year="+year;
        }else{
            url += request;
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,successListener,errorListener);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private Response.Listener successListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {

            handleResponseObject(response);

            if(isYearMode && !isLoading){
                setUpRecyclerViewWithYearMode();
            }else if(!isYearMode && !isLoading){
                setUpOriginRecycleView();
            }
            magazineAdapter.setOnItemClickListener(itemClickListener);
        }
    };


    private void handleResponseObject(Object response){
        MagazineInfoData magazineInfoData = new Gson().fromJson(response.toString(),MagazineInfoData.class);
        List<ItemsObjectData> items = magazineInfoData.getItems();
        ItemsObjectData item = items.get(0);

        List<Magazine> magazines = item.getObjects();

        if(isYearMode){
            selectYearMagazineList = magazines;
        }else{

            for(Magazine magazine:magazines){
                magazineList.add(magazine);
            }

            if(isLoading){
                magazineAdapter.notifyDataSetChanged();
                magazineAdapter.setLoaded();
            }

        }
    }



    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };


    private void setUpOriginRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        magazineAdapter = new MagazineAdapter(recyclerView,magazineList,this,false);
        recyclerView.setAdapter(magazineAdapter);
        setLoadMore();

    }

    private void setUpRecyclerViewWithYearMode(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);
        magazineAdapter = new MagazineAdapter(recyclerView,selectYearMagazineList,this,true);
        recyclerView.setAdapter(magazineAdapter);
    }

    private void setLoadMore(){
        magazineAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if(isYearMode){
                    return;
                }
                magazineAdapter.notifyItemInserted(magazineList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = true;
                        requestMagazineList(loadPage,null);
                        loadPage ++;
                    }
                },1000);
            }
        });
    }

    private MagazineAdapter.OnItemClickListener itemClickListener = new MagazineAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            Magazine magazine = isYearMode ? selectYearMagazineList.get(position):magazineList.get(position);

            Intent intent = new Intent(getApplicationContext(),SingleMagazineActivity.class);
            intent.putExtra("periods",magazine.getPeriods());
            intent.putExtra("title",magazine.getTitle());
            intent.putExtra("imageUrl",magazine.getImage_url());
            startActivity(intent);
        }
    };

}
