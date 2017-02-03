package com.academiaexpresssystem.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.academiaexpresssystem.Data.Order;
import com.academiaexpresssystem.Fragments.OrdersFragment;
import com.academiaexpresssystem.ProgressDialog;
import com.academiaexpresssystem.R;
import com.academiaexpresssystem.Server.DeliveryServerRequest;
import com.academiaexpresssystem.Server.OnRequestPerformedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class OrdersActivity extends AppCompatActivity {

    private ViewPager mPager;
    NavigationAdapter mPagerAdapter;
    TabLayout tabLayout;
    private String[] imageResId = {
            "НОВЫЕ",
            "ПРИНЯТЫЕ"
    };

    OrdersFragment first;
    OrdersFragment second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_layout);

        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(3);


        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog dialog = new ProgressDialog();
                dialog.show(getFragmentManager(), "deliverysystemapp");
                JSONObject object = new JSONObject();
                JSONObject user = new JSONObject();

                try {
                    user.put("device_token", JSONObject.NULL);
                    user.put("platform", JSONObject.NULL);
                    object.put("courier", user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DeliveryServerRequest.with(getApplicationContext())
                        .skipAuth()
                        .listener(new OnRequestPerformedListener() {
                            @Override
                            public void onRequestPerformedListener(Object... objects) {
                                Intent intent2 = new Intent(OrdersActivity.this, AuthActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent2);
                                finish();
                            }
                        })
                        .objects(object)
                        .updateUser()
                        .perform();

            }
        });

        first = new OrdersFragment();
        second = new OrdersFragment();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        tabLayout.setBackgroundColor(Color.TRANSPARENT);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    public void load() {
        DeliveryServerRequest.with(this)
                .authorize()
                .listener(new OnRequestPerformedListener() {
                    @Override
                    public void onRequestPerformedListener(Object... objects) {
                        try {
                            ArrayList<Order> first = new ArrayList<>();

                            JSONArray array = (JSONArray) objects[0];

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                ArrayList<String> ingrs = new ArrayList<>();

                                JSONArray array1 = object.getJSONArray("line_items");

                                for (int j = 0; j < array1.length(); j++) {
                                    ingrs.add(
                                            array1.getJSONObject(j).getString("quantity") + "=" +
                                                    (array1.getJSONObject(j).getJSONObject("dish").getString("id") + " " +
                                                            array1.getJSONObject(j).getJSONObject("dish").getString("name")) + "=" +
                                                    array1.getJSONObject(j).getJSONObject("dish").getInt("price") + "=" +
                                                    (array1.getJSONObject(j).getJSONObject("dish").isNull("description") ||
                                                            array1.getJSONObject(j).getJSONObject("dish").getString("description").isEmpty() ?
                                                    "Нет описания" : array1.getJSONObject(j).getJSONObject("dish").getString("description"))
                                    );
                                }

                                Calendar start = Calendar.getInstance();

                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                                    Date date = format.parse(object.getString("scheduled_for"));
                                    start.setTime(date);
                                } catch (Exception ignored) {}
                                Order order = new Order(
                                        object.getJSONObject("user").getString("last_name") + " " + object.getJSONObject("user").getString("first_name"),
                                        object.getJSONObject("user").getString("phone_number"),
                                        getDateStringRepresentation(start),
                                        object.getString("address"),
                                        ingrs,
                                        object.getString("status").equals("new"),
                                        object.getString("latitude") + "=" +  object.getString("longitude"),
                                        object.getString("id"),
                                        object.getInt("total_price")
                                );

                                order.setCalendar(start);

                                first.add(order);
                            }

                            Collections.sort(first, new Comparator<Order>() {
                                public int compare(Order o1, Order o2) {
                                    return o1.getCalendar().compareTo(o2.getCalendar());
                                }
                            });

                            OrdersActivity.this.first.setCollection(first);
                            OrdersActivity.this.first.end();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .orders()
                .perform();

        DeliveryServerRequest.with(this)
                .authorize()
                .listener(new OnRequestPerformedListener() {
                    @Override
                    public void onRequestPerformedListener(Object... objects) {
                        try {
                            ArrayList<Order> first = new ArrayList<>();
                            ArrayList<Order> second = new ArrayList<>();

                            JSONArray array = (JSONArray) objects[0];

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                ArrayList<String> ingrs = new ArrayList<String>();

                                JSONArray array1 = object.getJSONArray("line_items");

                                for (int j = 0; j < array1.length(); j++) {
                                    ingrs.add(
                                            array1.getJSONObject(j).getString("quantity") + "=" +
                                                    (array1.getJSONObject(j).getJSONObject("dish").getString("id") + " " +
                                                    array1.getJSONObject(j).getJSONObject("dish").getString("name")) + "=" +
                                                    array1.getJSONObject(j).getJSONObject("dish").getInt("price") + "=" +
                                                    (array1.getJSONObject(j).getJSONObject("dish").isNull("subtitle") ?
                                                            "Нет описания" : array1.getJSONObject(j).getJSONObject("dish").getString("subtitle"))
                                    );
                                }

                                Calendar start = Calendar.getInstance();

                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                                    Date date = format.parse(object.getString("scheduled_for"));
                                    start.setTime(date);
                                } catch (Exception ignored) {}

                                Order order = new Order(
                                        object.getJSONObject("user").getString("last_name") + " " + object.getJSONObject("user").getString("first_name"),
                                        object.getJSONObject("user").getString("phone_number"),
                                        getDateStringRepresentation(start),
                                        object.getString("address"),
                                        ingrs,
                                        object.getString("status").equals("new"),
                                        object.getString("latitude") + "=" +  object.getString("longitude"),
                                        object.getString("id"),
                                        object.getInt("total_price")
                                );
                                order.setCalendar(start);
                                second.add(order);
                            }

                            Collections.sort(first, new Comparator<Order>() {
                                public int compare(Order o1, Order o2) {
                                    return o1.getCalendar().compareTo(o2.getCalendar());
                                }
                            });

                            OrdersActivity.this.second.setCollection(second);
                            OrdersActivity.this.second.end();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .assigned()
                .perform();
    }

    public static String getDateStringRepresentation(Calendar startTime) {
        String date = Integer.toString(startTime.get(Calendar.DAY_OF_MONTH)) + "." +
                (startTime.get(Calendar.MONTH) + 1 > 9 ? (startTime.get(Calendar.MONTH) + 1) : "0" + (startTime.get(Calendar.MONTH) + 1)) + "." +
                Integer.toString(startTime.get(Calendar.YEAR));
        date += " ";
        date += (startTime.get(Calendar.HOUR_OF_DAY) > 9 ?
                Integer.toString(startTime.get(Calendar.HOUR_OF_DAY)) :
                "0" + Integer.toString(startTime.get(Calendar.HOUR_OF_DAY)))
                + ":" + (startTime.get(Calendar.MINUTE) > 9 ?
                Integer.toString(startTime.get(Calendar.MINUTE)) :
                "0" + Integer.toString(startTime.get(Calendar.MINUTE)));

        return date;
    }

    public static boolean isAfter(Calendar first, Calendar second) {
        return !first.before(second);
    }

    private class NavigationAdapter extends FragmentPagerAdapter {

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? first : second;
        }

        @Override
        public int getCount() {
            return imageResId.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return OrdersActivity.this.imageResId[position];
        }
    }
}