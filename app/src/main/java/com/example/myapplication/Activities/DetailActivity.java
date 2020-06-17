package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {

    String name, price, date, category, method, desc;
    TextView tvName, tvPrice, tvDate, tvMethod, tvDesc;
    ImageView image;
    RelativeLayout layout;
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
        desc = intent.getStringExtra("desc");


        tvName = findViewById(R.id.detail_tv_name);
        tvPrice = findViewById(R.id.detail_tv_price);
        tvDate = findViewById(R.id.detail_tv_date);
        image = findViewById(R.id.detail_img_icon);
        tvMethod = findViewById(R.id.detail_tv_method);
        layout = findViewById(R.id.detail_layout);
        tvDesc = findViewById(R.id.detail_desc);

        tvName.setText(name);
        tvPrice.setText(price);
        tvDate.setText(date);
        tvMethod.setText(method);
        tvDesc.setText(desc);

        Glide.with(DetailActivity.this)
                .load(id)
                .placeholder(R.drawable.logo)
                .centerCrop()
                .into(image);

        if (desc == null || desc.isEmpty()) tvDesc.setVisibility(View.GONE);

        switch(category) {
            case "Income":
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
                layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorIncome));
                break;
            case "Paid Debt":
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
                layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPay));
                break;
            case "Expense":
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
                layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorExpense));
                break;
            case "Debt":
                tvPrice.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
                layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDebt));
                break;
        }

    }
}
