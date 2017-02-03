package com.academiaexpresssystem.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.academiaexpresssystem.Data.Order;
import com.academiaexpresssystem.ProgressDialog;
import com.academiaexpresssystem.R;
import com.academiaexpresssystem.Server.DeliveryServerRequest;
import com.academiaexpresssystem.Server.OnRequestPerformedListener;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderActivity extends AppCompatActivity {

    public static Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(OrderActivity.this, AuthActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finish();
            }
        });

        if(order.isStatus()) {
            findViewById(R.id.fab13).setVisibility(View.VISIBLE);
            findViewById(R.id.fab23).setVisibility(View.GONE);
        } else {
            findViewById(R.id.fab23).setVisibility(View.VISIBLE);
            findViewById(R.id.fab13).setVisibility(View.GONE);
        }

        findViewById(R.id.fab13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(OrderActivity.this)
                        .title("AE Курьер")
                        .content("Вы уверены?")
                        .positiveText("Да")
                        .positiveColor(Color.parseColor("#212121"))
                        .negativeColor(Color.parseColor("#212121"))
                        .negativeText("Нет")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog1, DialogAction which) {
                                final ProgressDialog dialog = new ProgressDialog();
                                dialog.show(getFragmentManager(), "deliverysystemapp");
                                DeliveryServerRequest.with(OrderActivity.this)
                                        .authorize()
                                        .listener(new OnRequestPerformedListener() {
                                            @Override
                                            public void onRequestPerformedListener(Object... objects) {
                                                dialog.dismiss();
                                                findViewById(R.id.fab23).setVisibility(View.VISIBLE);
                                                findViewById(R.id.fab13).setVisibility(View.GONE);
                                            }
                                        })
                                        .attachOrder(order.getId())
                                        .perform();
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.fab23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(OrderActivity.this)
                        .title("AE Курьер")
                        .content("Вы уверены?")
                        .positiveText("Да")
                        .positiveColor(Color.parseColor("#212121"))
                        .negativeColor(Color.parseColor("#212121"))
                        .negativeText("Нет")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog1, DialogAction which) {
                                final ProgressDialog dialog = new ProgressDialog();
                                dialog.show(getFragmentManager(), "deliverysystemapp");

                                final JSONObject object = new JSONObject();
                                try {
                                    object.accumulate("status", "delivered");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONObject toSend = new JSONObject();
                                try {
                                    toSend.accumulate("order", object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                DeliveryServerRequest.with(OrderActivity.this)
                                        .authorize()
                                        .objects(toSend)
                                        .listener(new OnRequestPerformedListener() {
                                            @Override
                                            public void onRequestPerformedListener(Object... objects) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .completeOrder(order.getId())
                                        .perform();
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.gender2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri gmmIntentUri = Uri.parse("geo:" +
                            Double.parseDouble(order.getPosition().split("=")[0]) + "," +
                            Double.parseDouble(order.getPosition().split("=")[1]) +
                            "?q=" +
                            Double.parseDouble(order.getPosition().split("=")[0]) + "," +
                            Double.parseDouble(order.getPosition().split("=")[1]));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } catch (Exception ignored) {

                }
            }
        });

        ((TextView) findViewById(R.id.info_text)).setText(order.getTime());
        ((TextView) findViewById(R.id.info_text4)).setText(Integer.toString(order.getPrice())
                + Html.fromHtml("<html>&#x20bd</html>").toString());
        ((TextView) findViewById(R.id.gender2)).setText(order.getLocation());
        ((TextView) findViewById(R.id.address)).setText(order.getUserName());
        ((TextView) findViewById(R.id.gender)).setText(order.getUserPhone());

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + order.getUserPhone();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(OrderActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                2);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            }
        });

        ((LinearLayout) findViewById(R.id.ingr)).removeAllViews();
        if(order.getIngridients().size() == 0) {
            findViewById(R.id.ingr).setVisibility(View.GONE);
            findViewById(R.id.card_view2d3).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ingr).setVisibility(View.VISIBLE);
            findViewById(R.id.card_view2d3).setVisibility(View.VISIBLE);
            for (int i = 0; i < order.getIngridients().size(); i++) {
                View v = LayoutInflater.from(this).inflate(R.layout.lunch_part_card, null);

                ((TextView) v.findViewById(R.id.textView43)).setText(order.getIngridients().get(i).split("=")[1]);
                ((TextView) v.findViewById(R.id.textView44)).setText("X" + order.getIngridients().get(i).split("=")[0]);
                ((TextView) v.findViewById(R.id.textView4)).setText(order.getIngridients().get(i).split("=")[3]);
                ((LinearLayout) findViewById(R.id.ingr)).addView(v);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String uri = "tel:" + order.getUserPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
