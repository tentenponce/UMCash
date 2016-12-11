package com.uhac.umcash.models;

import com.google.gson.annotations.SerializedName;

/**
 * normal text or by code handler from php
 * Created by Exequiel Egbert V. Ponce on 12/10/2016.
 */

public class MsgResponse {

    @SerializedName("response_code")
    private int response_code;

    @SerializedName("response_msg")
    private String response_msg;

    public int getResponse_code() {
        return response_code;
    }

    public String getResponse_msg() {
        return response_msg;
    }
}

