package com.academiaexpresssystem.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.academiaexpresssystem.Activities.OrdersActivity;
import com.academiaexpresssystem.Adatpers.OrdersAdapter;
import com.academiaexpresssystem.Data.Order;
import com.academiaexpresssystem.R;

import java.util.ArrayList;

public class OrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    OrdersAdapter adapter;
    ArrayList<Order> collection;
    private SwipeRefreshLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orders_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        RecyclerView view = (RecyclerView) getView().findViewById(R.id.recyclerView);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrdersAdapter(getActivity());
        view.setAdapter(adapter);

        layout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh);
        layout.setOnRefreshListener(this);
    }

    public ArrayList<Order> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<Order> collection) {
        this.collection = collection;
        adapter.setCollection(collection);
        adapter.notifyDataSetChanged();

        if(collection.size() == 0) {
            getView().findViewById(R.id.noitems).setVisibility(View.VISIBLE);
        } else {
            getView().findViewById(R.id.noitems).setVisibility(View.GONE);
        }
    }

    public void end() {
        layout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        ((OrdersActivity) getActivity()).load();
    }
}