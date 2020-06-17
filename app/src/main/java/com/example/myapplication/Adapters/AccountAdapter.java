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
import androidx.recyclerview.widget.RecyclerView;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Transaction> transactions;

    public AccountAdapter(Context context, ArrayList<Transaction> transactions){
        mContext = context;
        this.transactions = transactions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        ImageView imageView;
        TextView tvName, tvBalance, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.item_transaction_layout);
            tvName = itemView.findViewById(R.id.item_transaction_name);
            tvBalance = itemView.findViewById(R.id.item_transaction_price);
            tvDate = itemView.findViewById(R.id.item_transaction_date);
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

        holder.tvName.setText(transactions.get(position).getName());
        holder.tvBalance.setText(transactions.get(position).getPrice());
        holder.tvDate.setText(transactions.get(position).getDate());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                //passing data
                mContext.startActivity(intent);
            }
        });

        Glide.with(mContext)
                .load(transactions.get(position).getId())
                .centerCrop()
                .placeholder(R.drawable.bunnocrop)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
