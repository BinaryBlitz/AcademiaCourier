package com.academiaexpresssystem.Activities;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.academiaexpresssystem.ProgressDialog;
import com.academiaexpresssystem.Push.QuickstartPreferences;
import com.academiaexpresssystem.R;
import com.academiaexpresssystem.Push.RegistrationIntentService;
import com.academiaexpresssystem.Server.DeliveryServerRequest;
import com.academiaexpresssystem.Server.DeviceInfoStore;
import com.academiaexpresssystem.Server.OnRequestPerformedListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);

        DeviceInfoStore.resetToken(this);

        final ProgressDialog dialog1 = new ProgressDialog();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                dialog1.dismiss();
                Intent intent2 = new Intent(AuthActivity.this, OrdersActivity.class);
                startActivity(intent2);
                finish();
            }
        };

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    final ProgressDialog dialog = new ProgressDialog();
                    dialog.show(getFragmentManager(), "deliverysystemapp");

                    JSONObject object = new JSONObject();

                    try {
                        object.accumulate("phone_number", ((EditText) findViewById(R.id.edittext1)).getText().toString());
                        object.accumulate("password", ((EditText) findViewById(R.id.edittext2)).getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DeliveryServerRequest.with(AuthActivity.this)
                            .skipAuth()
                            .objects(object)
                            .listener(new OnRequestPerformedListener() {
                                @Override
                                public void onRequestPerformedListener(Object... objects) {
                                    dialog.dismiss();
                                    if (objects[0].equals("Error")) {
                                        Snackbar.make(findViewById(R.id.main), "Ошибка авторизации.", Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            DeviceInfoStore.saveToken(((JSONObject) objects[0]).getString("api_token"));
                                           // DeviceInfoStore.saveId(((JSONObject) objects[0]).getString("id"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if (checkPlayServices()) {
                                            Intent intent = new Intent(AuthActivity.this, RegistrationIntentService.class);
                                            startService(intent);
                                            dialog1.show(getFragmentManager(), "getthelpapp");
                                        }
                                    }
                                }
                            })
                            .auth()
                            .perform();
                }
            }
        });
    }

    private boolean check() {
        if(((EditText) findViewById(R.id.edittext1)).getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.main), "Поле имени не заполнено.", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if(((EditText) findViewById(R.id.edittext2)).getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.main), "Поле  пароля не заполнено.", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
