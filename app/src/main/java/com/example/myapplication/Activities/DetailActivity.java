package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {

    String name, price, date, category, method;
    TextView tvName, tvPrice, tvDate, tvMethod;
    ImageView image;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        date = intent.getStringExtra("date");
        id = intent.getIntExtra("id", 0);
        category = intent.getStringExtra("category");
        method = intent.getStringExtra("method");

        tvName = findViewById(R.id.detail_tv_name);
        tvPrice = findViewById(R.id.detail_tv_price);
        tvDate = findViewById(R.id.detail_tv_date);
        image = findViewById(R.id.detail_img_icon);
        tvMethod = findViewById(R.id.detail_tv_method);

        tvName.setText(name);
        tvPrice.setText(price);
        tvDate.setText(date);
        tvMethod.setText(method);

        Glide.with(DetailActivity.this)
                .load(id)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(image);

        switch(category) {
            case "Income":
            case "Paid Debt":
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
                break;
            case "Expense":
            case "Debt":
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
                break;
        }

    }
}
