package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.DetailActivity;
import com.example.myapplication.Objects.Transaction;
import com.example.myapplication.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Transaction> transactions;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions){
        mContext = context;
        this.transactions = transactions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        ImageView imageView;
        TextView tvName, tvDate, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.item_transaction_layout);
            tvName = itemView.findViewById(R.id.item_transaction_name);
            tvDate = itemView.findViewById(R.id.item_transaction_date);
            tvPrice = itemView.findViewById(R.id.item_transaction_price);
            imageView = itemView.findViewById(R.id.item_transaction_img);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String name = transactions.get(position).getName();
        final String price = transactions.get(position).getPrice();
        final String date = transactions.get(position).getDate();
        final int id = transactions.get(position).getId();
        final String category = transactions.get(position).getCategory();
        holder.tvName.setText(name);
        holder.tvPrice.setText(price);
        holder.tvDate.setText(date);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("date", date);
                intent.putExtra("price", price);
                intent.putExtra("url", id);
                mContext.startActivity(intent);
            }
        });

        Glide.with(mContext)
                .load(transactions.get(position).getId())
                .centerCrop()
                .placeholder(R.drawable.bunnocrop)
                .into(holder.imageView);

        switch(category) {
            case "Income":
            case "Paid Debt":
                holder.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                break;
            case "Expense":
            case "Debt":
                holder.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
