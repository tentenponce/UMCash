package com.uhac.umcash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.uhac.umcash.R;
import com.uhac.umcash.models.Company;
import com.uhac.umcash.networks.RetroClient;
import com.uhac.umcash.networks.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    CoordinatorLayout loginCoor;

    AppCompatEditText usernameEDT;
    AppCompatEditText passwordEDT;

    AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCoor = (CoordinatorLayout) findViewById(R.id.loginCoor);
        usernameEDT = (AppCompatEditText) findViewById(R.id.usernameEDT);
        passwordEDT = (AppCompatEditText) findViewById(R.id.passwordEDT);
        loginBtn = (AppCompatButton) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetroInterface retroInterface = RetroClient.getClient(LoginActivity.this).create(RetroInterface.class);
                Call<Company> call = retroInterface.validateLogin(usernameEDT.getText().toString().trim(), passwordEDT.getText().toString().trim());

                call.enqueue(new Callback<Company>() {
                    @Override
                    public void onResponse(Call<Company> call, Response<Company> response) {
                        if (response.body() != null) {
                            Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                            intent.putExtra("company", response.body());
                            startActivity(intent);

                            finish();
                        } else {
                            Snackbar.make(loginCoor, "There's something wrong.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Company> call, Throwable t) {
                        Snackbar.make(loginCoor, "There's something wrong.", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
