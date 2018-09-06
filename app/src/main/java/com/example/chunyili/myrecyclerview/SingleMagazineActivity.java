package com.example.chunyili.myrecyclerview;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;


public class SingleMagazineActivity extends AppCompatActivity {

    TextView single_magazine_periods;
    TextView single_magazine_title;
    SimpleDraweeView single_magazine_cover;
    String periods;
    String title;
    String imageUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.single_magazine_activity);
        findView();
        setView();
    }

    private void findView() {
        single_magazine_periods = findViewById(R.id.single_magazine_periods);
        single_magazine_title = findViewById(R.id.single_magazine_title);
        single_magazine_cover = findViewById(R.id.single_magazine_cover);
    }

    private void setView() {
        String periods = getIntent().getStringExtra("periods");
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        single_magazine_periods.setText(periods);
        single_magazine_title.setText(title);
        single_magazine_cover.setImageURI(imageUrl);
    }

}
