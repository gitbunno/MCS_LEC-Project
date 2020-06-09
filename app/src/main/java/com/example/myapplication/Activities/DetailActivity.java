package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {

    String name, price, date, url;
    TextView tvName, tvPrice, tvDate;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        date = intent.getStringExtra("date");
        url = intent.getStringExtra("url");

        tvName = findViewById(R.id.detail_tv_name);
        tvPrice = findViewById(R.id.detail_tv_price);
        tvDate = findViewById(R.id.detail_tv_date);
        image = findViewById(R.id.detail_img_icon);

        tvName.setText(name);
        tvPrice.setText(price);
        tvDate.setText(date);

        Glide.with(DetailActivity.this)
                .load(url)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(image);

    }
}
