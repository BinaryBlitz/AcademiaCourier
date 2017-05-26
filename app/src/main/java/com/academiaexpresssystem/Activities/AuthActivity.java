package com.academiaexpresssystem.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.academiaexpresssystem.Push.RegistrationIntentService;
import com.academiaexpresssystem.R;
import com.academiaexpresssystem.Server.DeviceInfoStore;
import com.academiaexpresssystem.Server.ServerApi;
import com.academiaexpresssystem.Utils.AndroidUtilities;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);

        DeviceInfoStore.resetToken(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    executeRequest();
                }
            }
        });
    }

    private JsonObject generateJson() {
        JsonObject object = new JsonObject();

        object.addProperty("phone_number", ((EditText) findViewById(R.id.edittext1)).getText().toString());
        object.addProperty("password", ((EditText) findViewById(R.id.edittext2)).getText().toString());

        return object;
    }

    private void parseAnswer(JsonObject object) {
        try {
            DeviceInfoStore.saveToken(this, object.get("api_token").getAsString());
            DeviceInfoStore.saveId(this, object.get("id").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AndroidUtilities.INSTANCE.checkPlayServices(this)) {
            Intent intent = new Intent(AuthActivity.this, RegistrationIntentService.class);
            startService(intent);
        }

        Intent intent = new Intent(AuthActivity.this, OrdersActivity.class);
        startActivity(intent);
        finish();
    }

    private void executeRequest() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();

        ServerApi.get(this).api().auth(generateJson()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    parseAnswer(response.body());
                } else {
                    Snackbar.make(findViewById(R.id.main), R.string.auth_error, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Snackbar.make(findViewById(R.id.main), R.string.internet_connection_error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check() {
        if (((EditText) findViewById(R.id.edittext1)).getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.main), R.string.name_error, Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (((EditText) findViewById(R.id.edittext2)).getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.main), R.string.password_error, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
