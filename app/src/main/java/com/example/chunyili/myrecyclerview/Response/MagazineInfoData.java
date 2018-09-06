package com.example.chunyili.myrecyclerview.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MagazineInfoData implements Serializable {

    @SerializedName("success")
    private String success;
    @SerializedName("code")
    private String code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("items")
    private List<ItemsObjectData> items;

    public String getSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<ItemsObjectData> getItems() {
        return items;
    }

}
