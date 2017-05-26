package com.academiaexpresssystem.Adatpers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.academiaexpresssystem.Activities.OrderActivity;
import com.academiaexpresssystem.Data.Order;
import com.academiaexpresssystem.R;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    ArrayList<Order> collection;

    boolean inc = false;

    public boolean isInc() {
        return inc;
    }

    public void setInc(boolean inc) {
        this.inc = inc;
    }

    public OrdersAdapter(Activity context) {
        this.context = context;
        collection = new ArrayList<>();
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void setCollection(ArrayList<Order> collection) {
        this.collection = collection;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.order_card, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final NewsViewHolder holder = (NewsViewHolder) viewHolder;

        final Order order = collection.get(position);

        holder.location.setText(order.getLocation());
        holder.time.setText(order.getTime());
        holder.name.setText(order.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderActivity.class);
                OrderActivity.Companion.setOrder(order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView name;
        private TextView location;

        public NewsViewHolder(final View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.info_text);
            name = (TextView) itemView.findViewById(R.id.address);
            location = (TextView) itemView.findViewById(R.id.gender);
        }
    }
}