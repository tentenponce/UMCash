package com.uhac.umcash.networks;


import com.uhac.umcash.models.Company;
import com.uhac.umcash.models.MsgResponse;
import com.uhac.umcash.models.ReservationGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Holds all http requests of the app
 * Created by UHAC CALOOCAN on 12/10/2016.
 */
public interface RetroInterface {

    @GET("validateLogin.php")
    Call<Company> validateLogin(
            @Query("company_username") String username,
            @Query("company_password") String password
    );

    @GET("getReservation.php")
    Call<ReservationGroup> getReservation(
            @Query("reserve_code") String reserveCode,
            @Query("company_id") int companyId
    );

    @GET("validatePassword.php")
    Call<MsgResponse> validatePassword(
            @Query("bai_account_no") String accountNo,
            @Query("bai_password") String password
    );

    @GET("processPayment.php")
    Call<MsgResponse> processPayment(
            @Query("reserve_code") String reserveCode,
            @Query("company_id") int companyId
    );
}
