package com.uhac.umcash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.uhac.umcash.R;
import com.uhac.umcash.models.Company;
import com.uhac.umcash.models.ProductOrder;
import com.uhac.umcash.models.ReservationGroup;
import com.uhac.umcash.networks.RetroClient;
import com.uhac.umcash.networks.RetroInterface;
import com.uhac.umcash.utilities.Caloocan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * scan then list the reservation
 * Created by Exequiel Egbert V. Ponce on 12/11/2016.
 */

public class ScanActivity extends AppCompatActivity {

    private ReservationGroup reservationGroup;
    private Company company;

    AppCompatButton scanBtn;

    CoordinatorLayout scanCoor;

    AppCompatTextView accountNameTV;
    AppCompatTextView reserveCodeTV;
    AppCompatTextView totalPriceTV;
    AppCompatTextView dateAgoTV;

    LinearLayout reserveLin;

    FloatingActionButton processFab;

    RetroInterface retroInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        company = getIntent().getParcelableExtra("company");

        retroInterface = RetroClient.getClient(ScanActivity.this).create(RetroInterface.class);

        scanCoor = (CoordinatorLayout) findViewById(R.id.scanCoor);
        scanBtn = (AppCompatButton) findViewById(R.id.scanBtn);
        accountNameTV = (AppCompatTextView) findViewById(R.id.accountNameTV);
        reserveCodeTV = (AppCompatTextView) findViewById(R.id.reserveCodeTV);
        totalPriceTV = (AppCompatTextView) findViewById(R.id.totalPriceTV);
        dateAgoTV = (AppCompatTextView) findViewById(R.id.dateAgoTV);
        reserveLin = (LinearLayout) findViewById(R.id.reserveLin);
        processFab = (FloatingActionButton) findViewById(R.id.processFab);

        processFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reservationGroup != null) {
                    Intent intent = new Intent(ScanActivity.this, PaymentActivity.class);
                    intent.putExtra("reserve_code", reservationGroup.getCode());
                    intent.putExtra("company_id", company.getId());
                    startActivity(intent);
                }
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(ScanActivity.this);
                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningIntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningIntentResult != null) { //if result is not null

            String scanContent = scanningIntentResult.getContents(); //reserve code
            String scanFormat = scanningIntentResult.getFormatName();

            if(scanContent != null && scanFormat != null) {
                Call<ReservationGroup> call = retroInterface.getReservation(scanContent, company.getId());

                call.enqueue(new Callback<ReservationGroup>() {
                    @Override
                    public void onResponse(Call<ReservationGroup> call, Response<ReservationGroup> response) {
                        if (response.body() != null) {
                            if (response.body().getProductOrders() != null) {
                                loadReservation(response.body());
                            } else {
                                Snackbar.make(scanCoor, "Reservation cannot recognized.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(scanCoor, "Something wrong.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReservationGroup> call, Throwable t) {
                        Snackbar.make(scanCoor, "Something wrong.", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void loadReservation(ReservationGroup reservationGroup) {
        this.reservationGroup = reservationGroup;

        reserveLin.removeAllViews();
        accountNameTV.setText(reservationGroup.getAccountName());
        reserveCodeTV.setText(reservationGroup.getCode());
        dateAgoTV.setText(Caloocan.dateDifference(
                Caloocan.FORMATTER.format(reservationGroup.getDateToday()),
                Caloocan.FORMATTER.format(reservationGroup.getReservedDate())));

        double totalPrice = 0;
        for (ProductOrder productOrder : reservationGroup.getProductOrders()) {
            double orderPrice = productOrder.getQuantity() * productOrder.getProductItem().getPrice();
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_product_reserve, null);
            AppCompatTextView itemNameTV = (AppCompatTextView) linearLayout.findViewById(R.id.itemNameTV);
            AppCompatTextView itemQtyTV = (AppCompatTextView) linearLayout.findViewById(R.id.itemQtyTV);
            AppCompatTextView totalPriceTV = (AppCompatTextView) linearLayout.findViewById(R.id.totalPriceTV);

            itemNameTV.setText(productOrder.getProductItem().getName());
            itemQtyTV.setText("x" + productOrder.getQuantity());
            totalPriceTV.setText(Caloocan.PESO_SIGN + " " + Caloocan.numberFormat.format(orderPrice));

            reserveLin.addView(linearLayout);

            totalPrice += orderPrice;
        }

        totalPriceTV.setText(Caloocan.PESO_SIGN + " " + Caloocan.numberFormat.format(totalPrice));
    }

    private void resetScan() {
        reserveLin.removeAllViews();
        reservationGroup = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        resetScan();
    }
}
