package com.academiaexpresssystem.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.academiaexpresssystem.Data.Order;
import com.academiaexpresssystem.Fragments.OrdersFragment;
import com.academiaexpresssystem.R;
import com.academiaexpresssystem.Server.DeviceInfoStore;
import com.academiaexpresssystem.Server.ServerApi;
import com.academiaexpresssystem.Utils.AndroidUtilities;
import com.academiaexpresssystem.Utils.DateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private ViewPager mPager;
    private String[] titles = { getString(R.string.new_code), getString(R.string.assigned_code) };

    private OrdersFragment first;
    private OrdersFragment second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_layout);

        initElements();
        initTabs();
        setOnClickListeners();
    }

    private void initElements() {
        first = new OrdersFragment();
        second = new OrdersFragment();

        NavigationAdapter mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(titles.length);
    }

    private void initTabs() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setOnClickListeners() {
        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();

        ServerApi.get(this).api().updateUser(generateJson(), DeviceInfoStore.getToken(this)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    goToAuthActivity();
                } else {
                    onInternetConnectionError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                onInternetConnectionError();
            }
        });
    }

    private void goToAuthActivity() {
        Intent intent = new Intent(OrdersActivity.this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private JsonObject generateJson() {
        JsonObject object = new JsonObject();
        JsonObject user = new JsonObject();

        user.addProperty("device_token", "");
        user.addProperty("platform", "");
        object.add("courier", user);

        return object;
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                load();
            }
        });
    }

    private void onInternetConnectionError() {
        Snackbar.make(findViewById(R.id.main), R.string.internet_connection_error, Snackbar.LENGTH_SHORT).show();
    }

    private void getAssigned() {
        ServerApi.get(this).api().assigned(DeviceInfoStore.getToken(this)).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    parseOrders(response.body(), true);
                } else {
                    onInternetConnectionError();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                onInternetConnectionError();
            }
        });
    }

    private void getOrders() {
        ServerApi.get(this).api().getOrders(DeviceInfoStore.getToken(this)).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    parseOrders(response.body(), false);
                } else {
                    onInternetConnectionError();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                onInternetConnectionError();
            }
        });
    }

    private ArrayList<String> parseIngredients(JsonArray lineItems) {
        ArrayList<String> ingredients = new ArrayList<>();

        for (int j = 0; j < lineItems.size(); j++) {
            JsonObject item = lineItems.get(j).getAsJsonObject();
            JsonObject dish = item.get("dish").getAsJsonObject();
            ingredients.add(item.get("quantity").getAsString() + "=" +
                    (dish.get("id").getAsString() + " " + dish.get("name").getAsString()) + "=" +
                    dish.get("price") + "=" +
                    getDescription(dish)
            );
        }

        return ingredients;
    }

    private String getDescription(JsonObject dish) {
        return dish.get("description") == null ||
                dish.get("description").isJsonNull() ||
                dish.get("description").getAsString().isEmpty() ?
                getString(R.string.no_description) :
                dish.get("description").getAsString();
    }

    private String getDateFromJson(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            return DateUtils.INSTANCE.getDateStringRepresentation(format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getDateObjectFromJson(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Order parseOrder(JsonObject object, JsonObject user) {
        return new Order(
                AndroidUtilities.INSTANCE.getStringFieldFromJson(user.get("last_name")) + " " +
                        AndroidUtilities.INSTANCE.getStringFieldFromJson(user.get("first_name")),
                AndroidUtilities.INSTANCE.getStringFieldFromJson(user.get("phone_number")),
                getDateFromJson(AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("scheduled_for"))),
                AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("address")),
                parseIngredients(object.get("line_items").getAsJsonArray()),
                AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("status")).equals("new"),
                AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("latitude")) + "=" +
                        AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("longitude")),
                AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("id")),
                AndroidUtilities.INSTANCE.getIntFieldFromJson(object.get("total_price")),
                getDateObjectFromJson(AndroidUtilities.INSTANCE.getStringFieldFromJson(object.get("scheduled_for")))
        );
    }

    private void parseOrders(JsonArray array, boolean isAssigned) {
        ArrayList<Order> orders = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            orders.add(parseOrder(object, object.get("user").getAsJsonObject()));
        }

        Collections.sort(orders, new Comparator<Order>() {
            public int compare(Order first, Order second) {
                return first.getDate().compareTo(second.getDate());
            }
        });

        setCollectionToFragment(isAssigned ? second : first, orders);
    }

    private void setCollectionToFragment(OrdersFragment fragment, ArrayList<Order> orders) {
        fragment.setCollection(orders);
        fragment.end();
    }

    public void load() {
        getOrders();
        getAssigned();
    }

    private class NavigationAdapter extends FragmentPagerAdapter {

        NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? first : second;
        }

        @Override
        public int getCount() {
            return titles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}