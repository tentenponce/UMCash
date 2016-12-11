package com.uhac.umcash.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uhac.umcash.R;
import com.uhac.umcash.models.MsgResponse;
import com.uhac.umcash.networks.RetroClient;
import com.uhac.umcash.networks.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * process payment
 * Created by Exequiel Egbert V. Ponce on 12/11/2016.
 */

public class PaymentActivity extends AppCompatActivity {

    private String reserveCode;
    private int companyId;

    RetroInterface retroInterface;

    CoordinatorLayout paymentCoor;
    AppCompatTextView msgTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        reserveCode = getIntent().getStringExtra("reserve_code");
        companyId = getIntent().getIntExtra("company_id", -1);

        retroInterface = RetroClient.getClient(PaymentActivity.this).create(RetroInterface.class);

        paymentCoor = (CoordinatorLayout) findViewById(R.id.paymentCoor);
        msgTV = (AppCompatTextView) findViewById(R.id.msgTV);

        IntentIntegrator scanIntegrator = new IntentIntegrator(PaymentActivity.this);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningIntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningIntentResult != null) { //if result is not null

            String scanContent = scanningIntentResult.getContents(); //reserve code
            String scanFormat = scanningIntentResult.getFormatName();

            if (scanContent != null && scanFormat != null) {
                processReservation(scanContent);
            } else {
                onBackPressed();
            }
        } else {
            onBackPressed();
        }
    }

    private void processReservation(final String accountNo) {
        //validate PIN
        final AppCompatEditText input = new AppCompatEditText(PaymentActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder
                .setTitle("Enter Password")
                .setMessage("Please enter your password for verification.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<MsgResponse> call = retroInterface.validatePassword(accountNo, input.getText().toString().trim());
                        call.enqueue(new Callback<MsgResponse>() {
                            @Override
                            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().getResponse_code() == 1) { //success
                                        Snackbar.make(paymentCoor, response.body().getResponse_msg(), Snackbar.LENGTH_LONG).show();

                                        //reserve item
                                        Call<MsgResponse> call2 = retroInterface.processPayment(reserveCode, companyId);
                                        call2.enqueue(new Callback<MsgResponse>() {
                                            @Override
                                            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                                                if (response.body() != null) {
                                                    msgTV.setText(response.body().getResponse_msg());

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startActivity(new Intent(PaymentActivity.this, ScanActivity.class));
                                                            finish();
                                                        }
                                                    }, 5000);
                                                } else {
                                                    Snackbar.make(paymentCoor, "There's something wrong.", Snackbar.LENGTH_LONG).show();
                                                    onBackPressed();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MsgResponse> call, Throwable t) {
                                                Snackbar.make(paymentCoor, "There's something wrong.", Snackbar.LENGTH_LONG).show();
                                                onBackPressed();
                                            }
                                        });

                                    } else if (response.body().getResponse_code() == -1) { //invalid
                                        Snackbar.make(paymentCoor, response.body().getResponse_msg(), Snackbar.LENGTH_LONG).show();
                                    } else if (response.body().getResponse_code() == -2) {
                                        Snackbar.make(paymentCoor, response.body().getResponse_msg(), Snackbar.LENGTH_LONG).show();
                                    } else { //wtfzxc error
                                        Snackbar.make(paymentCoor, "There's something wrong.", Snackbar.LENGTH_LONG).show();
                                        onBackPressed();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MsgResponse> call, Throwable t) {
                                Snackbar.make(paymentCoor, "There's something wrong.", Snackbar.LENGTH_LONG).show();
                                onBackPressed();
                            }
                        });
                    }
                });

        builder.setView(input, 50, 0, 50, 0);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (negativeButton != null) {
            negativeButton.setBackgroundColor(Color.WHITE);
            negativeButton.setTextColor(Color.BLACK);
        }

        if (positiveButton != null) {
            positiveButton.setBackgroundColor(Color.WHITE);
            positiveButton.setTextColor(Color.BLACK);
        }
    }
}
